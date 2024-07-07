package com.inventarios.handler.marca.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.sql.SQLException;
import java.util.*;

import com.inventarios.handler.marca.response.MarcaResponseRest;
import com.inventarios.model.*;
import com.inventarios.util.GsonFactory;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

public abstract class ReadMarcaAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> MARCA_TABLE = DSL.table("marca");
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
    MarcaResponseRest responseRest = new MarcaResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    String output ="";
    try {
      Result<Record> result = read();
      responseRest.getMarcaResponse().setListamarcaes(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Marcaes listados");
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

  protected List<Marca> convertResultToList(Result<Record> result) throws SQLException {
    List<Marca> listaMarcaes = new ArrayList<>();
    for (Record record : result) {
      Marca marca = new Marca();
      marca.setId(record.getValue("id", Long.class));
      ///marca.setDescripmarca(record.getValue("descripmarca", String.class));
      //marca.setDescripcortamarca(record.getValue("descripcortamarca", String.class));

      //marca.setGrupo(record.getValue("categoriaId", Grupo.class));
      ///marca.setPicture(record.getValue("picture", byte[].class));
      Custodio custodio = new Custodio();
      custodio.setId(record.getValue("custodioid", Long.class));
      custodio.setArearesponsable(mostrarCustodio(custodio.getId()));
      ///marca.setResponsable(custodio);

      Tipo tipo = new Tipo();
      tipo.setId(record.getValue("tipoid", Long.class));
      tipo.setNombretipo(mostrarTipoBien(tipo.getId()));
      ///marca.setTipo(tipo);

      Categoria categoria =new Categoria();
      categoria.setId(record.getValue("categoriaid", Long.class));
      categoria.setNombregrupo(mostrarCategoria(categoria.getId()));
      ///marca.setGrupo(categoria);

      listaMarcaes.add(marca);
    }
    return listaMarcaes;
  }

}
