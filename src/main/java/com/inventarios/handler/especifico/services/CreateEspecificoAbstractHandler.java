package com.inventarios.handler.especifico.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.inventarios.handler.especifico.response.EspecificoResponseRest;
import com.inventarios.model.*;
import java.sql.SQLException;
import java.util.*;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class CreateEspecificoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    protected final static Table<Record> ESPECIFICO_TABLE = DSL.table("especifico");
    protected final static Table<Record> ESPECIFICOS_TABLE = DSL.table("especificos");
    final static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
        headers.put("Access-Control-Allow-Methods", "POST");
    }
    protected abstract int getEspecificoID(Especifico especifico) throws SQLException;
    protected abstract void save(Especifico especifico, List<Especificos> especificosList) throws SQLException;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        //input.setHeaders(headers);
        EspecificoResponseRest responseRest = new EspecificoResponseRest();
        LambdaLogger logger = context.getLogger();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        List<Especifico> list = new ArrayList<>();
        List<Especificos> especificosList = new ArrayList<>();
        String output ="";
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(input.getBody());
            String body = input.getBody();
            if (body != null && !body.isEmpty()) {
                logger.log(body);
                Especifico especifico = new Gson().fromJson(body, Especifico.class);
                ///Especifico especifico = mapper.readValue(body, Especifico.class);
                if (especifico != null) {
                    List<Especificos> especificosListaInicial = new ArrayList<>();

                    // Initialize default value for especificoid
                    Long defaultEspecificoId = 0L;

                    // Create a default Especificos object
                    Especificos defaultEspecifico = new Especificos();
                    defaultEspecifico.setEspecificoid(defaultEspecificoId);
                    defaultEspecifico.setNombreespecifico(""); // You can set other properties if necessary

                    // Add the default Especificos object to especificosList
                    especificosListaInicial.add(defaultEspecifico);

                    // Set the especificosList to the especificos property of especifico
                    especifico.setEspecificos(especificosListaInicial);

                    logger.log("especifico = "+especifico);
                    Long responsableId = jsonNode.get("responsableId").asLong();
                    logger.log("responsableId = "+responsableId);
                    Long articuloId = jsonNode.get("articuloId").asLong();
                    logger.log("articuloId = "+articuloId);

                    Responsable responsable=new Responsable();
                    responsable.setId(responsableId);

                    Articulo articulo=new Articulo();
                    articulo.setId(articuloId);

                    especifico.setResponsable(responsable);
                    especifico.setArticulo(articulo);

                    especifico.getResponsable().setId(responsableId);
                    especifico.getArticulo().setId(articuloId);

                    long especificoID = getEspecificoID(especifico);
                    especifico.setId(especificoID);

                    JsonNode especificosNode = jsonNode.get("especificos");
                    if (especificosNode != null && especificosNode.isArray()) {
                        for (JsonNode especificoNode : especificosNode) {
                            //especificoNode.get("especificoid").asText();
                            //logger.log("especificoNode.get(especificoid).asText() = " + especificoNode.get("especificoid")!=null?especificoNode.get("especificoid").asText():"");
                            Especificos especificos = new Especificos();
                            especificos.setEspecificoid(especifico.getId());
                            ///especificos.setEspecifico(especifico);
                            //especificos.setNombreespecifico(especificoNode.get("nombreespecifico").asText(""));
                            especificos.setNombreespecifico(especificoNode.get("nombreespecifico")!=null?especificoNode.get("nombreespecifico").asText():"");
                            especificosList.add(especificos);
                        }
                    }
                    especifico.setEspecificos(especificosList);
                    logger.log(":::::::::::::::::::::::::::::::::: PREPARANDO PARA INSERTAR ::::::::::::::::::::::::::::::::::");
                    save(especifico, especificosList);
                    logger.log(":::::::::::::::::::::::::::::::::: INSERCIÓN COMPLETA ::::::::::::::::::::::::::::::::::");
                    list.add(especifico);
                    responseRest.getEspecificoResponse().setListaespecificos(list);
                    responseRest.setMetadata("Respuesta ok", "00", "Especifico guardado");
                }
                //output = mapper.writeValueAsString(responseRest);
                output = new Gson().toJson(responseRest);
            }
            return response.withStatusCode(200)
                    .withBody(output);
        } catch (Exception e) {
            responseRest.setMetadata("Respuesta nok", "-1", "Error al guardar");
            return response
                    .withBody(e.toString())
                    .withStatusCode(500);
        }
    }
}