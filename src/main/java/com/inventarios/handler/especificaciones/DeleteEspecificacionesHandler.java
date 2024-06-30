package com.inventarios.handler.especificaciones;

import com.inventarios.core.RDSConexion;
import java.sql.SQLException;
import com.inventarios.handler.especificaciones.services.DeleteEspecificacionesAbstractHandler;
import org.jooq.impl.DSL;
public class DeleteEspecificacionesHandler extends DeleteEspecificacionesAbstractHandler {
  protected void delete(long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.deleteFrom(ESPECIFICACIONES_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
