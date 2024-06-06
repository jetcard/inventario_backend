package com.inventarios.handler.parametros;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.parametros.services.BusquedaPorIdsAbstractHandler;
import java.sql.SQLException;
import com.inventarios.model.AtributosFiltro;
import org.jooq.Record;
import org.jooq.Result;

public class BusquedaPorIdParametroHandler extends BusquedaPorIdsAbstractHandler {
  protected Result<Record> filterAtributos(AtributosFiltro filter) throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(PARAMETRO_TABLE)
            .where(PARAMETRO_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
  }
}