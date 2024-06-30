package com.inventarios.handler.especificaciones;

import com.inventarios.core.RDSConexion;
import java.sql.SQLException;
import com.inventarios.handler.especificaciones.services.UpdateEspecificacionesAbstractHandler;
import org.jooq.impl.DSL;
public class UpdateEspecificacionesHandler extends UpdateEspecificacionesAbstractHandler {
  protected void update(Long id, String descripespecifico, String nombreespecifico) throws SQLException {
    var dsl = RDSConexion.getDSL();
    //DSLContext dsl = DependencyFactory.getDSL();
    dsl.update(ESPECIFICACIONES_TABLE)
      //.set(DSL.field("descripespecifico"), descripespecifico)
      .set(DSL.field("nombreespecifico"), nombreespecifico)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
