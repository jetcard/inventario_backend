package com.inventarios.handler.categoria;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.categoria.services.BusquedaCategoriaAbstractHandler;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;
public class BusquedaCategoriaHandler extends BusquedaCategoriaAbstractHandler {
  protected Result<Record> busquedaPorNombreGrupo(String filter) throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(GRUPO_TABLE)
            .where(GRUPO_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
  }
}