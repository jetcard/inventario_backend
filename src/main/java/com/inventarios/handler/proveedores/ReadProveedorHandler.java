package com.inventarios.handler.proveedores;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.proveedores.services.ReadProveedorAbstractHandler;
import java.sql.SQLException;

import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;

public class ReadProveedorHandler extends ReadProveedorAbstractHandler {
  /*protected Result<Record> read() throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select().from(PROVEEDOR_TABLE).fetch();
  }*/

  protected Result<Record9<Long, String, String, String, String, String, String, Long, String>> read() throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select(
                    PROVEEDOR_TABLE_ID, PROVEEDOR_TABLE_RAZONSOCIAL, PROVEEDOR_TABLE_RUC,
                    PROVEEDOR_TABLE_DIRECCIONFISCAL, PROVEEDOR_TABLE_CONTACTO, PROVEEDOR_TABLE_TELEFONO,
                    PROVEEDOR_TABLE_CORREO,
                    CUSTODIOID_PROVEEDOR_TABLE, CUSTODIO_AREA_RESPONSABLE
            )
            .from(PROVEEDOR_TABLE)
            .leftJoin(CUSTODIO_TABLE)
            .on(CUSTODIOID_PROVEEDOR_TABLE.eq(CUSTODIO_TABLE_ID))
            .fetch();
  }

}