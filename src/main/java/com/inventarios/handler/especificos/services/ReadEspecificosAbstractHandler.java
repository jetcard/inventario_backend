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
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

public abstract class ReadEspecificosAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> ESPECIFICO_TABLE = DSL.table("especifico");
  protected final static Table<Record> ESPECIFICOS_TABLE = DSL.table("especificos");

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected abstract Result<Record> read(String responsableId, String articuloId, String tipoId, String grupoId) throws SQLException;

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
    String responsableId  = queryParams.get("responsableId");
    String articuloId     = queryParams.get("articuloId");
    String tipoId         = queryParams.get("tipoId");
    String grupoId        = queryParams.get("grupoId");

    // Imprimir los valores en los logs (opcional)
    logger.log("responsableId: " + responsableId);
    logger.log("articuloId: " + articuloId);
    logger.log("tipoId: " + tipoId);
    logger.log("grupoId: " + grupoId);
    String output = "";
    //comparar con los valores de la tabla especificos
    try {
      Result<Record> result = read(responsableId, articuloId, tipoId, grupoId);
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


    /*
      List<String> atributos = new ArrayList<>();

      if (responsableId == 5 && articuloId == 5 && tipoId == 1 && grupoId == 3) {
        atributos = Arrays.asList("ALTO", "ANCHO", "LARGO");
      } else {
        atributos = Arrays.asList("1", "2");
      }

      return ResponseEntity.ok(atributos);

    List<Especifico> listaEspecificos = new ArrayList<>();

    for (Record record : result) {
      Especifico especifico = new Especifico();
      especifico.setId(record.getValue("id", Long.class));

      Responsable responsable = new Responsable();
      responsable.setId(record.getValue("responsableid", Long.class));
      responsable.setArearesponsable(mostrarResponsable(responsable.getId()));
      especifico.setResponsable(responsable);

      Articulo articulo = new Articulo();
      articulo.setId(record.getValue("articuloid", Long.class));
      articulo.setNombrearticulo(mostrarArticulo(articulo.getId()));
      especifico.setArticulo(articulo);

      listaEspecificos.add(especifico);
    }
    return listaEspecificos;
  }

    // Crear una respuesta (esto es solo un ejemplo)
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
    response.setStatusCode(200);
    response.setBody("Valores obtenidos correctamente");


    logger.log("==================================== handleRequest ===================================== ");
    ///String path = especificos?responsableId=1&articuloId=1&tipoId=1&grupoId=1
    String path = input.getPath();
    logger.log("==================================== path =  "+path);//path = /activos/campo/
    Map<String, String> pathParameters = input.getQueryStringParameters();
    /*String responsableId = pathParameters != null ? pathParameters.get("responsableId") : null;
    logger.log("responsableId: " + responsableId);
    String articuloId = pathParameters != null ? pathParameters.get("articuloId") : null;
    logger.log("articuloId: " + articuloId);
    String tipoId = pathParameters != null ? pathParameters.get("tipoId") : null;
    logger.log("tipoId: " + tipoId);
    String grupoId = pathParameters != null ? pathParameters.get("grupoId") : null;
    logger.log("grupoId: " + grupoId);
    logger.log("==================================== pathParameters =  "+pathParameters);

    */


  }

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
  }

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
