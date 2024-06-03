package com.inventarios.handler.articulos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.articulos.services.DeleteArticuloAbstractHandler;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class DeleteArticuloHandler extends DeleteArticuloAbstractHandler {

  protected void delete(long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.deleteFrom(ARTICULO_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
