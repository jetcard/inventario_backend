package com.inventarios.handler.comunes.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.*;

import com.inventarios.handler.comunes.response.ComunResponseRest;
import com.inventarios.model.*;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

public abstract class ReadComunAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> COMUN_TABLE = DSL.table("comun");
  protected final static Table<Record> RESPONSABLE_TABLE = DSL.table("responsable");
  protected static final Field<String> RESPONSABLE_TABLE_COLUMNA = DSL.field("arearesponsable", String.class);
  protected final static Table<Record> TIPO_TABLE = DSL.table("tipo");
  protected static final Field<String> TIPO_TABLE_COLUMNA = DSL.field("nombretipo", String.class);
  protected final static Table<Record> GRUPO_TABLE = DSL.table("grupo");
  protected static final Field<String> GRUPO_TABLE_COLUMNA = DSL.field("nombregrupo", String.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected abstract Result<Record> read() throws SQLException;
  protected abstract String mostrarResponsable(Long id) throws SQLException;
  protected abstract String mostrarTipoBien(Long id) throws SQLException;
  protected abstract String mostrarGrupo(Long id) throws SQLException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    ComunResponseRest responseRest = new ComunResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    String output ="";
    try {
      Result<Record> result = read();
      responseRest.getComunResponse().setListacomunes(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Comunes listados");
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

  /*private String convertResultToJson(Result<Record> result) {
    List<Map<String, Object>> resultList = new ArrayList<>();
    for (Record record : result) {
      Map<String, Object> recordMap = new LinkedHashMap<>();
      for (Field<?> field : result.fields()) {
        recordMap.put(field.getName(), record.get(field));
      }
      System.out.println("recordMap: "+recordMap);
      resultList.add(recordMap);
    }
    System.out.println("resultList: "+resultList);
    return new Gson().toJson(resultList);
  }*/

  protected List<Comun> convertResultToList(Result<Record> result) throws SQLException {
    List<Comun> listaComunes = new ArrayList<>();
    for (Record record : result) {
      Comun comun = new Comun();
      comun.setId(record.getValue("id", Long.class));
      comun.setDescripcomun(record.getValue("descripcomun", String.class));
      comun.setDescripcortacomun(record.getValue("descripcortacomun", String.class));

      //comun.setGrupo(record.getValue("grupoId", Grupo.class));
      ///comun.setPicture(record.getValue("picture", byte[].class));
      Responsable responsable = new Responsable();
      responsable.setId(record.getValue("responsableid", Long.class));
      responsable.setArearesponsable(mostrarResponsable(responsable.getId()));
      comun.setResponsable(responsable);

      Tipo tipo = new Tipo();
      tipo.setId(record.getValue("tipoid", Long.class));
      tipo.setNombretipo(mostrarTipoBien(tipo.getId()));
      comun.setTipo(tipo);

      Grupo grupo=new Grupo();
      grupo.setId(record.getValue("grupoid", Long.class));
      grupo.setNombregrupo(mostrarGrupo(grupo.getId()));
      comun.setGrupo(grupo);

      listaComunes.add(comun);
    }
    return listaComunes;
  }

}
