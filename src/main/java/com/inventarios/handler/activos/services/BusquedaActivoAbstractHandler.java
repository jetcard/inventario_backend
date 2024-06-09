package com.inventarios.handler.activos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.handler.activos.response.ActivoResponseRest;
import com.inventarios.model.*;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.impl.DSL;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public abstract class BusquedaActivoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final Table<Record> ACTIVO_TABLE = DSL.table("activo");
  protected static final Table<Record> RESPONSABLE_TABLE = DSL.table("responsable");
  protected static final Field<String> RESPONSABLE_TABLE_COLUMNA = DSL.field("arearesponsable", String.class);
  protected static final Table<Record> TIPO_TABLE = DSL.table("tipo");
  protected static final Field<String> TIPO_TABLE_COLUMNA = DSL.field("nombretipo", String.class);
  protected static final Table<Record> GRUPO_TABLE = DSL.table("grupo");
  protected static final Field<String> GRUPO_TABLE_COLUMNA = DSL.field("nombregrupo", String.class);
  protected static final Table<Record> ARTICULO_TABLE = DSL.table("articulo");
  protected static final Field<String> ARTICULO_TABLE_COLUMNA = DSL.field("nombrearticulo", String.class);
  protected static final Table<Record> PROVEEDOR_TABLE = DSL.table("proveedor");
  protected static final Field<String> PROVEEDOR_TABLE_COLUMNA = DSL.field("razonsocial", String.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected abstract String mostrarResponsable(Long id) throws SQLException;
  protected abstract String mostrarTipoBien(Long id) throws SQLException;
  protected abstract String mostrarGrupo(Long id) throws SQLException;
  protected abstract String mostrarArticulo(Long id) throws SQLException;
  protected abstract String mostrarProveedor(Long id) throws SQLException;
  protected abstract Result<Record> busquedaActivo(String responsable, String proveedor,
                                                   String codinventario, String modelo,
                                                   String marca, String nroSerie,
                                                   LocalDate fechaCompraDesde,
                                                   LocalDate fechaCompraHasta) throws SQLException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    //input.setHeaders(headers);
    String output = "";
    LambdaLogger logger = context.getLogger();
    logger.log("==================================== busquedaActivo ===================================== ");
    ///String path = "/activos/campo?responsable=adm&proveedor=&codinventario=&modelo=&marca=&nroserie=&fechadesde=2024-01-01&fechahasta=2025-06-01";//input.getPath();
    String path = input.getPath();
    logger.log("==================================== path =  "+path);//path = /activos/campo/
    Map<String, String> pathParameters = input.getPathParameters();
    logger.log("==================================== pathParameters =  "+pathParameters);
    //pathParameters = {id=campo}
    if (path.contains("campo")) {
      logger.log("==================================== C A M P O ===================================== ");
      String idString1 = pathParameters.get("id");
      logger.log("==================================== C A M P O ===================================== " + idString1);
      //==================================== C A M P O ===================================== campo
    } else if (path.contains("/busqueda/")) {
      logger.log("=================================== ELSE  IF BUSQUEDA ====================================== " );
      String idString2 = pathParameters.get("id");
      logger.log("==================================== E L S E ===================================== " + idString2);
    } else {
      logger.log("==================================== ELSE  ===================================== " );
      String idString3 = pathParameters.get("id");
      logger.log("==================================== E L S E ===================================== " + idString3);
    }
    //input.setHeaders(headers);
    String idString = pathParameters.get("id");
    if (idString.contains("campo")) {
      logger.log("==================================== trabajao cpn C A M P O ===================================== ");


    } else if (path.contains("busqueda")) {
      logger.log("=================================== trabajo con  BUSQUEDA ====================================== " );
    } else {
      logger.log("==================================== ELSE  ===================================== " );
    }
    String idCampo = pathParameters.get("campo");
    logger.log("C A M P O =====================================> "+idCampo);

    ActivoResponseRest responseRest = new ActivoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    // Obtener los parámetros de consulta
    Map<String, String> queryParameters = input.getQueryStringParameters();
    String responsable = queryParameters != null ? queryParameters.get("responsable") : null;
    logger.log("responsable: " + responsable);
    String proveedor = queryParameters != null ? queryParameters.get("proveedor") : null;
    logger.log("proveedor: " + proveedor);
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

    LocalDate fechaDesde = null;
    LocalDate fechaHasta = null;

    if (fechaCompraDesde != null && !fechaCompraDesde.isEmpty()) {
      try {
        fechaDesde = LocalDate.parse(fechaCompraDesde);
      } catch (DateTimeParseException e) {
        e.printStackTrace();
        // Manejar el error de formato de fecha aquí si es necesario
      }
    }

    if (fechaCompraHasta != null && !fechaCompraHasta.isEmpty()) {
      try {
        fechaHasta = LocalDate.parse(fechaCompraHasta);
      } catch (DateTimeParseException e) {
        e.printStackTrace();
        // Manejar el error de formato de fecha aquí si es necesario
      }
    }
    try {
      Result<Record> result = busquedaActivo(responsable, proveedor, codinventario, modelo, marca, nroSerie, fechaDesde, fechaHasta);
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
  
  protected List<Activo> convertResultToList(Result<Record> result) throws SQLException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    List<Activo> listaActivos = new ArrayList<>();
    for (Record record : result) {
      Activo activo = new Activo();
      activo.setId(record.getValue("id", Long.class));
      activo.setCodinventario(record.getValue("codinventario", String.class));
      activo.setModelo(record.getValue("modelo", String.class));
      activo.setMarca(record.getValue("marca", String.class));
      activo.setNroserie(record.getValue("nroserie", String.class));
      LocalDate fechaIngreso = record.getValue("fechaingreso", LocalDate.class);
      if (fechaIngreso != null) {
        String formattedDate = fechaIngreso.format(formatter);
        //activo.setFechaingreso(formattedDate);
        // Asegúrate de que fechaingreso sea un String en la clase Activo
      }
      activo.setFechaingreso(record.getValue("fechaingreso", Date.class));

      activo.setMoneda(record.getValue( "nroserie", String.class));
      activo.setImporte(record.getValue("importe", BigDecimal.class));

      Responsable responsable = new Responsable();
      responsable.setId(record.getValue("responsableid", Long.class));
      responsable.setArearesponsable(mostrarResponsable(responsable.getId()));
      //responsable.setArearesponsable(record.getValue(RESPONSABLE_TABLE.field("arearesponsable"), String.class));
      /*responsable.setNombreusuario(record.getValue(RESPONSABLE_TABLE.field("nombreusuario"), String.class));
      responsable.setNombresyapellidos(record.getValue(RESPONSABLE_TABLE.field("nombresyapellidos"), String.class));
      responsable.setCorreo(record.getValue(RESPONSABLE_TABLE.field("correo"), String.class));*/

      activo.setResponsable(responsable);

      Proveedor proveedor = new Proveedor();
      proveedor.setId(record.getValue("proveedorid", Long.class));
      proveedor.setRazonsocial(mostrarProveedor(proveedor.getId()));
      activo.setProveedor(proveedor);

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