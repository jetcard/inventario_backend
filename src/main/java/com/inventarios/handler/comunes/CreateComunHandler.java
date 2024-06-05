package com.inventarios.handler.comunes;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.comunes.services.CreateComunAbstractHandler;
import com.inventarios.model.Comun;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class CreateComunHandler extends CreateComunAbstractHandler {
  @Override
  protected void save(Comun comun, Long responsableID, Long tipoID, Long grupoID) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(COMUN_TABLE)
            .set(DSL.field("descripcomun"), comun.getDescripcomun().toUpperCase())
            //.set(DSL.field("descripcortacomun"), comun.getDescripcortacomun())
            .set(DSL.field("responsableID"), responsableID)
            .set(DSL.field("tipoID"), tipoID)
            .set(DSL.field("grupoId"), grupoID)
            .execute();

  }

}
