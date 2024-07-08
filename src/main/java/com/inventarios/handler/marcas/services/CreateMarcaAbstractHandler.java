package com.inventarios.handler.marcas.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.marcas.response.MarcaResponseRest;
import com.inventarios.model.Marca;

import java.sql.SQLException;
import java.util.*;

import com.inventarios.util.GsonFactory;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class CreateMarcaAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    protected final static Table<Record> MARCA_TABLE = DSL.table("marca");
    final static Map<String, String> headers = new HashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    static {
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
        headers.put("Access-Control-Allow-Methods", "POST");
    }

    protected abstract void save(Marca marca) throws SQLException;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        input.setHeaders(headers);
        LambdaLogger logger = context.getLogger();
        MarcaResponseRest responseRest = new MarcaResponseRest();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
        List<Marca> list = new ArrayList<>();
        String output = "";
        String contentTypeHeader = input.getHeaders().get("Content-Type");
        logger.log("Content-Type: " + contentTypeHeader);
        try {


            String body = input.getBody();
            //String body = "{\"modelo\":\"AS\",\"marca\":\"AS\",\"nroserie\":\"1\",\"fechacompra\":\"1\",\"importe\":777,\"categoriaId\":1,\"account\":55555}";
            logger.log("##################### BODY MARCA ######################");
            logger.log(body);
            logger.log("#######################################################");

            //Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            Marca marca = GsonFactory.createGson().fromJson(body, Marca.class);
            //Marca marca = gson.fromJson(body, Marca.class);

            logger.log("debe llegar aquí 1 ya tenemos Marca");
            if (marca == null) {
                return response
                        .withBody("El cuerpo de la solicitud no contiene datos válidos para un marca")
                        .withStatusCode(400);
            }
            logger.log("debe llegar aquí 2");
            // Obtener el ID del grupo del objeto Marca
            //Long categoriaId = marca.getGrupo().getId();


            logger.log("Marca: ");

            //if (marca != null) {
            logger.log("Marca.getId() = " + marca.getId());
            //logger.log("Marca.getDescripmarca() = " + marca.getDescripmarca());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(body);

            DSLContext dsl = RDSConexion.getDSL();

            logger.log(":::::::::::::::::::::::::::::::::: PREPARANDO PARA INSERTAR ::::::::::::::::::::::::::::::::::");

            save(marca);
            logger.log(":::::::::::::::::::::::::::::::::: INSERCIÓN COMPLETA ::::::::::::::::::::::::::::::::::");
            list.add(marca);
            responseRest.getMarcaResponse().setListamarcas(list);
            responseRest.setMetadata("Respuesta ok", "00", "Común guardado");
            output = GsonFactory.createGson().toJson(responseRest);
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

