package com.inventarios.handler.especificos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.especificos.response.EspecificosResponseRest;
import com.inventarios.model.Especificos;
import java.sql.SQLException;
import java.util.*;
import com.inventarios.model.Grupo;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class CreateEspecificosAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    protected final static Table<Record> ESPECIFICOS_TABLE = DSL.table("especificos");
    protected final static Table<Record> GRUPO_TABLE = DSL.table("grupo");
    final static Map<String, String> headers = new HashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    static {
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
        headers.put("Access-Control-Allow-Methods", "POST");
    }

    protected abstract void save(Especificos especificos, Long grupoID) throws SQLException;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        input.setHeaders(headers);
        LambdaLogger logger = context.getLogger();
        EspecificosResponseRest responseRest = new EspecificosResponseRest();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
        List<Especificos> list = new ArrayList<>();
        String output = "";
        String contentTypeHeader = input.getHeaders().get("Content-Type");
        logger.log("Content-Type: " + contentTypeHeader);
        try {


            String body = input.getBody();
            //String body = "{\"modelo\":\"AS\",\"marca\":\"AS\",\"nroserie\":\"1\",\"fechacompra\":\"1\",\"importe\":777,\"grupoId\":1,\"account\":55555}";
            logger.log("######################################### BODY ################################################");
            logger.log(body);
            logger.log("###############################################################################################");



            Especificos especificos = new Gson().fromJson(body, Especificos.class);

            logger.log("debe llegar aquí 1 ya tenemos Especificos");
            if (especificos == null) {
                return response
                        .withBody("El cuerpo de la solicitud no contiene datos válidos para un especificos")
                        .withStatusCode(400);
            }
            logger.log("debe llegar aquí 2");
            // Obtener el ID del grupo del objeto Especificos
            //Long grupoId = especificos.getGrupo().getId();


            logger.log("Especificos: ");

            //if (especificos != null) {
            logger.log("Especificos.getId() = " + especificos.getId());
            //logger.log("Especificos.getDescripespecifico() = " + especificos.getDescripespecifico());
            logger.log("Especificos.getNombreespecifico() = " + especificos.getNombreespecifico());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(body);

            // Acceder a los datos del cuerpo de la solicitud
            String grupoId = jsonNode.get("grupoId").asText();

            logger.log("id from path grupoId --->  " + grupoId);
            Long grupoID = null;
            try {
                grupoID = Long.parseLong(grupoId);
                logger.log("ID del grupo: " + grupoID);
            } catch (NumberFormatException e) {
                return response
                        .withBody("Invalid id in path")
                        .withStatusCode(400);
            }
            /*String bodyparaque = "{\"modelo\":\"+especificos.getModelo()+\",\"marca\":\"+especificos.getMarca()+" +
                    "\",\"nroserie\":\"+especificos.getNroserie()+\",\"fechacompra\":\"+especificos.getFechacompra()+" +
                    "\",\"importe\":+especificos.getImporte()+,\"grupoId\":1,\"account\":+especificos.getAccount()}";
            logger.log("bodyparaque: ");
            logger.log(bodyparaque);*/
            DSLContext dsl = RDSConexion.getDSL();
            Optional<Grupo> grupoSearch = dsl.select()
                    .from(GRUPO_TABLE)
                    .where(DSL.field("id", Long.class).eq(grupoID))
                    .fetchOptionalInto(Grupo.class);
            if (!grupoSearch.isPresent()) {
                //Long grupoId = especificos.getGrupo().getId();

                return response
                        .withBody("El grupo especificado no existe")
                        .withStatusCode(404);
            }
            //if (grupoSearch.isPresent()) {
            logger.log("grupoSearch.isPresent()");
        /*    logger.log("Especificos.getGrupo I  : "+especificos.getGrupo());
            especificos.setGrupo(grupoSearch.get());
            logger.log("Especificos.getGrupo II : "+especificos.getGrupo());*/
          /*byte[] compressedPicture = null;
          if (especificos.getPicture() != null) {
            compressedPicture = Util.compressZLib(especificos.getPicture());
          }*/
            logger.log(":::::::::::::::::::::::::::::::::: PREPARANDO PARA INSERTAR ::::::::::::::::::::::::::::::::::");
            save(especificos, grupoID);
            logger.log(":::::::::::::::::::::::::::::::::: INSERCIÓN COMPLETA ::::::::::::::::::::::::::::::::::");
            list.add(especificos);
            responseRest.getEspecificosResponse().setListaespecificoss(list);
            responseRest.setMetadata("Respuesta ok", "00", "Especificos guardado");
              /*} else {
                responseRest.setMetadata("Respuesta nok", "-1", "Especificos no encontrado");
              }*/
            output = new Gson().toJson(responseRest);
            return response.withStatusCode(200)
                    .withBody(output);
            /*} else {
              return response
                      .withBody("Invalid group data in request body, especificos is null")
                      .withStatusCode(400);
            }*/
        /*} else {

      }*/
        } catch (Exception e) {
            responseRest.setMetadata("Respuesta nok", "-1", "Error al insertar");
            return response
                    .withBody(new Gson().toJson(responseRest))
                    .withStatusCode(500);
        }
    }

}

