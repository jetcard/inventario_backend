package com.inventarios.handler.especifico.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.handler.especifico.response.EspecificoResponseRest;
import com.inventarios.model.Articulo;
import com.inventarios.model.Especifico;
import java.sql.SQLException;
import java.util.*;
import com.inventarios.model.Responsable;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public abstract class BusquedaPorIdEspecificoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final org.jooq.Table<?> ESPECIFICO_TABLE = DSL.table("especifico");
  protected static final org.jooq.Field<String> ESPECIFICO_TABLE_COLUMNA = DSL.field("nombreespecifico", String.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }
  protected abstract Result<Record> busquedaPorNombreEspecifico(String argv) throws SQLException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    LambdaLogger logger = context.getLogger();
    EspecificoResponseRest responseRest = new EspecificoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    logger.log("buscar: " + idString);
    String output = "";
    try {
      Result<Record> result = busquedaPorNombreEspecifico(idString);
      responseRest.getEspecificoResponse().setListaespecificos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Especificos encontrados");
      output = new Gson().toJson(responseRest);
      return response.withStatusCode(200)
                    .withBody(output);
  } catch (Exception e) {
        responseRest.setMetadata("Respuesta nok", "-1", "Error al guardar");
            return response
                    .withBody(e.toString())
        .withStatusCode(500);
        }
  }

  protected List<Especifico> convertResultToList(Result<Record> result) {
    List<Especifico> listaEspecificos = new ArrayList<>();
    for (Record record : result) {
      Especifico especifico = new Especifico();
      especifico.setId(record.getValue("id", Long.class));
      especifico.setResponsable(record.getValue("responsableId", Responsable.class));
      especifico.setArticulo(record.getValue("articuloId", Articulo.class));
      especifico.setEspecificos(record.getValue("descripespecifico", List.class));
      listaEspecificos.add(especifico);
    }
    return listaEspecificos;
  }
}



    /*
    String body = input.getBody();
    context.getLogger().log("body " + body);
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Especifico especifico = new Gson().fromJson(body, Especifico.class);
        if (especifico != null) {
          context.getLogger().log("especifico.getId() = " + especifico.getId());
          if (id.equals(especifico.getId())) {
            busquedaPorId(especifico.getId());
            list.add(especifico);
            responseRest.getEspecificoResponse().setListaespecificos(list);
            responseRest.setMetadata("Respuesta ok", "00", "Especifico actualizado");
          } else {
            return response
                    .withBody("Id no coincide con el id del body")
                    .withStatusCode(400);
          }
        }
        output = new Gson().toJson(responseRest);
      }
      return response.withStatusCode(200)
              .withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al eliminar");
      return response
        .withBody(e.toString())
        .withStatusCode(500);
    }*/

  /*private String convertResultToJson(Result<Record> result) {
    List<Map<String, Object>> resultList = new ArrayList<>();
    for (Record record : result) {
      Map<String, Object> recordMap = new HashMap<>();
      for (Field<?> field : result.fields()) {
        recordMap.put(field.getName(), record.get(field));
      }
      resultList.add(recordMap);
    }
    return new Gson().toJson(resultList);
  }*/


