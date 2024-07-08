package com.inventarios.handler.parametros.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import java.sql.SQLException;
import java.util.*;

import com.inventarios.handler.parametros.response.ParametroResponseRest;
import com.inventarios.model.Parametro;
import com.inventarios.util.GsonFactory;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class DeleteParametroAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> PARAMETRO_TABLE = DSL.table("parametros");

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "DELETE");
  }

  protected abstract Optional<Parametro> parametroSearch(Long id) throws SQLException;
  protected abstract void delete(long id) throws SQLException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    ParametroResponseRest parametroResponseRest = new ParametroResponseRest();
    List<Parametro> listaParametros = new ArrayList<>();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    context.getLogger().log("Eliminando " + idString);
    Long id = null;
    try {
      id = Long.parseLong(idString);
    } catch (NumberFormatException e) {
      return response
              .withBody("Invalid id in path")
              .withStatusCode(400);
    }
    String body = input.getBody();
    context.getLogger().log("body " + body);
    try {
      context.getLogger().log("body " + body);
      Parametro parametroFromBody = GsonFactory.createGson().fromJson(body, Parametro.class);
      if (parametroFromBody != null) {
        if(parametroSearch(id).isPresent()){
          Parametro parametro = parametroSearch(id).get();
          delete(parametro.getId());
          listaParametros.remove(parametro);
          parametroResponseRest.getParametroResponse().setListaparametros(listaParametros);
          parametroResponseRest.setMetadata("Respuesta ok", "00", "Parámetro eliminado");
        } else {
          parametroResponseRest.setMetadata("Respuesta nok", "-1", "Parámetro no encontrado");
        }
        String output = GsonFactory.createGson().toJson(parametroResponseRest);
        return response.withStatusCode(200).withBody(output);
      } else {
        return response
                .withBody("Invalid group data in request body")
                .withStatusCode(400);
      }
    } catch (Exception e) {
      parametroResponseRest.setMetadata("Respuesta nok", "-1", "Error al actualizar");
      return response
              .withBody(e.toString())
              .withStatusCode(500);
    }
  }

 /*

    ParametroResponseRest responseRest = new ParametroResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    context.getLogger().log("...eliminando: " + idString);

    Long id = null;
    try {
      id = Long.parseLong(idString);
    } catch (NumberFormatException e) {
      return response
              .withBody("Invalid id in path")
              .withStatusCode(400);
    }
    try {
      delete(id);
      responseRest.setMetadata("Respuesta ok", "00", "Parámetro eliminado");
      String output = GsonFactory.createGson().toJson(responseRest);
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
*/
}

