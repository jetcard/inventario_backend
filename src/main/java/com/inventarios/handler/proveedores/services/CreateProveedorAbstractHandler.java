package com.inventarios.handler.proveedores.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.model.Proveedor;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import com.inventarios.handler.proveedores.response.ProveedorResponseRest;
import com.inventarios.util.GsonFactory;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class CreateProveedorAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> PROVEEDOR_TABLE = DSL.table("proveedor");
  final static Map<String, String> headers = new HashMap<>();
  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "POST");
  }
  protected abstract void save(String ruc,
                               String razonsocial,
                               String direccionfiscal,
                               String contacto,
                               String telefono,
                               String correo ) throws SQLException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    ProveedorResponseRest responseRest = new ProveedorResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers);
    //String body = input.getBody();
    String body =
            "{\"ruc\":\"27798362723\",\"razonsocial\":\"CONSULTORES\",\"direccionfiscal\":\"DIRECCION\",\"contacto\":\"HJASJSD\", \"telefono\": \"888888111\",\"correo\": \"kahh@ddkk.com\"}";
    context.getLogger().log("body " + body);
    String output ="";
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Proveedor proveedor = GsonFactory.createGson().fromJson(body, Proveedor.class);
        if (proveedor != null) {
          save(proveedor.getRuc(),
              proveedor.getRazonsocial().toUpperCase(),
              proveedor.getDireccionfiscal().toUpperCase(),
              proveedor.getContacto().toUpperCase(),
              proveedor.getTelefono().toUpperCase(),
              proveedor.getCorreo().toLowerCase()
          );
          responseRest.setMetadata("Respuesta ok", "00", "Proveedor guardado");
        }
        output = GsonFactory.createGson().toJson(responseRest);
      }
      return response.withStatusCode(200)
              .withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al guardar");
      return response
        .withBody(e.toString())
        .withStatusCode(500);
    }
  }
}