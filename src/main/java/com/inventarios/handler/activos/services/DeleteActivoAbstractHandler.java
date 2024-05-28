package com.inventarios.handler.activos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import com.inventarios.handler.activos.response.ActivoResponseRest;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class DeleteActivoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> ACTIVO_TABLE = DSL.table("activo");

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "DELETE");
  }

  protected abstract void delete(long id);

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    ActivoResponseRest responseRest = new ActivoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    context.getLogger().log("Eliminando: " + idString);

    Long id = null;
    String output ="";
    try {
      id = Long.parseLong(idString);
    } catch (NumberFormatException e) {
      return response
              .withBody("Invalid id in path")
              .withStatusCode(400);
    }
    try {
      delete(id);
      responseRest.setMetadata("Respuesta ok", "00", "Articulo eliminado");
      output = new Gson().toJson(responseRest);
      return response
              .withBody(output)
              .withStatusCode(200);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al eliminar");
      return response
              .withBody(e.toString())
              .withStatusCode(500);
    }
  }
  /*public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers)
      .withStatusCode(200);
    String body = input.getBody();
    context.getLogger().log("body " + body);
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Activo activo = new Gson().fromJson(body, Activo.class);
        if (activo != null) {
          //delete(2);
          delete(activo.getId());
        }
      }
      return response;
    } catch (Exception e) {
      return response
        .withBody(e.toString())
        .withStatusCode(500);
    }
  }*/
}

