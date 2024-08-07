package com.inventarios.handler.proveedores;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.proveedores.services.UpdateProveedorAbstractHandler;
import com.inventarios.model.Proveedor;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class UpdateProveedorHandler extends UpdateProveedorAbstractHandler {

  protected void update(Proveedor proveedor, Long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.update(PROVEEDOR_TABLE)
      .set(DSL.field("ruc"), proveedor.getRuc())
      .set(DSL.field("razonsocial"), proveedor.getRazonsocial())
      .set(DSL.field("direccionfiscal"), proveedor.getDireccionfiscal())
      .set(DSL.field("contacto"), proveedor.getContacto())
      .set(DSL.field("telefono"), proveedor.getTelefono())
      .set(DSL.field("correo"), proveedor.getCorreo())
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
