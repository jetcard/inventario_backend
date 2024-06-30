package com.inventarios.handler.activo;

import com.inventarios.core.RDSConexion;
import java.sql.SQLException;
import com.inventarios.handler.activo.services.DeleteActivoAbstractHandler;
import org.jooq.impl.DSL;

public class DeleteActivoHandler extends DeleteActivoAbstractHandler {

  protected void delete(long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.deleteFrom(ACTIVO_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
