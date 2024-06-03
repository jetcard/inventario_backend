package com.inventarios.handler.tipos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.tipos.services.BusquedaPorIdTipoAbstractHandler;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;

public class BusquedaPorIdTipoHandler extends BusquedaPorIdTipoAbstractHandler {
  protected Result<Record> busquedaPorNombreTipo(String filter) throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(TIPO_TABLE)
            .where(TIPO_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
  }

}