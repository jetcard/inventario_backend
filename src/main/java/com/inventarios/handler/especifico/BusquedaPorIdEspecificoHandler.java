package com.inventarios.handler.especifico;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.especifico.services.BusquedaPorIdEspecificoAbstractHandler;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;

public class BusquedaPorIdEspecificoHandler extends BusquedaPorIdEspecificoAbstractHandler {
  protected Result<Record> busquedaPorNombreEspecifico(String filter) throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(ESPECIFICO_TABLE)
            .where(ESPECIFICO_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
  }
}