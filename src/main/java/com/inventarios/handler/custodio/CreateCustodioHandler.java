package com.inventarios.handler.custodio;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.custodio.services.CreateCustodioAbstractHandler;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class CreateCustodioHandler extends CreateCustodioAbstractHandler {
  protected void save(String arearesponsable, String nombresyapellidos) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(RESPONSABLE_TABLE)
      .set(DSL.field("arearesponsable"), arearesponsable)
      .set(DSL.field("nombresyapellidos"), nombresyapellidos)
      .execute();
  }

}
