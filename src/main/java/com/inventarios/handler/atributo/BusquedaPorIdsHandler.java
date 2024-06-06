package com.inventarios.handler.atributo;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributo.services.BusquedaPorIdsAbstractHandler;
import com.inventarios.model.AtributosFiltro;
import org.jooq.Record;
import org.jooq.Result;

import java.sql.SQLException;

public class BusquedaPorIdsHandler extends BusquedaPorIdsAbstractHandler {
  protected Result<Record> filtraPorIds(AtributosFiltro filter) throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(ATRIBUTO_TABLE)
            .where(ATRIBUTO_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
  }
}