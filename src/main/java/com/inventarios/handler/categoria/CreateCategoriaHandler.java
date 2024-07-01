package com.inventarios.handler.categoria;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.categoria.services.CreateCategoriaAbstractHandler;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class CreateCategoriaHandler extends CreateCategoriaAbstractHandler {
  protected void save(String nombregrupo, String descripgrupo) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(GRUPO_TABLE)
      .set(DSL.field("nombregrupo"), nombregrupo)
      .set(DSL.field("descripgrupo"), descripgrupo)
      .execute();
  }

}
