package com.inventarios.handler.proveedores;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.proveedores.services.CreateProveedorAbstractHandler;
import java.sql.SQLException;

import org.jooq.impl.DSL;

public class CreateProveedorHandler extends CreateProveedorAbstractHandler {


  protected void save(String ruc,
                      String razonsocial,
                      String direccionfiscal,
                      String contacto,
                      String telefono,
                      String correo,
                      long custodioId) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(PROVEEDOR_TABLE)
      .set(DSL.field("ruc"), ruc)
      .set(DSL.field("razonsocial"), razonsocial)
      .set(DSL.field("direccionfiscal"), direccionfiscal)
      .set(DSL.field("contacto"), contacto)
      .set(DSL.field("telefono"), telefono)
      .set(DSL.field("correo"), correo)
      .set(DSL.field("custodioId"), custodioId)
      .execute();
  }

}
