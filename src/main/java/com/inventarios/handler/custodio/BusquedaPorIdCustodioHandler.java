package com.inventarios.handler.custodio;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.custodio.services.BusquedaPorIdCustodioAbstractHandler;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;
public class BusquedaPorIdCustodioHandler extends BusquedaPorIdCustodioAbstractHandler {
  protected Result<Record> busquedaPorNombreResponsable(String filter) throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(RESPONSABLE_TABLE)
            .where(RESPONSABLE_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
  }
}