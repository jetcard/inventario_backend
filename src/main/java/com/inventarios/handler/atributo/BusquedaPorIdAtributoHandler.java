package com.inventarios.handler.atributo;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributo.services.BusquedaPorIdAtributoAbstractHandler;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;

public class BusquedaPorIdAtributoHandler extends BusquedaPorIdAtributoAbstractHandler {

  @Override
  protected Result<Record> busquedaPorArticuloId(String articuloId) throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(ATRIBUTO_TABLE)
            .where(ATRIBUTO_TABLE_COLUMNA.eq(Long.parseLong(articuloId)))
            .fetch();
  }

}