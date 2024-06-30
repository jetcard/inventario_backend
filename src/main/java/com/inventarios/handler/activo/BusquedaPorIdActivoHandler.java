package com.inventarios.handler.activo;

import com.inventarios.core.RDSConexion;
import java.sql.SQLException;
import com.inventarios.handler.activo.services.BusquedaPorIdActivoAbstractHandler;
import org.jooq.Record;
import org.jooq.Result;

public class BusquedaPorIdActivoHandler extends BusquedaPorIdActivoAbstractHandler {
  protected Result<Record> busquedaPorNombreEspecifico(String filter) throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(ACTIVO_TABLE)
            .where(ACTIVO_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
  }
}