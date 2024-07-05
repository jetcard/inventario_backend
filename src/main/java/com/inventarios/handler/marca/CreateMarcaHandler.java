package com.inventarios.handler.marca;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.marca.services.CreateMarcaAbstractHandler;
import com.inventarios.model.Marca;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class CreateMarcaHandler extends CreateMarcaAbstractHandler {
  @Override
  protected void save(Marca marca, Long custodioId, Long tipoID, Long categoriaId) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(MARCA_TABLE)
            .set(DSL.field("descripcion"), marca.getDescripcion().toUpperCase())
            //.set(DSL.field("descripcortamarca"), marca.getDescripcortamarca())
            .set(DSL.field("custodioId"), custodioId)
            .set(DSL.field("tipoID"), tipoID)
            .set(DSL.field("categoriaId"), categoriaId)
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
