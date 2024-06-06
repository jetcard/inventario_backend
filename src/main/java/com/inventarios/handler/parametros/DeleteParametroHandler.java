package com.inventarios.handler.parametros;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.parametros.services.DeleteParametroAbstractHandler;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class DeleteParametroHandler extends DeleteParametroAbstractHandler {

  protected void delete(long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.deleteFrom(PARAMETRO_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
