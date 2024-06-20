package com.inventarios.handler.especificos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import java.sql.SQLException;
import java.util.*;

import com.google.gson.Gson;
import com.inventarios.handler.especificos.response.EspecificosResponseRest;
import com.inventarios.model.Especificos;
import com.inventarios.util.GsonFactory;
import org.jooq.Record3;
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

public abstract class ReadEspecificosAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> ATRIBUTO_TABLE = DSL.table("atributo");
  protected final static Table<Record> ATRIBUTOS_TABLE = DSL.table("atributos");

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected abstract Result<Record3> read(long responsableId, long articuloId, long tipoId, long grupoId) throws SQLException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    //input.setHeaders(headers);
    EspecificosResponseRest responseRest = new EspecificosResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    LambdaLogger logger = context.getLogger();
    // Obtener los parámetros de consulta
    Map<String, String> queryParams = input.getQueryStringParameters();

    // Extraer los valores específicos
    /*String responsableId  = "1";//queryParams.get("responsableId");
    String articuloId     = "3";//queryParams.get("articuloId");
    String tipoId         = "1";//queryParams.get("tipoId");
    String grupoId        = "2";//queryParams.get("grupoId");

    // Imprimir los valores en los logs (opcional)
    logger.log("responsableId: " + responsableId);
    logger.log("articuloId: " + articuloId);
    logger.log("tipoId: " + tipoId);
    logger.log("grupoId: " + grupoId);*/
    String output = "";
    //comparar con los valores de la tabla especificos
    // Extraer y convertir los valores específicos
    long responsableId  = 1L;//queryParams.containsKey("responsableId") ? Long.parseLong(queryParams.get("responsableId")) : 0;
    long articuloId     = 2L;//queryParams.containsKey("articuloId") ? Long.parseLong(queryParams.get("articuloId")) : 0;
    long tipoId         = 1L;//queryParams.containsKey("tipoId") ? Long.parseLong(queryParams.get("tipoId")) : 0;
    long grupoId        = 2L;//queryParams.containsKey("grupoId") ? Long.parseLong(queryParams.get("grupoId")) : 0;

    // Imprimir los valores en los logs (opcional)
    logger.log("responsableId: " + responsableId);
    logger.log("articuloId: " + articuloId);
    logger.log("tipoId: " + tipoId);
    logger.log("grupoId: " + grupoId);

    try {
      Result<Record3> result = read(responsableId, articuloId, tipoId, grupoId);
      responseRest.getEspecificosResponse().setListaespecificoss(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Especificoss encontrados");
      Gson gson = GsonFactory.createGson();
      output = gson.toJson(responseRest);
      return response.withStatusCode(200)
              .withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al consultar");
      return response
              .withBody(e.toString())
              .withStatusCode(500);
    }
  }

  protected List<Especificos> convertResultToList(Result<Record3> result) {
    List<Especificos> listaEspecificoss = new ArrayList<>();
    for (Record record : result) {
      Especificos especificos = new Especificos();
      especificos.setId(record.getValue(ATRIBUTOS_TABLE.field("id"), Long.class));
      especificos.setEspecificoid(record.getValue(ATRIBUTOS_TABLE.field("atributoid"), Long.class));
      especificos.setNombreespecifico(record.getValue(ATRIBUTOS_TABLE.field("nombreatributo"), String.class));
      listaEspecificoss.add(especificos);
    }
    return listaEspecificoss;
  }

/*
  protected List<Especificos> convertResultToList(Result<Record> result) {
    List<Especificos> listaEspecificoss = new ArrayList<>();
    for (Record record : result) {
      Especificos especificos = new Especificos();
      especificos.setId(record.getValue(ESPECIFICOS_TABLE.field("id"), Long.class));
      especificos.setEspecificoid(record.getValue(ESPECIFICOS_TABLE.field("especificoid"), Long.class));
      especificos.setNombreespecifico(record.getValue(ESPECIFICOS_TABLE.field("nombreespecifico"), String.class));
      listaEspecificoss.add(especificos);
    }
    return listaEspecificoss;
  }*/

  /*
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
*/
}
