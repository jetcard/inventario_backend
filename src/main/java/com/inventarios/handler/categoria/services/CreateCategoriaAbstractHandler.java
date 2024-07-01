package com.inventarios.handler.categoria.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.model.Categoria;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import com.inventarios.handler.categoria.response.CategoriaResponseRest;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
public abstract class CreateCategoriaAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> GRUPO_TABLE = DSL.table("categoria");
  final static Map<String, String> headers = new HashMap<>();
  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "POST");
  }
  protected abstract void save(String nombregrupo, String descripgrupo) throws SQLException;
  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    CategoriaResponseRest responseRest = new CategoriaResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers);
    String body = input.getBody();
    //String body = "{ \"nombregrupo\": \"hello world\", \"descripgrupo\": \"As\" }"; //

    context.getLogger().log("body " + body);
    String output ="";
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Categoria categoria = new Gson().fromJson(body, Categoria.class);
        if (categoria != null) {
          save(categoria.getNombregrupo().toUpperCase(), categoria.getDescripgrupo().toUpperCase());
          responseRest.setMetadata("Respuesta ok", "00", "Grupo guardado");
        }
        output = new Gson().toJson(responseRest);
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