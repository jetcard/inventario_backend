package com.inventarios.handler.especificos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.*;
import com.inventarios.handler.especificos.response.EspecificosResponseRest;
import com.inventarios.model.Especificos;
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

public abstract class ReadEspecificosAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> ESPECIFICOS_TABLE = DSL.table("especificos");

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected abstract Result<Record> read() throws SQLException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    //input.setHeaders(headers);
    EspecificosResponseRest responseRest = new EspecificosResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    String output ="";
    try {
      Result<Record> result = read();
      responseRest.getEspecificosResponse().setListaespecificoss(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Especificoss encontrados");
      output = new Gson().toJson(responseRest);
      return response.withStatusCode(200)
              .withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al consultar");
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
      especificos.setEspecificoid(record.getValue("especificoid", Long.class));
      especificos.setNombreespecifico(record.getValue("nombreespecifico", String.class));
      listaEspecificoss.add(especificos);
    }
    return listaEspecificoss;
  }

}
