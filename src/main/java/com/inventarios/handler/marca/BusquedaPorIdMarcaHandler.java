package com.inventarios.handler.marca;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.marca.services.BusquedaPorIdMarcaAbstractHandler;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;

public class BusquedaPorIdMarcaHandler extends BusquedaPorIdMarcaAbstractHandler {
  protected Result<Record> busquedaPorNombreMarca(String filter) throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(MARCA_TABLE)
            .where(MARCA_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
  }
}