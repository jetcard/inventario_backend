package com.inventarios.handler.especificos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.especificos.services.DeleteEspecificosAbstractHandler;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class DeleteEspecificosHandler extends DeleteEspecificosAbstractHandler {
  protected void delete(long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.deleteFrom(ESPECIFICOS_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
