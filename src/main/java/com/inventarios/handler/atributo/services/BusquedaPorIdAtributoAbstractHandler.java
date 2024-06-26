package com.inventarios.handler.atributo.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.handler.atributo.response.AtributoResponseRest;
import com.inventarios.model.*;
import java.sql.SQLException;
import java.util.*;
import com.inventarios.util.GsonFactory;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.DSL.name;

public abstract class BusquedaPorIdAtributoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final org.jooq.Table<?> ATRIBUTO_TABLE = DSL.table("atributo");
  //protected static final org.jooq.Field<Long> ATRIBUTO_TABLE_COLUMNA = DSL.field("articuloId", Long.class);

  //protected final static Table<Record> ATRIBUTO_TABLE = table("atributo");
  //protected final static Table<Record> ATRIBUTOS_TABLE = table("atributos");
  protected final static Field<Long> ATRIBUTO_ID = field(name("atributo", "id"), Long.class);
  protected final static Field<Long> ATRIBUTO_RESPONSABLE_ID = field(name("atributo", "responsableid"), Long.class);
  protected final static Field<Long> ATRIBUTO_ARTICULO_ID = field(name("atributo", "articuloid"), Long.class);
  //protected final static Field<Long> ATRIBUTOS_ID = field(name("atributos", "id"), Long.class);
  //protected final static Field<Long> ATRIBUTOS_ATRIBUTOID = field(name("atributos", "atributoid"), Long.class);
  //protected final static Field<String> ATRIBUTOS_NOMBREATRIBUTO = field(name("atributos", "nombreatributo"), String.class);

  protected final static Table<Record> RESPONSABLE_TABLE = DSL.table("responsable");
  protected static final Field<String> RESPONSABLE_TABLE_COLUMNA = DSL.field("arearesponsable", String.class);
  protected final static Table<Record> ARTICULO_TABLE = table("articulo");
  protected static final Field<String> ARTICULO_TABLE_COLUMNA = DSL.field("nombrearticulo", String.class);
  protected final static Table<Record> TIPO_TABLE = DSL.table("tipo");
  protected static final Field<String> TIPO_TABLE_COLUMNA = DSL.field("nombretipo", String.class);
  protected final static Table<Record> GRUPO_TABLE = DSL.table("grupo");
  protected static final Field<String> GRUPO_TABLE_COLUMNA = DSL.field("nombregrupo", String.class);

  protected final static Field<Long> ATRIBUTO_TIPO_ID = field(name("atributo", "tipoid"), Long.class);
  protected final static Field<Long> ATRIBUTO_GRUPO_ID = field(name("atributo", "grupoid"), Long.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected abstract Result<Record5<Long, Long, Long, Long, Long>> busquedaPorArticuloId(String articuloId) throws SQLException;
  protected abstract String mostrarResponsable(Long id) throws SQLException;
  protected abstract String mostrarArticulo(Long id) throws SQLException;
  protected abstract String mostrarTipoBien(Long id) throws SQLException;
  protected abstract String mostrarGrupo(Long id) throws SQLException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    LambdaLogger logger = context.getLogger();
    AtributoResponseRest responseRest = new AtributoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);

    //String articuloId = input.getPathParameters().get("articuloId");
    //logger.log("articuloId: " + articuloId);

    Map<String, String> pathParameters = input.getPathParameters();
    String articuloId = pathParameters.get("id");
    logger.log("articuloId: " + articuloId);

    try {
      Result<Record5<Long, Long, Long, Long, Long>> result = busquedaPorArticuloId(articuloId);
      responseRest.getAtributoResponse().setListaatributos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Atributos encontrados");
      Gson gson = GsonFactory.createGson();
      String output = gson.toJson(responseRest);
      logger.log(output);
      return response.withStatusCode(200).withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al consultar");
      return response.withBody(e.toString()).withStatusCode(500);
    }
  }

  protected List<Atributo> convertResultToList(Result<Record5<Long, Long, Long, Long, Long>> result) throws SQLException {
    List<Atributo> listaAtributos = new ArrayList<>();
    for (Record record : result) {
      Atributo atributo = new Atributo();
      atributo.setId(record.getValue("id", Long.class));

      Responsable responsable = new Responsable();
      responsable.setId(record.get(ATRIBUTO_RESPONSABLE_ID));
      responsable.setArearesponsable(mostrarResponsable(responsable.getId()));
      atributo.setResponsable(responsable);

      Articulo articulo = new Articulo();
      //System.out.println(record.get(ATRIBUTO_ARTICULO_ID)!=null?record.get(ATRIBUTO_ARTICULO_ID):"nulo");//33
      articulo.setId(record.get(ATRIBUTO_ARTICULO_ID));
      articulo.setNombrearticulo(mostrarArticulo(articulo.getId()));
      atributo.setArticulo(articulo);

      Tipo tipo = new Tipo();
      tipo.setId(record.get(ATRIBUTO_TIPO_ID));
      tipo.setNombretipo(mostrarTipoBien(tipo.getId()));
      atributo.setTipo(tipo);

      Grupo grupo=new Grupo();
      grupo.setId(record.get(ATRIBUTO_GRUPO_ID));
      grupo.setNombregrupo(mostrarGrupo(grupo.getId()));
      atributo.setGrupo(grupo);
      listaAtributos.add(atributo);
    }
    return listaAtributos;
  }












  /*
  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    LambdaLogger logger = context.getLogger();
    AtributoResponseRest responseRest = new AtributoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    String articuloId = input.getPathParameters().get("articuloId");


    logger.log("articuloId: " + articuloId);

    try {
      Result<Record> result = busquedaPorArticuloId(String articuloId);
      responseRest.getAtributoResponse().setListaatributos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Atributos encontrados");
      Gson gson = GsonFactory.createGson();
      String output = gson.toJson(responseRest);
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

  protected List<Atributo> convertResultToList(Result<Record> result) {
    List<Atributo> listaAtributos = new ArrayList<>();
    for (Record record : result) {
      Atributo atributo = new Atributo();
      atributo.setId(record.getValue("id", Long.class));
      atributo.setResponsable(record.getValue("responsableId", Responsable.class));
      atributo.setTipo(record.getValue("tipoId", Tipo.class));
      atributo.setGrupo(record.getValue("grupoId", Grupo.class));
      atributo.setArticulo(record.getValue("articuloId", Articulo.class));
      //atributo.setAtributos(record.getValue("descripatributo", List.class));
      listaAtributos.add(atributo);
    }
    return listaAtributos;
  }*/
}



    /*
    String body = input.getBody();
    context.getLogger().log("body " + body);
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Atributo atributo = new Gson().fromJson(body, Atributo.class);
        if (atributo != null) {
          context.getLogger().log("atributo.getId() = " + atributo.getId());
          if (id.equals(atributo.getId())) {
            busquedaPorId(atributo.getId());
            list.add(atributo);
            responseRest.getAtributoResponse().setListaatributos(list);
            responseRest.setMetadata("Respuesta ok", "00", "Atributo actualizado");
          } else {
            return response
                    .withBody("Id no coincide con el id del body")
                    .withStatusCode(400);
          }
        }
        output = new Gson().toJson(responseRest);
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


