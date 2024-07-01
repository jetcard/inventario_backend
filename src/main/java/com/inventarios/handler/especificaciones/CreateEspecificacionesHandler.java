package com.inventarios.handler.especificaciones;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.especificaciones.services.CreateEspecificacionesAbstractHandler;
import com.inventarios.model.Especificaciones;
import java.sql.SQLException;
import org.jooq.impl.DSL;
public class CreateEspecificacionesHandler extends CreateEspecificacionesAbstractHandler {
  @Override
  protected void save(Especificaciones especificaciones, Long activoID) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(ESPECIFICACIONES_TABLE)
            //.set(DSL.field("descripespecifico"), especificaciones.getDescripespecifico())
            .set(DSL.field("nombreatributo"), especificaciones.getNombreatributo().toUpperCase())
            .set(DSL.field("descripcionatributo"), especificaciones.getDescripcionatributo().toUpperCase())
            .set(DSL.field("especificacionid"), activoID)
            .execute();
  }
}