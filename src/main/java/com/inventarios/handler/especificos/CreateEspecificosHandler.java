package com.inventarios.handler.especificos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.especificos.services.CreateEspecificosAbstractHandler;
import com.inventarios.model.Especificos;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class CreateEspecificosHandler extends CreateEspecificosAbstractHandler {
  @Override
  protected void save(Especificos especificos, Long grupoID) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(ESPECIFICOS_TABLE)
            //.set(DSL.field("descripespecifico"), especificos.getDescripespecifico())
            .set(DSL.field("nombreespecifico"), especificos.getNombreespecifico().toUpperCase())
            .set(DSL.field("grupoId"), grupoID)
            .execute();

  }

}
