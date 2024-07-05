package com.inventarios.handler.marca;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.marca.services.DeleteMarcaAbstractHandler;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class DeleteMarcaHandler extends DeleteMarcaAbstractHandler {

  protected void delete(long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.deleteFrom(MARCA_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}