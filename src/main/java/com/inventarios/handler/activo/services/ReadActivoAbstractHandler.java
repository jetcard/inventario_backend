package com.inventarios.handler.activo.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.inventarios.handler.activo.CreateActivoHandler;
import com.inventarios.handler.activo.ReadActivoHandler;
import com.inventarios.handler.activo.UpdateActivoHandler;
import com.inventarios.handler.activo.response.ActivoResponseRest;
import com.inventarios.handler.keycloak.service.AuthorizerKeycloakAbstractHandler;
import com.inventarios.model.*;
import com.inventarios.util.GsonFactory;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.*;

public abstract class ReadActivoAbstractHandler //implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
   extends AuthorizerKeycloakAbstractHandler {
  //
  protected final static Table<Record> ACTIVO_TABLE = table("activo");
  protected final static Table<Record> ESPECIFICACIONES_TABLE = table("especificaciones");
  protected final static Field<Long> ACTIVO_ID = field(name("activo", "id"), Long.class);
  protected final static Field<Long> ACTIVO_RESPONSABLE_ID = field(name("activo", "custodioid"), Long.class);
  protected final static Field<Long> ACTIVO_ARTICULO_ID = field(name("activo", "articuloid"), Long.class);
  protected final static Field<Long> ACTIVO_GRUPO_ID = field(name("activo", "categoriaid"), Long.class);

  protected final static Field<Long> ACTIVO_PROVEEDOR_ID = field(name("activo", "proveedorid"), Long.class);

  protected final static Field<Long> ESPECIFICACIONES_ID = field(name("especificaciones", "id"), Long.class);
  protected final static Field<Long> ESPECIFICACIONES_ESPECIFICOID = field(name("especificaciones", "especificacionid"), Long.class);
  protected final static Field<String> ESPECIFICACIONES_NOMBREESPECIFICO = field(name("especificaciones", "nombreatributo"), String.class);
  protected final static Field<String> ESPECIFICACIONES_DESCRIPESPECIFICO = field(name("especificaciones", "descripcionatributo"), String.class);
  protected final static Field<String> ACTIVO_CODINVENTARIO = field(name("activo", "codinventario"), String.class);
  protected final static Field<String> ACTIVO_MODELO = field(name("activo", "modelo"), String.class);
  protected final static Field<String> ACTIVO_MARCA = field(name("activo", "marca"), String.class);
  protected final static Field<String> ACTIVO_NROSERIE = field(name("activo", "nroserie"), String.class);
  protected final static Field<LocalDate> ACTIVO_FECHAINGRESO = field(name("activo", "fechaingreso"), LocalDate.class);
  protected final static Field<String> ACTIVO_FECHAINGRESOSTR = field(name("activo", "fechaingresostr"), String.class);
  protected final static Field<String> ACTIVO_MONEDA = field(name("activo", "moneda"), String.class);
  protected final static Field<String> ACTIVO_IMPORTE = field(name("activo", "importe"), String.class);
  protected final static Field<String> ACTIVO_DESCRIPCION = field(name("activo", "descripcion"), String.class);
  protected final static Table<Record> RESPONSABLE_TABLE = DSL.table("custodio");
  protected static final Field<String> RESPONSABLE_TABLE_COLUMNA = DSL.field("arearesponsable", String.class);
  protected final static Table<Record> ARTICULO_TABLE = table("articulo");
  protected static final Field<String> ARTICULO_TABLE_COLUMNA = DSL.field("nombrearticulo", String.class);
  protected final static Table<Record> TIPO_TABLE = DSL.table("tipo");
  protected static final Field<String> TIPO_TABLE_COLUMNA = DSL.field("nombretipo", String.class);
  protected final static Table<Record> GRUPO_TABLE = DSL.table("categoria");
  protected static final Field<String> GRUPO_TABLE_COLUMNA = DSL.field("nombregrupo", String.class);
  protected final static Table<Record> PROVEEDOR_TABLE = DSL.table("proveedor");
  protected static final Field<String> PROVEEDOR_TABLE_COLUMNA = DSL.field("razonsocial", String.class);

  protected final static Field<Long> ACTIVO_TIPO_ID = field(name("activo", "tipoid"), Long.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected abstract Result<Record18<Long, Long, Long, Long, Long, Long, Long, Long, String, String, String, String, String, LocalDate, String, String, String, String>> read() throws SQLException;
  protected abstract String mostrarCustodio(Long id) throws SQLException;
  protected abstract String mostrarArticulo(Long id) throws SQLException;
  protected abstract String mostrarTipoBien(Long id) throws SQLException;
  protected abstract String mostrarCategoria(Long id) throws SQLException;
  protected abstract String mostrarProveedor(Long id) throws SQLException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent request, final Context context) {
    //input.setHeaders(headers);
    LambdaLogger logger = context.getLogger();
    ActivoResponseRest responseRest = new ActivoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    String output ="";
    try {
      String authToken = extractAuthToken(request);
      logger.log("authToken utilizada en Activo : ");
      logger.log(authToken);
      AuthorizationInfo authInfo = validateAuthToken(authToken);
      if(authInfo!=null){
        logger.log("authInfo = "+authInfo);
        addAuthorizationHeaders(authInfo, request);
        if (authInfo.isAdmin()) {
          logger.log("###########");
          logger.log(" ROL ADMIN");
          logger.log("###########");

          // Aquí se maneja la lógica para crear o actualizar activos
          if (request.getHttpMethod().equalsIgnoreCase("GET")) {
            ReadActivoHandler readActivoHandler = new ReadActivoHandler();
            response = readActivoHandler.handleRequest(request, context);
          } else if (request.getHttpMethod().equalsIgnoreCase("POST")) {
            CreateActivoHandler createEspecificoHandler = new CreateActivoHandler();
            response = createEspecificoHandler.handleRequest(request, context);
          } else if (request.getHttpMethod().equalsIgnoreCase("PUT")) {
            UpdateActivoHandler updateEspecificoHandler = new UpdateActivoHandler();
            response = updateEspecificoHandler.handleRequest(request, context);
          } else {
            responseRest.setMetadata("No autorizado", "-1", "No autorizado para leer, crear o actualizar activos.");
            return response
                    .withBody(new Gson().toJson(responseRest))
                    .withStatusCode(405); // Código HTTP 405 - Método no permitido
          }
        } else if (authInfo.isUser()) {
          logger.log("##########");
          logger.log(" ROL USER ");
          logger.log("##########");
          //CreateespecificacionesHandler createespecificacionesHandler = new CreateespecificacionesHandler();
          //response = createespecificacionesHandler.handleRequest(request, context);
        } else {
          logger.log("##########");
          logger.log(" OTRO ROL ");
          logger.log("##########");
        }

      }else{
        logger.log("authInfo is null");
      }

      Result<Record18<Long, Long, Long, Long, Long, Long, Long, Long, String, String, String, String, String, LocalDate, String, String, String, String>> result = read();
      responseRest.getActivoResponse().setListaactivos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Activos encontrados");
      Gson gson = GsonFactory.createGson();
      output = gson.toJson(responseRest);
      return response.withStatusCode(200).withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al consultar");
      return response
        .withBody(e.toString())
        .withStatusCode(500);
    }
  }

  protected List<Activo> convertResultToList(Result<Record18<Long, Long, Long, Long, Long, Long, Long, Long, String, String, String, String, String, LocalDate, String, String, String, String>> result) throws SQLException {
      Map<Long, Activo> especificoMap = new HashMap<>();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      for (Record18<Long, Long, Long, Long, Long, Long, Long, Long, String, String, String, String, String, LocalDate, String, String, String, String> record : result) {
        Long especificoId = record.get(ACTIVO_ID);
        Activo activo = especificoMap.get(especificoId);
        if (activo == null) {
          activo = new Activo();
          activo.setId(especificoId);
          // Aquí deberías asignar los valores correspondientes a responsable y articulo
          // Consultas adicionales o mapeos deben ser implementados para Responsable y Articulo
          Custodio custodio = new Custodio();
          custodio.setId(record.get(ACTIVO_RESPONSABLE_ID));
          custodio.setArearesponsable(mostrarCustodio(custodio.getId()));
          activo.setCustodio(custodio);
          //especifico.setResponsable(findResponsableById(record.get(ACTIVO_RESPONSABLE_ID)));
          Articulo articulo = new Articulo();
          articulo.setId(record.get(ACTIVO_ARTICULO_ID));
          articulo.setNombrearticulo(mostrarArticulo(articulo.getId()));
          activo.setArticulo(articulo);
          //especifico.setArticulo(findArticuloById(record.get(ACTIVO_ARTICULO_ID)));

          Tipo tipo = new Tipo();
          tipo.setId(record.get(ACTIVO_TIPO_ID));
          tipo.setNombretipo(mostrarTipoBien(tipo.getId()));
          activo.setTipo(tipo);

          Categoria categoria =new Categoria();
          categoria.setId(record.get(ACTIVO_GRUPO_ID));
          categoria.setNombregrupo(mostrarCategoria(categoria.getId()));
          activo.setCategoria(categoria);

          //
          Proveedor proveedor=new Proveedor();
          proveedor.setId(record.get(ACTIVO_PROVEEDOR_ID));
          proveedor.setRazonsocial(mostrarProveedor(proveedor.getId()));
          activo.setProveedor(proveedor);

          activo.setCodinventario(record.getValue("codinventario", String.class));
          activo.setModelo(record.getValue("modelo", String.class));
          activo.setMarca(record.getValue("marca", String.class));
          activo.setNroserie(record.getValue("nroserie", String.class));
          LocalDate fechaIngreso = record.getValue("fechaingreso", LocalDate.class);
          activo.setFechaingreso(fechaIngreso);
          if (fechaIngreso != null) {
            String formattedDate = fechaIngreso.format(formatter);
            activo.setFechaingresostr(formattedDate);
          }
          activo.setMoneda(record.getValue("moneda", String.class));
          activo.setImporte(record.getValue("importe", BigDecimal.class));
          activo.setDescripcion(record.getValue("descripcion", String.class));

          ///especifico.setespecificaciones(new ArrayList<>());
          especificoMap.put(especificoId, activo);
        }
        Long especificacionesId = record.get(ESPECIFICACIONES_ID);
        if (especificacionesId != null) {
          Especificaciones especificaciones = new Especificaciones();
          especificaciones.setId(especificacionesId);
          especificaciones.setEspecificacionid(record.get(ESPECIFICACIONES_ESPECIFICOID));
          especificaciones.setNombreatributo(record.get(ESPECIFICACIONES_NOMBREESPECIFICO));
          especificaciones.setDescripcionatributo(record.get(ESPECIFICACIONES_DESCRIPESPECIFICO));
          ///especifico.getespecificaciones().add(especificaciones);
        }
      }
      return new ArrayList<>(especificoMap.values());
    }
/*
*/

}
