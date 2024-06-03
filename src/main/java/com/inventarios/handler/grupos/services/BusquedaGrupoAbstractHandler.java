package com.inventarios.handler.grupos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.handler.grupos.response.GrupoResponseRest;
import com.inventarios.model.Grupo;
import java.sql.SQLException;
import java.util.*;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public abstract class BusquedaGrupoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final org.jooq.Table<?> GRUPO_TABLE = DSL.table("grupo");
  protected static final org.jooq.Field<String> GRUPO_TABLE_COLUMNA = DSL.field("nombregrupo", String.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }
  protected abstract Result<Record> busquedaPorNombreGrupo(String argv) throws SQLException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    LambdaLogger logger = context.getLogger();
    GrupoResponseRest responseRest = new GrupoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    logger.log("buscar: " + idString);
    String output = "";
    try {
      Result<Record> result = busquedaPorNombreGrupo(idString);
      responseRest.getGrupoResponse().setListagrupos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Grupos encontrados");
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

  protected List<Grupo> convertResultToList(Result<Record> result) {
    List<Grupo> listaGrupos = new ArrayList<>();
    for (Record record : result) {
      Grupo grupo = new Grupo();
      grupo.setId(record.getValue("id", Long.class));
      grupo.setNombregrupo(record.getValue("nombregrupo", String.class));
      grupo.setDescripgrupo(record.getValue("descripgrupo", String.class));
      listaGrupos.add(grupo);
    }
    return listaGrupos;
  }
}
