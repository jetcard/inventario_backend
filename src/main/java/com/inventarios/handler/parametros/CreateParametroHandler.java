package com.inventarios.handler.parametros;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.parametros.services.CreateParametroAbstractHandler;
import com.inventarios.model.Parametro;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class CreateParametroHandler extends CreateParametroAbstractHandler {
  @Override
  protected void save(String nombre, String descripcion) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(PARAMETRO_TABLE)
            .set(DSL.field("nombre"), nombre)
            .set(DSL.field("descripcion"), descripcion)
            .execute();

  }

}
