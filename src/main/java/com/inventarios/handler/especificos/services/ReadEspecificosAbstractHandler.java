package com.inventarios.handler.especificos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import java.sql.SQLException;
import java.util.*;
import com.google.gson.Gson;
import com.inventarios.handler.atributos.response.AtributosResponseRest;
import com.inventarios.model.Atributos;
import com.inventarios.util.GsonFactory;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;

public abstract class ReadEspecificosAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> ATRIBUTO_TABLE = DSL.table("atributo");
  protected final static Table<Record> ATRIBUTOS_TABLE = DSL.table("atributos");
  protected final static Field<Long> ATRIBUTOS_ID = field(name("atributos", "id"), Long.class);
  protected final static Field<Long> ATRIBUTOS_ATRIBUTOID = field(name("atributos", "atributoid"), Long.class);
  protected final static Field<String> ATRIBUTOS_NOMBREATRIBUTO = field(name("atributos", "nombreatributo"), String.class);
  protected final static Field<Long> ATRIBUTO_ID = field(name("atributo", "id"), Long.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected abstract Result<Record3<Long, Long, String>> read(long responsableId, long articuloId, long tipoId, long grupoId) throws SQLException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    //input.setHeaders(headers);
    AtributosResponseRest responseRest = new AtributosResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    LambdaLogger logger = context.getLogger();
    // Obtener los parámetros de consulta
    Map<String, String> queryParams = input.getQueryStringParameters();

    // Extraer los valores específicos
    String output = "";
    //comparar con los valores de la tabla especificos
    long responsableId  = queryParams.containsKey("responsableId") ? Long.parseLong(queryParams.get("responsableId")) : 0;
    long articuloId     = queryParams.containsKey("articuloId") ? Long.parseLong(queryParams.get("articuloId")) : 0;
    long tipoId         = queryParams.containsKey("tipoId") ? Long.parseLong(queryParams.get("tipoId")) : 0;
    long grupoId        = queryParams.containsKey("grupoId") ? Long.parseLong(queryParams.get("grupoId")) : 0;

    // Imprimir los valores en los logs (opcional)
    logger.log("responsableId: " + responsableId);
    logger.log("articuloId: " + articuloId);
    logger.log("tipoId: " + tipoId);
    logger.log("grupoId: " + grupoId);

    try {
      Result<Record3<Long, Long, String>> result = read(responsableId, articuloId, tipoId, grupoId);
      responseRest.getAtributosResponse().setListaatributoss(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Especificoss encontrados");
      Gson gson = GsonFactory.createGson();
      output = gson.toJson(responseRest);
      logger.log(output);
      return response.withStatusCode(200)
              .withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al consultar");
      return response
              .withBody(e.toString())
              .withStatusCode(500);
    }
  }

  protected List<Atributos> convertResultToList(Result<Record3<Long, Long, String>> result) {
    List<Atributos> listaAtributos = new ArrayList<>();
    for (Record3<Long, Long, String> record : result) {
      Atributos atributos = new Atributos();
      atributos.setId(record.getValue(ATRIBUTOS_ID));
      atributos.setAtributoid(record.getValue(ATRIBUTOS_ATRIBUTOID));
      atributos.setNombreatributo(record.getValue(ATRIBUTOS_NOMBREATRIBUTO));
      listaAtributos.add(atributos);
    }
    return listaAtributos;
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
