package com.inventarios.handler.activos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.activos.services.UpdateActivoAbstractHandler;
import com.inventarios.model.Activo;
import org.jooq.impl.DSL;
import java.sql.SQLException;

public class UpdateActivoHandler extends UpdateActivoAbstractHandler {
  protected void update(Long id, Activo activo, Long responsableID, Long tipoID, Long grupoID,
                        Long articuloID,
                        Long proveedorID) throws SQLException {
    var dsl = RDSConexion.getDSL();
    //DSLContext dsl = DependencyFactory.getDSL();
    dsl.update(ACTIVO_TABLE)
      .set(DSL.field("codinventario"), activo.getCodinventario().toUpperCase())
      .set(DSL.field("modelo"), activo.getModelo().toUpperCase())
      .set(DSL.field("marca"), activo.getMarca().toUpperCase())
      .set(DSL.field("nroserie"), activo.getNroserie().toUpperCase())
      .set(DSL.field("fechaingreso"), activo.getFechaingreso())
      .set(DSL.field("fechaingresostr"), activo.getFechaingresostr())
      .set(DSL.field("importe"), activo.getImporte())
      .set(DSL.field("moneda"), activo.getMoneda())
      .set(DSL.field("responsableId"), responsableID)
      .set(DSL.field("tipoId"), tipoID)
      .set(DSL.field("grupoId"), grupoID)
      .set(DSL.field("articuloId"), articuloID)
      .set(DSL.field("proveedorId"), proveedorID)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }
}
