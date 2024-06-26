package com.inventarios.handler.comunes.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.handler.comunes.response.ComunResponseRest;
import com.inventarios.model.Comun;
import java.sql.SQLException;
import java.util.*;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public abstract class BusquedaPorIdComunAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  final static Map<String, String> headers = new HashMap<>();
  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected static final org.jooq.Table<?> COMUN_TABLE = DSL.table("comun");
  protected static final org.jooq.Field<String> COMUN_TABLE_COLUMNA = DSL.field("nombrecomun", String.class);
  protected abstract Result<Record> busquedaPorNombreComun(String argv) throws SQLException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    LambdaLogger logger = context.getLogger();
    ComunResponseRest responseRest = new ComunResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    logger.log("buscar: " + idString);
    String output = "";
    try {
      Result<Record> result = busquedaPorNombreComun(idString);
      responseRest.getComunResponse().setListacomunes(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Comuns encontrados");
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

  protected List<Comun> convertResultToList(Result<Record> result) {
    List<Comun> listaComuns = new ArrayList<>();
    for (Record record : result) {
      Comun comun = new Comun();
      comun.setId(record.getValue("id", Long.class));
      comun.setDescripcomun(record.getValue("descripcomun", String.class));
      listaComuns.add(comun);
    }
    return listaComuns;
  }
}