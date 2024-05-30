package com.inventarios.handler.proveedores;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.proveedores.services.BusquedaProveedorAbstractHandler;
import org.jooq.Record;
import org.jooq.Result;

public class BusquedaProveedorHandler extends BusquedaProveedorAbstractHandler {

  @Override
  protected Result<Record> busquedaPorNombreProveedor(String filter) {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(PROVEEDOR_TABLE)
            .where(PROVEEDOR_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
  }

  @Override
  protected Result<Record> autocompletarProveedor(String filter) {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(PROVEEDOR_TABLE)
            .where(PROVEEDOR_TABLE_COLUMNA.likeIgnoreCase("%" + filter + "%"))
            .limit(10) // Limita el número de resultados a 10
            .fetch();
  }
}