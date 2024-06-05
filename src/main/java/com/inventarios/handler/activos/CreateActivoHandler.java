package com.inventarios.handler.activos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.activos.services.CreateActivoAbstractHandler;
import com.inventarios.model.Activo;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class CreateActivoHandler extends CreateActivoAbstractHandler {
  @Override
  protected void save(Activo activo, Long responsableID, Long tipoID, Long grupoID,
                      Long articuloID,
                      Long proveedorID) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(ACTIVO_TABLE)
            .set(DSL.field("codinventario"), activo.getCodinventario().toUpperCase())
            .set(DSL.field("modelo"), activo.getModelo().toUpperCase())
            .set(DSL.field("marca"), activo.getMarca().toUpperCase())
            .set(DSL.field("nroserie"), activo.getNroserie().toUpperCase())
            .set(DSL.field("fechaingreso"), activo.getFechaingreso())
            .set(DSL.field("importe"), activo.getImporte())
            .set(DSL.field("moneda"), activo.getMoneda())
            .set(DSL.field("responsableID"), responsableID)
            .set(DSL.field("tipoID"), tipoID)
            .set(DSL.field("grupoId"), grupoID)
            .set(DSL.field("articuloID"), articuloID)
            .set(DSL.field("proveedorID"), proveedorID)
            .execute();
  }
}
