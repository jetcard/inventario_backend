package com.inventarios.handler.marcas;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.marcas.services.DeleteMarcaAbstractHandler;
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