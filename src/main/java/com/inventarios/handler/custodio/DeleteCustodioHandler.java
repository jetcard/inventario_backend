package com.inventarios.handler.custodio;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.custodio.services.DeleteCustodioAbstractHandler;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class DeleteCustodioHandler extends DeleteCustodioAbstractHandler {

  protected void delete(long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.deleteFrom(RESPONSABLE_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
