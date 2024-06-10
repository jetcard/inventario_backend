package com.inventarios.handler.especificos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.handler.especificos.response.EspecificosResponseRest;
import com.inventarios.model.Especificos;
import java.sql.SQLException;
import java.util.*;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public abstract class BusquedaPorIdEspecificosAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final org.jooq.Table<?> ESPECIFICOS_TABLE = DSL.table("especificos");
  protected static final org.jooq.Field<String> ESPECIFICOS_TABLE_COLUMNA = DSL.field("nombreespecificos", String.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }
  protected abstract Result<Record> busquedaPorNombreEspecificos(String argv) throws SQLException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    EspecificosResponseRest responseRest = new EspecificosResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    context.getLogger().log("id from path: " + idString);
    String output = "";
    try {
      Result<Record> result = busquedaPorNombreEspecificos(idString);
      responseRest.getEspecificosResponse().setListaespecificoss(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Especificoss encontrados");
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

  protected List<Especificos> convertResultToList(Result<Record> result) {
    List<Especificos> listaEspecificoss = new ArrayList<>();
    for (Record record : result) {
      Especificos especificos = new Especificos();
      especificos.setId(record.getValue("id", Long.class));
      //especificos.setEspecifico(record.getValue("nombreespecifico", Especifico.class));
      especificos.setNombreespecifico(record.getValue("nombreespecifico", String.class));
      listaEspecificoss.add(especificos);
    }
    return listaEspecificoss;
  }
}



    /*
    String body = input.getBody();
    context.getLogger().log("body " + body);
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Especificos especificos = new Gson().fromJson(body, Especificos.class);
        if (especificos != null) {
          context.getLogger().log("especificos.getId() = " + especificos.getId());
          if (id.equals(especificos.getId())) {
            busquedaPorId(especificos.getId());
            list.add(especificos);
            responseRest.getEspecificosResponse().setListaespecificoss(list);
            responseRest.setMetadata("Respuesta ok", "00", "Especificos actualizado");
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


