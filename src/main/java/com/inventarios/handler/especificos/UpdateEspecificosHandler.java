package com.inventarios.handler.especificos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.especificos.services.UpdateEspecificosAbstractHandler;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class UpdateEspecificosHandler extends UpdateEspecificosAbstractHandler {
  protected void update(Long id, String descripespecifico, String nombreespecifico) throws SQLException {
    var dsl = RDSConexion.getDSL();
    //DSLContext dsl = DependencyFactory.getDSL();
    dsl.update(ESPECIFICOS_TABLE)
      //.set(DSL.field("descripespecifico"), descripespecifico)
      .set(DSL.field("nombreespecifico"), nombreespecifico)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
