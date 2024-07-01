package com.inventarios.handler.categoria;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.categoria.services.UpdateCategoriaAbstractHandler;
import com.inventarios.model.Categoria;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class UpdateCategoriaHandler extends UpdateCategoriaAbstractHandler {

  protected void update(Categoria categoria, Long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.update(GRUPO_TABLE)
      .set(DSL.field("nombregrupo"), categoria.getNombregrupo())
      .set(DSL.field("descripgrupo"), categoria.getDescripgrupo())
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
