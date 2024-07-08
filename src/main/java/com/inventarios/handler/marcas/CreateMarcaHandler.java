package com.inventarios.handler.marcas;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.marcas.services.CreateMarcaAbstractHandler;
import com.inventarios.model.Marca;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class CreateMarcaHandler extends CreateMarcaAbstractHandler {
  @Override
  protected void save(Marca marca) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(MARCA_TABLE)
            .set(DSL.field("nombre"), marca.getNombre().toUpperCase())
            .set(DSL.field("descripcion"), marca.getDescripcion().toUpperCase())
            .execute();

  }
/**
 *   CRUD TABLA MARCA
 *
 *   FUNCIÓN BUSCAR PROVEEDOR (PARA EL LUNES)
 *
 *   AMARRE PROVEEDOR+CUSTODIO (1-n)
 *
 *
 */
}
