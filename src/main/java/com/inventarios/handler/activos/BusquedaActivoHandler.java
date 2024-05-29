package com.inventarios.handler.activos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.activos.services.BusquedaActivoAbstractHandler;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import java.time.LocalDate;
import static org.jooq.impl.DSL.field;

public class BusquedaActivoHandler extends BusquedaActivoAbstractHandler {

  @Override
  protected Result<Record> busquedaActivo(String responsable, String codinventario, String modelo, String marca, String nroSerie, LocalDate fechaCompraDesde, LocalDate fechaCompraHasta) {
    DSLContext dsl = RDSConexion.getDSL();
    Condition condition = DSL.trueCondition();
    if (responsable != null && !responsable.isEmpty()) {
      condition = condition.and(RESPONSABLE_TABLE.field("arearesponsable").likeIgnoreCase("%" + responsable + "%"));
    }
    if (codinventario != null && !codinventario.isEmpty()) {
      condition = condition.and(field("codinventario").likeIgnoreCase("%" + codinventario + "%"));
    }
    if (modelo != null && !modelo.isEmpty()) {
      condition = condition.and(field("modelo").likeIgnoreCase("%" + modelo + "%"));
    }
    if (marca != null && !marca.isEmpty()) {
      condition = condition.and(field("marca").likeIgnoreCase("%" + marca + "%"));
    }
    if (nroSerie != null && !nroSerie.isEmpty()) {
      condition = condition.and(field("nroserie").likeIgnoreCase("%" + nroSerie + "%"));
    }
    if (fechaCompraDesde != null && fechaCompraHasta != null) {
      condition = condition.and(field("fechaingreso").between(fechaCompraDesde).and(fechaCompraHasta));
    }
    return dsl.select()
            .from(ACTIVO_TABLE)
            .join(RESPONSABLE_TABLE).on(field("responsableId").eq(RESPONSABLE_TABLE.field("id")))
            .where(condition)
            .fetch();
  }

  @Override
  protected String mostrarResponsable(Long id) {
    var dsl = RDSConexion.getDSL();
    Record record = dsl.select(RESPONSABLE_TABLE_COLUMNA)
            .from(RESPONSABLE_TABLE)
            .where(DSL.field("id", Long.class).eq(id))
            .fetchOne();
    return record != null ? record.getValue(RESPONSABLE_TABLE_COLUMNA) : null;
  }

  @Override
  protected String mostrarTipoBien(Long id) {
    var dsl = RDSConexion.getDSL();
    Record record = dsl.select(TIPO_TABLE_COLUMNA)
            .from(TIPO_TABLE)
            .where(DSL.field("id", Long.class).eq(id))
            .fetchOne();
    return record != null ? record.getValue(TIPO_TABLE_COLUMNA) : null;
  }

  @Override
  protected String mostrarGrupo(Long id) {
    var dsl = RDSConexion.getDSL();
    Record record = dsl.select(GRUPO_TABLE_COLUMNA)
            .from(GRUPO_TABLE)
            .where(DSL.field("id", Long.class).eq(id))
            .fetchOne();
    return record != null ? record.getValue(GRUPO_TABLE_COLUMNA) : null;
  }

  @Override
  protected String mostrarArticulo(Long id) {
    var dsl = RDSConexion.getDSL();
    Record record = dsl.select(ARTICULO_TABLE_COLUMNA)
            .from(ARTICULO_TABLE)
            .where(DSL.field("id", Long.class).eq(id))
            .fetchOne();
    return record != null ? record.getValue(ARTICULO_TABLE_COLUMNA) : null;
  }

}