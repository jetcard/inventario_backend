package com.inventarios.handler.categoria;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.categoria.services.DeleteCategoriaAbstractHandler;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class DeleteCategoriaHandler extends DeleteCategoriaAbstractHandler {

  protected void delete(long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.deleteFrom(GRUPO_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
