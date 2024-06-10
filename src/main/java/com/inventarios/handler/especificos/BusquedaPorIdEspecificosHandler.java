package com.inventarios.handler.especificos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.especificos.services.BusquedaPorIdEspecificosAbstractHandler;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;

public class BusquedaPorIdEspecificosHandler extends BusquedaPorIdEspecificosAbstractHandler {
  protected Result<Record> busquedaPorNombreEspecificos(String filter) throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(ESPECIFICOS_TABLE)
            .where(ESPECIFICOS_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
  }

}