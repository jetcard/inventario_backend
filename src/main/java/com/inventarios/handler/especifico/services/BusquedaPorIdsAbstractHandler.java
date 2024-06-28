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
import com.inventarios.model.EspecificosFiltro;
import com.inventarios.model.Responsable;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BusquedaPorIdsAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final Table<?> ESPECIFICO_TABLE = DSL.table("especifico");
  protected static final Field<String> ESPECIFICO_TABLE_COLUMNA = DSL.field("nombreespecifico", String.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }
  protected abstract Result<Record> filtraPorIds(EspecificosFiltro filter) throws SQLException;

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
    EspecificosFiltro filtro=new EspecificosFiltro();
    try {
      Result<Record> result = filtraPorIds(filtro);
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
      ///especifico.setEspecificos(record.getValue("descripespecifico", List.class));
      listaEspecificos.add(especifico);
    }
    return listaEspecificos;
  }

}