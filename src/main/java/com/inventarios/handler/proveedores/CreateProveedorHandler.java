package com.inventarios.handler.proveedores;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.proveedores.services.CreateProveedorAbstractHandler;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class CreateProveedorHandler extends CreateProveedorAbstractHandler {
  protected void save(String ruc, String razonsocial) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(PROVEEDOR_TABLE)
      .set(DSL.field("ruc"), ruc)
      .set(DSL.field("razonsocial"), razonsocial)
      .execute();
  }

}
