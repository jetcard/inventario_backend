package com.inventarios.handler.especifico;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.especifico.services.DeleteEspecificoAbstractHandler;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class DeleteEspecificoHandler extends DeleteEspecificoAbstractHandler {

  protected void delete(long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.deleteFrom(ESPECIFICO_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
