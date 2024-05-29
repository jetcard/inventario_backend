package com.inventarios.handler.activos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.handler.activos.response.ActivoResponseRest;
import com.inventarios.model.*;
import com.inventarios.util.Conversiones;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.impl.DSL;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BusquedaActivoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final Table<Record> ACTIVO_TABLE = DSL.table("activo");
  protected final static Table<Record> RESPONSABLE_TABLE = DSL.table("responsable");
  protected static final Field<String> RESPONSABLE_TABLE_COLUMNA = DSL.field("arearesponsable", String.class);
  protected final static Table<Record> TIPO_TABLE = DSL.table("tipo");
  protected static final Field<String> TIPO_TABLE_COLUMNA = DSL.field("nombretipo", String.class);
  protected final static Table<Record> GRUPO_TABLE = DSL.table("grupo");
  protected static final Field<String> GRUPO_TABLE_COLUMNA = DSL.field("nombregrupo", String.class);
  protected final static Table<Record> ARTICULO_TABLE = DSL.table("articulo");
  protected static final Field<String> ARTICULO_TABLE_COLUMNA = DSL.field("nombrearticulo", String.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected abstract String mostrarResponsable(Long id);
  protected abstract String mostrarTipoBien(Long id);
  protected abstract String mostrarGrupo(Long id);
  protected abstract String mostrarArticulo(Long id);
  protected abstract Result<Record> busquedaActivo(String responsable, String codinventario, String modelo, String marca, String nroSerie, LocalDate fechaCompraDesde, LocalDate fechaCompraHasta);

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    LambdaLogger logger = context.getLogger();
    ActivoResponseRest responseRest = new ActivoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    // Obtener los parámetros de consulta
    // Obtener los parámetros de consulta
    Map<String, String> queryParameters = input.getQueryStringParameters();
    String responsable = queryParameters != null ? queryParameters.get("responsable") : null;
    logger.log("responsable: " + responsable);

    String codinventario = queryParameters != null ? queryParameters.get("codinventario") : null;
    logger.log("codinventario: " + codinventario);
    String modelo = queryParameters != null ? queryParameters.get("modelo") : null;
    logger.log("modelo: " + modelo);
    String marca = queryParameters != null ? queryParameters.get("marca") : null;
    logger.log("marca: " + marca);
    String nroSerie = queryParameters != null ? queryParameters.get("nroserie") : null;
    logger.log("nroserie: " + nroSerie);
    String fechaCompraDesde = queryParameters != null ? queryParameters.get("fechadesde") : null;
    logger.log("fechadesde: " + fechaCompraDesde);
    String fechaCompraHasta = queryParameters != null ? queryParameters.get("fechahasta") : null;
    logger.log("fechahasta: " + fechaCompraHasta);
    String output = "";
    LocalDate fechaDesde = new Conversiones().convertirALocalDate(fechaCompraDesde);
    LocalDate fechaHasta = new Conversiones().convertirALocalDate(fechaCompraHasta);
    try {
      Result<Record> result = busquedaActivo(responsable, codinventario, modelo, marca, nroSerie, fechaDesde, fechaHasta);
      responseRest.getActivoResponse().setListaactivos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Activos encontrados");
      output = new Gson().toJson(responseRest);
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
      activo.setCodinventario(record.getValue("codinventario", String.class));
      activo.setModelo(record.getValue("modelo", String.class));
      activo.setMarca(record.getValue("marca", String.class));
      activo.setNroserie(record.getValue("nroserie", String.class));
      activo.setFechaingreso(record.getValue("fechaingreso", Date.class));
      activo.setMoneda(record.getValue( "nroserie", String.class));
      activo.setImporte(record.getValue("importe", BigDecimal.class));

      Responsable responsable = new Responsable();
      responsable.setId(record.getValue("responsableid", Long.class));
      responsable.setNombreusuario(record.getValue(RESPONSABLE_TABLE.field("nombreusuario"), String.class));
      responsable.setArearesponsable(record.getValue(RESPONSABLE_TABLE.field("arearesponsable"), String.class));
      activo.setResponsable(responsable);

      Tipo tipo = new Tipo();
      tipo.setId(record.getValue("tipoid", Long.class));
      tipo.setNombretipo(mostrarTipoBien(tipo.getId()));
      activo.setTipo(tipo);
      Grupo grupo=new Grupo();
      grupo.setId(record.getValue("grupoid", Long.class));
      grupo.setNombregrupo(mostrarGrupo(grupo.getId()));
      activo.setGrupo(grupo);
      Articulo articulo = new Articulo();
      articulo.setId(record.getValue("tipoid", Long.class));
      articulo.setNombrearticulo(mostrarArticulo(articulo.getId()));
      activo.setArticulo(articulo);
      listaActivos.add(activo);
    }
    return listaActivos;
  }
}