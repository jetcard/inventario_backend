package com.inventarios.handler.activos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.activos.response.ActivoResponseRest;
import com.inventarios.model.*;
import java.sql.SQLException;
import java.util.*;

import com.inventarios.util.GsonFactory;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class CreateActivoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    protected final static Table<Record> ACTIVO_TABLE = DSL.table("activo");
    protected final static Table<Record> RESPONSABLE_TABLE = DSL.table("responsable");
    protected final static Table<Record> TIPO_TABLE = DSL.table("tipo");
    protected final static Table<Record> GRUPO_TABLE = DSL.table("grupo");
    protected final static Table<Record> ARTICULO_TABLE = DSL.table("articulo");
    protected final static Table<Record> PROVEEDOR_TABLE = DSL.table("proveedor");

    final static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
        headers.put("Access-Control-Allow-Methods", "POST");
    }

    protected abstract void save(Activo activo, Long responsableID, Long tipoID, Long grupoID,
                                 Long articuloID,
                                 Long proveedorID) throws SQLException;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        //input.setHeaders(headers);
        LambdaLogger logger = context.getLogger();
        ActivoResponseRest responseRest = new ActivoResponseRest();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
        List<Activo> list = new ArrayList<>();
        String output = "";
        String contentTypeHeader = input.getHeaders().get("Content-Type");
        logger.log("Content-Type: " + contentTypeHeader);
        try {
            String body = input.getBody();
            //String body ="{\"modelo\":\"sygfd\",\"marca\":\"fdsfdsa\",\"nroserie\":\"fas4343432\",\"fechacompra\":\"2024-10-05\",\"importe\":24,\"account\":2,\"responsableId\":1, \"grupoId\": 1, \"tipoId\": 1, \"articuloId\": 2 }";
            //String body = "{\"modelo\":\"AS\",\"marca\":\"AS\",\"nroserie\":\"1\",\"fechacompra\":\"1\",\"importe\":777,\"grupoId\":1,\"account\":55555}";
            //String body = "{\"codinventario\": \"XQXQ\", \"modelo\": \"CWW\", \"marca\": \"ZWXW\", \"nroserie\": \"IZZII\", \"fechaingreso\": \"2024-12-26\", \"importe\": \"5555\", \"moneda\": \"S/\", \"responsableId\": 2, \"grupoId\": 2, \"tipoId\": 2, \"articuloId\": 2, \"proveedorId\": 2}";
            logger.log("######################################### BODY ################################################");
            logger.log(body);
            logger.log("###############################################################################################");

            //PostgreSQL utiliza el formato yyyy-MM-dd
            //Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            Gson gson = GsonFactory.createGson();

            Activo activo = gson.fromJson(body, Activo.class);

            if (activo == null) {
                return response
                        .withBody("El cuerpo de la solicitud no contiene datos válidos para un activo")
                        .withStatusCode(400);
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(body);

            Long responsableID = null;
            Long tipoID = null;
            Long grupoID = null;
            Long articuloID = null;
            Long proveedorID = null;
            try {
                responsableID = Long.parseLong(jsonNode.get("responsableId").asText());
                tipoID = Long.parseLong(jsonNode.get("tipoId").asText());
                grupoID = Long.parseLong(jsonNode.get("grupoId").asText());
                articuloID = Long.parseLong(jsonNode.get("articuloId").asText());
                proveedorID = Long.parseLong(jsonNode.get("proveedorId").asText());
            } catch (NumberFormatException e) {
                return response
                        .withBody("Invalid id in path")
                        .withStatusCode(400);
            }
            DSLContext dsl = RDSConexion.getDSL();

            Optional<Responsable> responsableSearch = dsl.select()
                    .from(RESPONSABLE_TABLE)
                    .where(DSL.field("id", Long.class).eq(responsableID))
                    .fetchOptionalInto(Responsable.class);
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

            Optional<Grupo> grupoSearch = dsl.select()
                    .from(GRUPO_TABLE)
                    .where(DSL.field("id", Long.class).eq(grupoID))
                    .fetchOptionalInto(Grupo.class);
            if (!grupoSearch.isPresent()) {
                return response
                        .withBody("El grupo especificado no existe")
                        .withStatusCode(404);
            }

            Optional<Articulo> articuloSearch = dsl.select()
                    .from(ARTICULO_TABLE)
                    .where(DSL.field("id", Long.class).eq(articuloID))
                    .fetchOptionalInto(Articulo.class);
            if (!articuloSearch.isPresent()) {
                return response
                        .withBody("El articulo especificado no existe")
                        .withStatusCode(404);
            }

            Optional<Proveedor> proveedorSearch = dsl.select()
                    .from(PROVEEDOR_TABLE)
                    .where(DSL.field("id", Long.class).eq(proveedorID))
                    .fetchOptionalInto(Proveedor.class);
            if (!proveedorSearch.isPresent()) {
                return response
                        .withBody("El proveedor especificado no existe")
                        .withStatusCode(404);
            }

            activo.setResponsable(responsableSearch.get());
            activo.setTipo(tipoSearch.get());
            activo.setGrupo(grupoSearch.get());
            activo.setArticulo(articuloSearch.get());
            activo.setProveedor(proveedorSearch.get());

            logger.log(":::::::::::::::::::::::::::::::::: PREPARANDO PARA INSERTAR ::::::::::::::::::::::::::::::::::");
            save(activo, responsableID, tipoID, grupoID, articuloID, proveedorID);
            logger.log(":::::::::::::::::::::::::::::::::: INSERCIÓN COMPLETA ::::::::::::::::::::::::::::::::::");
            list.add(activo);
            responseRest.getActivoResponse().setListaactivos(list);
            responseRest.setMetadata("Respuesta ok", "00", "Activo guardado");

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