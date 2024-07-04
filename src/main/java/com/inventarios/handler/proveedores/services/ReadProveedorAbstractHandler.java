package com.inventarios.handler.proveedores.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import java.sql.SQLException;
import java.util.*;
import com.inventarios.model.Proveedor;
import com.inventarios.handler.proveedores.response.ProveedorResponseRest;
import com.inventarios.util.GsonFactory;
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

public abstract class ReadProveedorAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  protected final static Table<Record> PROVEEDOR_TABLE = DSL.table("proveedor");
  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected abstract Result<Record> read() throws SQLException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    ProveedorResponseRest responseRest = new ProveedorResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers);
    String output ="";
    try {
      Result<Record> result = read();
      responseRest.getProveedorResponse().setListaproveedores(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Proveedores encontrados");
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

  protected List<Proveedor> convertResultToList(Result<Record> result) {
    List<Proveedor> listaProveedores = new ArrayList<>();
    for (Record record : result) {
      Proveedor proveedor = new Proveedor();
      proveedor.setId(record.getValue("id", Long.class));
      proveedor.setRuc(record.getValue("ruc", String.class));
      proveedor.setRazonsocial(record.getValue("razonsocial", String.class));
      proveedor.setDireccionfiscal(record.getValue("direccionfiscal", String.class));
      proveedor.setContacto(record.getValue("contacto", String.class));
      proveedor.setTelefono(record.getValue("telefono", String.class));
      proveedor.setCorreo(record.getValue("correo", String.class));
      listaProveedores.add(proveedor);
    }
    return listaProveedores;
  }

}