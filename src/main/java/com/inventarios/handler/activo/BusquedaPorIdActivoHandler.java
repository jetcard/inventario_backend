package com.inventarios.handler.activo;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.activo.services.BusquedaPorIdActivoAbstractHandler;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import java.sql.SQLException;
import java.time.LocalDate;
import static org.jooq.impl.DSL.field;

public class BusquedaPorIdActivoHandler extends BusquedaPorIdActivoAbstractHandler {

  protected Result<Record18<Long, Long, Long, Long, Long, Long, Long, Long, String, String, String, String, String, LocalDate, String, String, String, String>> busquedaActivo(String codinventario, String modelo, String marca, String nroSerie, LocalDate fechaCompraDesde, LocalDate fechaCompraHasta) throws SQLException {
    DSLContext dsl = RDSConexion.getDSL();
    Condition condition = DSL.trueCondition();
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
    return dsl.select(
                    ACTIVO_ID,
                    ACTIVO_RESPONSABLE_ID,
                    ACTIVO_ARTICULO_ID,
                    ACTIVO_TIPO_ID,
                    ACTIVO_GRUPO_ID,
                    ACTIVO_PROVEEDOR_ID,
                    ESPECIFICACIONES_ID,
                    ESPECIFICACIONES_ESPECIFICOID,
                    ESPECIFICACIONES_NOMBREESPECIFICO,
                    ACTIVO_CODINVENTARIO,
                    ACTIVO_MODELO,
                    ACTIVO_MARCA,
                    ACTIVO_NROSERIE,
                    ACTIVO_FECHAINGRESO,
                    ACTIVO_FECHAINGRESOSTR,
                    ACTIVO_MONEDA,
                    ACTIVO_IMPORTE,
                    ACTIVO_DESCRIPCION
            )
            .from(ACTIVO_TABLE)
            .leftJoin(ESPECIFICACIONES_TABLE)
            .on(ACTIVO_ID.eq(ESPECIFICACIONES_ESPECIFICOID))
            .where(condition)
            .fetch();
  }

  @Override
  protected String mostrarCustodio(Long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    Record record = dsl.select(RESPONSABLE_TABLE_COLUMNA)
            .from(RESPONSABLE_TABLE)
            .where(DSL.field("id", Long.class).eq(id))
            .fetchOne();
    return record != null ? record.getValue(RESPONSABLE_TABLE_COLUMNA) : null;
  }
  @Override
  protected String mostrarArticulo(Long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    Record record = dsl.select(ARTICULO_TABLE_COLUMNA)
            .from(ARTICULO_TABLE)
            .where(DSL.field("id", Long.class).eq(id))
            .fetchOne();
    return record != null ? record.getValue(ARTICULO_TABLE_COLUMNA) : null;
  }
  @Override
  protected String mostrarTipoBien(Long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    Record record = dsl.select(TIPO_TABLE_COLUMNA)
            .from(TIPO_TABLE)
            .where(DSL.field("id", Long.class).eq(id))
            .fetchOne();
    return record != null ? record.getValue(TIPO_TABLE_COLUMNA) : null;
  }
  @Override
  protected String mostrarCategoria(Long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    Record record = dsl.select(GRUPO_TABLE_COLUMNA)
            .from(GRUPO_TABLE)
            .where(DSL.field("id", Long.class).eq(id))
            .fetchOne();
    return record != null ? record.getValue(GRUPO_TABLE_COLUMNA) : null;
  }
  @Override
  protected String mostrarProveedor(Long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    Record record = dsl.select(PROVEEDOR_TABLE_COLUMNA)
            .from(PROVEEDOR_TABLE)
            .where(DSL.field("id", Long.class).eq(id))
            .fetchOne();
    return record != null ? record.getValue(PROVEEDOR_TABLE_COLUMNA) : null;
  }
}
