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
import com.inventarios.util.GsonFactory;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

public abstract class ReadComunAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> COMUN_TABLE = DSL.table("comun");
  protected final static Table<Record> RESPONSABLE_TABLE = DSL.table("custodio");
  protected static final Field<String> RESPONSABLE_TABLE_COLUMNA = DSL.field("arearesponsable", String.class);
  protected final static Table<Record> TIPO_TABLE = DSL.table("tipo");
  protected static final Field<String> TIPO_TABLE_COLUMNA = DSL.field("nombretipo", String.class);
  protected final static Table<Record> GRUPO_TABLE = DSL.table("categoria");
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
  protected abstract String mostrarCustodio(Long id) throws SQLException;
  protected abstract String mostrarTipoBien(Long id) throws SQLException;
  protected abstract String mostrarCategoria(Long id) throws SQLException;

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
      output = GsonFactory.createGson().toJson(responseRest);
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
      //comun.setDescripcortacomun(record.getValue("descripcortacomun", String.class));

      //comun.setGrupo(record.getValue("categoriaId", Grupo.class));
      ///comun.setPicture(record.getValue("picture", byte[].class));
      Custodio custodio = new Custodio();
      custodio.setId(record.getValue("custodioid", Long.class));
      custodio.setArearesponsable(mostrarCustodio(custodio.getId()));
      comun.setResponsable(custodio);

      Tipo tipo = new Tipo();
      tipo.setId(record.getValue("tipoid", Long.class));
      tipo.setNombretipo(mostrarTipoBien(tipo.getId()));
      comun.setTipo(tipo);

      Categoria categoria =new Categoria();
      categoria.setId(record.getValue("categoriaid", Long.class));
      categoria.setNombregrupo(mostrarCategoria(categoria.getId()));
      comun.setGrupo(categoria);

      listaComunes.add(comun);
    }
    return listaComunes;
  }

}
