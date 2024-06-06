package com.inventarios.handler.parametros;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.parametros.services.UpdateParametroAbstractHandler;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class UpdateParametroHandler extends UpdateParametroAbstractHandler {

  protected void update(Long id, String nombre, String descripcion) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.update(PARAMETRO_TABLE)
      .set(DSL.field("nombre"), nombre)
      .set(DSL.field("descripcion"), descripcion)
      //
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
