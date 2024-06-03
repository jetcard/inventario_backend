package com.inventarios.handler.responsables;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.responsables.services.BusquedaPorIdResponsableAbstractHandler;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;

public class BusquedaPorIdResponsableHandler extends BusquedaPorIdResponsableAbstractHandler {
  protected Result<Record> busquedaPorNombreResponsable(String filter) throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(RESPONSABLE_TABLE)
            .where(RESPONSABLE_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
  }

}