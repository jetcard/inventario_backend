package com.inventarios.handler.comunes.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.comunes.response.ComunResponseRest;
import com.inventarios.model.Comun;
import com.inventarios.model.Categoria;
import com.inventarios.model.Custodio;
import com.inventarios.model.Tipo;
import java.sql.SQLException;
import java.util.*;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class CreateComunAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    protected final static Table<Record> COMUN_TABLE = DSL.table("comun");
    protected final static Table<Record> RESPONSABLE_TABLE = DSL.table("custodio");
    protected final static Table<Record> TIPO_TABLE = DSL.table("tipo");
    protected final static Table<Record> GRUPO_TABLE = DSL.table("categoria");
    final static Map<String, String> headers = new HashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    static {
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
        headers.put("Access-Control-Allow-Methods", "POST");
    }

    protected abstract void save(Comun comun, Long custodioId, Long tipoID, Long categoriaId) throws SQLException;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        input.setHeaders(headers);
        LambdaLogger logger = context.getLogger();
        ComunResponseRest responseRest = new ComunResponseRest();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
        List<Comun> list = new ArrayList<>();
        String output = "";
        String contentTypeHeader = input.getHeaders().get("Content-Type");
        logger.log("Content-Type: " + contentTypeHeader);
        try {


            String body = input.getBody();
            //String body = "{\"modelo\":\"AS\",\"marca\":\"AS\",\"nroserie\":\"1\",\"fechacompra\":\"1\",\"importe\":777,\"categoriaId\":1,\"account\":55555}";
            logger.log("##################### BODY COMUN ######################");
            logger.log(body);
            logger.log("#######################################################");

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            //Comun comun = new Gson().fromJson(body, Comun.class);
            Comun comun = gson.fromJson(body, Comun.class);

            logger.log("debe llegar aquí 1 ya tenemos Comun");
            if (comun == null) {
                return response
                        .withBody("El cuerpo de la solicitud no contiene datos válidos para un comun")
                        .withStatusCode(400);
            }
            logger.log("debe llegar aquí 2");
            // Obtener el ID del grupo del objeto Comun
            //Long categoriaId = comun.getGrupo().getId();


            logger.log("Comun: ");

            //if (comun != null) {
            logger.log("Comun.getId() = " + comun.getId());
            logger.log("Comun.getDescripcomun() = " + comun.getDescripcomun());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(body);

            Long custodioId = null;
            Long tipoID = null;
            Long categoriaId = null;
            try {
                custodioId = Long.parseLong(jsonNode.get("custodioId").asText());
                tipoID = Long.parseLong(jsonNode.get("tipoId").asText());
                categoriaId = Long.parseLong(jsonNode.get("categoriaId").asText());
            } catch (NumberFormatException e) {
                return response
                        .withBody("Invalid id in path")
                        .withStatusCode(400);
            }


            /*String bodyparaque = "{\"modelo\":\"+comun.getModelo()+\",\"marca\":\"+comun.getMarca()+" +
                    "\",\"nroserie\":\"+comun.getNroserie()+\",\"fechacompra\":\"+comun.getFechacompra()+" +
                    "\",\"importe\":+comun.getImporte()+,\"categoriaId\":1,\"account\":+comun.getAccount()}";
            logger.log("bodyparaque: ");
            logger.log(bodyparaque);*/
            DSLContext dsl = RDSConexion.getDSL();

            Optional<Custodio> responsableSearch = dsl.select()
                    .from(RESPONSABLE_TABLE)
                    .where(DSL.field("id", Long.class).eq(custodioId))
                    .fetchOptionalInto(Custodio.class);
            if (!responsableSearch.isPresent()) {
                return response
                        .withBody("El responsable especificado no existe")
                        .withStatusCode(404);
            }

            Optional<Tipo> tipoSearch = dsl.select()
                    .from(TIPO_TABLE)
                    .where(DSL.field("id", Long.class).eq(tipoID))
                    .fetchOptionalInto(Tipo.class);
            if (!tipoSearch.isPresent()) {
                return response
                        .withBody("El tipo especificado no existe")
                        .withStatusCode(404);
            }

            Optional<Categoria> grupoSearch = dsl.select()
                    .from(GRUPO_TABLE)
                    .where(DSL.field("id", Long.class).eq(categoriaId))
                    .fetchOptionalInto(Categoria.class);
            if (!grupoSearch.isPresent()) {
                return response
                        .withBody("El grupo especificado no existe")
                        .withStatusCode(404);
            }
            //if (grupoSearch.isPresent()) {
            logger.log("grupoSearch.isPresent()");
            logger.log("Comun.getGrupo I  : "+comun.getGrupo());
          /*byte[] compressedPicture = null;
          if (comun.getPicture() != null) {
            compressedPicture = Util.compressZLib(comun.getPicture());
          }*/
            logger.log(":::::::::::::::::::::::::::::::::: PREPARANDO PARA INSERTAR ::::::::::::::::::::::::::::::::::");

            comun.setResponsable(responsableSearch.get());
            comun.setTipo(tipoSearch.get());
            comun.setGrupo(grupoSearch.get());
            logger.log("Comun.getGrupo II : "+comun.getGrupo());

            save(comun, custodioId, tipoID, categoriaId);
            logger.log(":::::::::::::::::::::::::::::::::: INSERCIÓN COMPLETA ::::::::::::::::::::::::::::::::::");
            list.add(comun);
            responseRest.getComunResponse().setListacomunes(list);
            responseRest.setMetadata("Respuesta ok", "00", "Común guardado");

            output = new Gson().toJson(responseRest);
            return response.withStatusCode(200)
                    .withBody(output);

        } catch (Exception e) {
            responseRest.setMetadata("Respuesta nok", "-1", "Error al insertar");
            return response
                    .withBody(new Gson().toJson(responseRest))
                    .withStatusCode(500);
        }
    }

}

