package com.inventarios.handler.atributo.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.inventarios.handler.atributo.response.AtributoResponseRest;
import com.inventarios.model.*;
import java.sql.SQLException;
import java.util.*;
import com.inventarios.util.GsonFactory;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class CreateAtributoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    protected final static Table<Record> ATRIBUTO_TABLE = DSL.table("atributo");
    protected final static Table<Record> ATRIBUTOS_TABLE = DSL.table("atributos");
    final static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
        headers.put("Access-Control-Allow-Methods", "POST");
    }
    protected abstract int getAtributoID(Atributo atributo) throws SQLException;
    protected abstract void save(Atributo atributo, List<Atributos> atributosList) throws SQLException;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        //input.setHeaders(headers);
        AtributoResponseRest responseRest = new AtributoResponseRest();
        LambdaLogger logger = context.getLogger();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        List<Atributo> list = new ArrayList<>();
        List<Atributos> atributosList = new ArrayList<>();
        String output ="";
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(input.getBody());
            String body = input.getBody();
            if (body != null && !body.isEmpty()) {
                logger.log(body);
                Atributo atributo = new Gson().fromJson(body, Atributo.class);
                ///Atributo atributo = mapper.readValue(body, Atributo.class);
                if (atributo != null) {
                    List<Atributos> atributosListaInicial = new ArrayList<>();

                    // Initialize default value for atributoid
                    Long defaultAtributoId = 0L;

                    // Create a default Atributos object
                    Atributos defaultAtributo = new Atributos();
                    defaultAtributo.setAtributoid(defaultAtributoId);
                    defaultAtributo.setNombreatributo(""); // You can set other properties if necessary

                    // Add the default Atributos object to atributosList
                    atributosListaInicial.add(defaultAtributo);

                    // Set the atributosList to the atributos property of atributo
                    atributo.setAtributos(atributosListaInicial);

                    logger.log("atributo = "+atributo);
                    Long custodioId = jsonNode.get("custodioId").asLong();
                    logger.log("custodioId = "+custodioId);
                    Long articuloId = jsonNode.get("articuloId").asLong();
                    logger.log("articuloId = "+articuloId);
                    Long tipoId = jsonNode.get("tipoId").asLong();
                    logger.log("tipoId = "+tipoId);
                    Long categoriaId = jsonNode.get("categoriaId").asLong();
                    logger.log("categoriaId = "+categoriaId);

                    Custodio custodio =new Custodio();
                    custodio.setId(custodioId);

                    Articulo articulo=new Articulo();
                    articulo.setId(articuloId);

                    Tipo tipo=new Tipo();
                    tipo.setId(tipoId);

                    Categoria categoria =new Categoria();
                    categoria.setId(categoriaId);

                    atributo.setCustodio(custodio);
                    atributo.setArticulo(articulo);
                    atributo.setTipo(tipo);
                    atributo.setCategoria(categoria);

                    atributo.getCustodio().setId(custodioId);
                    atributo.getArticulo().setId(articuloId);
                    atributo.getTipo().setId(tipoId);
                    atributo.getCategoria().setId(categoriaId);

                    long atributoID = getAtributoID(atributo);
                    atributo.setId(atributoID);

                    JsonNode atributosNode = jsonNode.get("atributos");
                    if (atributosNode != null && atributosNode.isArray()) {
                        for (JsonNode atributoNode : atributosNode) {
                            Atributos atributos = new Atributos();
                            atributos.setAtributoid(atributo.getId());
                            atributos.setNombreatributo(atributoNode.get("nombreatributo")!=null?atributoNode.get("nombreatributo").asText():"");
                            atributosList.add(atributos);
                        }
                    }
                    atributo.setAtributos(atributosList);
                    logger.log(":::::::::::::::::::::::::::::::::: PREPARANDO PARA INSERTAR ::::::::::::::::::::::::::::::::::");
                    save(atributo, atributosList);
                    logger.log(":::::::::::::::::::::::::::::::::: INSERCIÓN COMPLETA ::::::::::::::::::::::::::::::::::");
                    list.add(atributo);
                    responseRest.getAtributoResponse().setListaatributos(list);
                    responseRest.setMetadata("Respuesta ok", "00", "Atributo guardado");
                }
                //output = mapper.writeValueAsString(responseRest);
                //output = new Gson().toJson(responseRest);
                output = GsonFactory.createGson().toJson(responseRest);
                logger.log(output);
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