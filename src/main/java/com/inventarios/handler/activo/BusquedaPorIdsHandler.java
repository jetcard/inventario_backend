package com.inventarios.handler.activo;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.activo.services.BusquedaPorIdsAbstractHandler;
import com.inventarios.model.EspecificacionesFiltro;
import org.jooq.Record;
import org.jooq.Result;

import java.sql.SQLException;

public class BusquedaPorIdsHandler extends BusquedaPorIdsAbstractHandler {
  /*protected Result<Record> filtraPorIds(especificacionesFiltro filter) throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(ACTIVO_TABLE)
            .where(ACTIVO_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
  }*/
}