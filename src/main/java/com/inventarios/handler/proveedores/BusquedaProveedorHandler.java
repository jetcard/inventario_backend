package com.inventarios.handler.proveedores;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.proveedores.services.BusquedaProveedorAbstractHandler;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;

public class BusquedaProveedorHandler extends BusquedaProveedorAbstractHandler {

  @Override
  protected Result<Record> busquedaPorNombreProveedor(String filter) throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(PROVEEDOR_TABLE)
            .where(PROVEEDOR_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
  }

  @Override
  protected Result<Record> autocompletarProveedor(String filter) throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(PROVEEDOR_TABLE)
            .where(PROVEEDOR_TABLE_COLUMNA.likeIgnoreCase("%" + filter + "%"))
            .limit(10) // Limita el número de resultados a 10
            .fetch();
  }
}