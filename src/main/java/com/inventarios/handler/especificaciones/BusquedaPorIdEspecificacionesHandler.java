package com.inventarios.handler.especificaciones;

import com.inventarios.core.RDSConexion;
import java.sql.SQLException;
import com.inventarios.handler.especificaciones.services.BusquedaPorIdEspecificacionesAbstractHandler;
import org.jooq.Record;
import org.jooq.Result;
public class BusquedaPorIdEspecificacionesHandler extends BusquedaPorIdEspecificacionesAbstractHandler {
  protected Result<Record> busquedaPorNombreespecificaciones(String filter) throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(ESPECIFICACIONES_TABLE)
            .where(ESPECIFICACIONES_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
  }

}