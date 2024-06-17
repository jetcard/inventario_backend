package com.inventarios.handler.activos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.activos.services.DeleteActivoAbstractHandler;
import java.sql.SQLException;
import org.jooq.impl.DSL;
public class DeleteActivoHandler extends DeleteActivoAbstractHandler {
  protected void delete(long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.deleteFrom(ACTIVO_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}