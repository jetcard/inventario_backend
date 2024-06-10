package com.inventarios.handler.especifico;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.especifico.services.BusquedaPorIdsAbstractHandler;
import com.inventarios.model.EspecificosFiltro;
import org.jooq.Record;
import org.jooq.Result;

import java.sql.SQLException;

public class BusquedaPorIdsHandler extends BusquedaPorIdsAbstractHandler {
  protected Result<Record> filtraPorIds(EspecificosFiltro filter) throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(ESPECIFICO_TABLE)
            .where(ESPECIFICO_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
  }
}