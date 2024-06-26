package com.inventarios.handler.articulos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.articulos.services.CreateArticuloAbstractHandler;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class CreateArticuloHandler extends CreateArticuloAbstractHandler {
  protected void save(String nombrearticulo, String descriparticulo) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(ARTICULO_TABLE)
      .set(DSL.field("nombrearticulo"), nombrearticulo)
      .set(DSL.field("descriparticulo"), descriparticulo)
      .execute();
  }

}
