package com.inventarios.handler.activos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.activos.services.UpdateActivoAbstractHandler;
import org.jooq.impl.DSL;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.SQLException;

public class UpdateActivoHandler extends UpdateActivoAbstractHandler {
  protected void update(Long id, String codinventario, String modelo, String marca, String nroSerie,
                        Date fechaingreso, BigDecimal importe,
                        String moneda, Long responsableID, Long tipoID, Long grupoID,
                        Long articuloID,
                        Long proveedorID) throws SQLException {
    var dsl = RDSConexion.getDSL();
    //DSLContext dsl = DependencyFactory.getDSL();
    dsl.update(ACTIVO_TABLE)
      .set(DSL.field("codinventario"), codinventario)
      .set(DSL.field("modelo"), modelo)
      .set(DSL.field("marca"), marca)
      .set(DSL.field("nroSerie"), nroSerie)
      .set(DSL.field("fechaingreso"), fechaingreso)
      .set(DSL.field("importe"), importe)
      .set(DSL.field("moneda"), moneda)
      .set(DSL.field("responsableId"), responsableID)
      .set(DSL.field("tipoId"), tipoID)
      .set(DSL.field("grupoId"), grupoID)
      .set(DSL.field("articuloId"), articuloID)
      .set(DSL.field("proveedorId"), proveedorID)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
