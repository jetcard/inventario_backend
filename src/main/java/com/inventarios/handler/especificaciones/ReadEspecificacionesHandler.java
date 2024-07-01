package com.inventarios.handler.especificaciones;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.especificaciones.services.ReadEspecificacionesAbstractHandler;
import org.jooq.*;
import org.jooq.impl.DSL;
import java.sql.SQLException;
import static org.jooq.impl.DSL.field;
public class ReadEspecificacionesHandler extends ReadEspecificacionesAbstractHandler {
  protected Result<Record3<Long, Long, String>> read(long custodioId, long articuloId, long tipoId, long categoriaId) throws SQLException {

    DSLContext dsl = RDSConexion.getDSL();
    Condition condition = DSL.trueCondition();

    if (custodioId > 0) {
      condition = condition.and(field("atributo.custodioid").eq(custodioId));
    }
    if (articuloId > 0) {
      condition = condition.and(field("atributo.articuloid").eq(articuloId));
    }
    if (tipoId > 0) {
      condition = condition.and(field("atributo.tipoid").eq(tipoId));
    }
    if (categoriaId > 0) {
      condition = condition.and(field("atributo.categoriaid").eq(categoriaId));
    }

    return dsl.select(
                    ATRIBUTOS_ID,
                    ATRIBUTOS_ATRIBUTOID,
                    ATRIBUTOS_NOMBREATRIBUTO
            )
            .from(ATRIBUTOS_TABLE)
            .join(ATRIBUTO_TABLE)
            .on(ATRIBUTO_ID.eq(ATRIBUTOS_ATRIBUTOID))
            //.on(field("atributo.id").eq(field("atributos.atributoid")))
            .where(condition)
            .fetch();
  }
  /*
  protected Result<Record> read(String custodioId, String articuloId, String tipoId, String categoriaId) throws SQLException {

    DSLContext dsl = RDSConexion.getDSL();
    Condition condition = DSL.trueCondition();

    if (custodioId != null && !custodioId.isEmpty()) {
      condition = condition.and(field("especifico.custodioId").eq(custodioId));
    }
    if (articuloId != null && !articuloId.isEmpty()) {
      condition = condition.and(field("especifico.articuloId").eq(articuloId));
    }
    if (tipoId != null && !tipoId.isEmpty()) {
      condition = condition.and(field("especifico.tipoId").eq(tipoId));
    }
    if (categoriaId != null && !categoriaId.isEmpty()) {
      condition = condition.and(field("especifico.categoriaId").eq(categoriaId));
    }

    return dsl.select()
            .from(ACTIVO_TABLE)
            .join(ESPECIFICACIONES_TABLE).on(field("especificaciones.especificoid").eq(field("especifico.id")))
            .where(condition)
            .fetch();*/
  }
/*
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
  protected String mostrarArticulo(Long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    Record record = dsl.select(ARTICULO_TABLE_COLUMNA)
            .from(ARTICULO_TABLE)
            .where(DSL.field("id", Long.class).eq(id))
            .fetchOne();
    return record != null ? record.getValue(ARTICULO_TABLE_COLUMNA) : null;
  }  */
