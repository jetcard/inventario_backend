package com.inventarios.handler.activo.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.handler.activo.response.ActivoResponseRest;
import com.inventarios.model.Articulo;
import com.inventarios.model.Activo;
import java.sql.SQLException;
import java.util.*;
import com.inventarios.model.Custodio;
import com.inventarios.util.GsonFactory;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
public abstract class BusquedaPorIdActivoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final org.jooq.Table<?> ACTIVO_TABLE = DSL.table("activo");
  protected static final org.jooq.Field<String> ACTIVO_TABLE_COLUMNA = DSL.field("nombreespecifico", String.class);

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
    ActivoResponseRest responseRest = new ActivoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    logger.log("buscar: " + idString);
    String output = "";
    try {
      Result<Record> result = busquedaPorNombreEspecifico(idString);
      responseRest.getActivoResponse().setListaactivos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Activos encontrados");
      output = GsonFactory.createGson().toJson(responseRest);
      return response.withStatusCode(200)
                    .withBody(output);
  } catch (Exception e) {
        responseRest.setMetadata("Respuesta nok", "-1", "Error al guardar");
            return response
                    .withBody(e.toString())
        .withStatusCode(500);
        }
  }

  protected List<Activo> convertResultToList(Result<Record> result) {
    List<Activo> listaActivos = new ArrayList<>();
    for (Record record : result) {
      Activo activo = new Activo();
      activo.setId(record.getValue("id", Long.class));
      activo.setResponsable(record.getValue("custodioId", Custodio.class));
      activo.setArticulo(record.getValue("articuloId", Articulo.class));
      ///especifico.setespecificaciones(record.getValue("descripespecifico", List.class));
      listaActivos.add(activo);
    }
    return listaActivos;
  }
}



    /*
    String body = input.getBody();
    context.getLogger().log("body " + body);
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Especifico especifico = GsonFactory.createGson().fromJson(body, Especifico.class);
        if (especifico != null) {
          context.getLogger().log("especifico.getId() = " + especifico.getId());
          if (id.equals(especifico.getId())) {
            busquedaPorId(especifico.getId());
            list.add(especifico);
            responseRest.getEspecificoResponse().setListaespecificaciones(list);
            responseRest.setMetadata("Respuesta ok", "00", "Especifico actualizado");
          } else {
            return response
                    .withBody("Id no coincide con el id del body")
                    .withStatusCode(400);
          }
        }
        output = GsonFactory.createGson().toJson(responseRest);
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


