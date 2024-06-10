package com.inventarios.handler.especifico;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.especifico.services.UpdateEspecificoAbstractHandler;
import com.inventarios.model.Articulo;
import com.inventarios.model.Grupo;
import com.inventarios.model.Responsable;
import java.sql.SQLException;

import com.inventarios.model.Tipo;
import org.jooq.impl.DSL;

public class UpdateEspecificoHandler extends UpdateEspecificoAbstractHandler {

  protected void update(Long id,
                        Responsable responsable,
                        Articulo articulo,
                        Tipo tipo,
                        Grupo grupo) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.update(ESPECIFICO_TABLE)
      .set(DSL.field("responsable"), responsable)
      .set(DSL.field("articulo"), articulo)
            .set(DSL.field("tipo"), tipo)
            .set(DSL.field("grupo"), grupo)
            .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
