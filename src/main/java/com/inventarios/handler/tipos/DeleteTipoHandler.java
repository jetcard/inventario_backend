package com.inventarios.handler.tipos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.tipos.services.DeleteTipoAbstractHandler;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class DeleteTipoHandler extends DeleteTipoAbstractHandler {

  protected void delete(long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.deleteFrom(TIPO_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
