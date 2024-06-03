package com.inventarios.handler.proveedores;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.proveedores.services.DeleteProveedorAbstractHandler;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class DeleteProveedorHandler extends DeleteProveedorAbstractHandler {

  protected void delete(long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.deleteFrom(PROVEEDOR_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
