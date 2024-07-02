package com.inventarios.handler.proveedores.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.handler.proveedores.response.ProveedorResponseRest;
import com.inventarios.model.Proveedor;
import java.sql.SQLException;
import java.util.*;

import com.inventarios.util.GsonFactory;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public abstract class BusquedaProveedorAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final org.jooq.Table<?> PROVEEDOR_TABLE = DSL.table("proveedor");
  protected static final org.jooq.Field<String> PROVEEDOR_TABLE_COLUMNA = DSL.field("ruc", String.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }
  protected abstract Result<Record> busquedaPorNombreProveedor(String filter) throws SQLException;
  protected abstract Result<Record> autocompletarProveedor(String filter) throws SQLException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    LambdaLogger logger = context.getLogger();
    ProveedorResponseRest responseRest = new ProveedorResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    String path = input.getPath();
    Map<String, String> pathParameters = input.getPathParameters();
    String output = "";
    try {
      if (path.contains("/busqueda/")) {
        String idString = pathParameters.get("id");
        logger.log("buscar: " + idString);
        Result<Record> result = busquedaPorNombreProveedor(idString);
        responseRest.getProveedorResponse().setListaproveedores(convertResultToList(result));
        responseRest.setMetadata("Respuesta ok", "00", "Proveedores encontrados");
        output = GsonFactory.createGson().toJson(responseRest);
        return response.withStatusCode(200).withBody(output);
      } else if (path.contains("/autocompletar/")) {
        String filter = pathParameters.get("id");
        logger.log("Autocompletar proveedor por filtro: " + filter);
        Result<Record> result = autocompletarProveedor(filter);
        responseRest.getProveedorResponse().setListaproveedores(convertResultToList(result));
        responseRest.setMetadata("Respuesta ok", "00", "Proveedores encontrados");
        output = GsonFactory.createGson().toJson(responseRest);
        return response.withStatusCode(200).withBody(output);
      } else {
        responseRest.setMetadata("Error interno", "-1", "Ruta no encontrada");
        output = GsonFactory.createGson().toJson(responseRest);
        return response.withStatusCode(500).withBody(output);
      }
    } catch (Exception e) {
      logger.log("Error interno del servidor: " + e);
      responseRest.setMetadata("Error interno", "-1", "Error al obtener proveedores");
      output = GsonFactory.createGson().toJson(responseRest);
      return response.withStatusCode(500).withBody(output);
    }

    /*
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    logger.log("buscar: " + idString);

    try {
      Result<Record> result = busquedaPorNombreProveedor(idString);
      responseRest.getProveedorResponse().setListaproveedores(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Proveedors encontrados");
      output = GsonFactory.createGson().toJson(responseRest);
      return response.withStatusCode(200)
                    .withBody(output);
    } catch (Exception e) {
        responseRest.setMetadata("Respuesta nok", "-1", "Error al guardar");
            return response
                    .withBody(e.toString())
        .withStatusCode(500);
    }*/
  }

  protected List<Proveedor> convertResultToList(Result<Record> result) {
    List<Proveedor> listaProveedores = new ArrayList<>();
    for (Record record : result) {
      Proveedor proveedor = new Proveedor();
      proveedor.setId(record.getValue("id", Long.class));
      proveedor.setRazonsocial(record.getValue("razonsocial", String.class));
      proveedor.setRuc(record.getValue("ruc", String.class));
      listaProveedores.add(proveedor);
    }
    return listaProveedores;
  }
}