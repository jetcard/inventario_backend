package com.inventarios.handler.custodio;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.custodio.services.UpdateCustodioAbstractHandler;
import com.inventarios.model.Custodio;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class UpdateCustodioHandler extends UpdateCustodioAbstractHandler {

  protected void update(Custodio custodio, Long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.update(RESPONSABLE_TABLE)
      .set(DSL.field("arearesponsable"), custodio.getArearesponsable())
      .set(DSL.field("nombresyapellidos"), custodio.getNombresyapellidos())
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
