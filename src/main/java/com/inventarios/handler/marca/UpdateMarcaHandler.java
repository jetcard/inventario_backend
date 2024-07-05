package com.inventarios.handler.marca;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.marca.services.UpdateMarcaAbstractHandler;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class UpdateMarcaHandler extends UpdateMarcaAbstractHandler {

  protected void update(Long id, String descripmarca) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.update(MARCA_TABLE)
      .set(DSL.field("descripmarca"), descripmarca)
      //.set(DSL.field("descripcortamarca"), descripcortamarca)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
