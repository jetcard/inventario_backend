package com.inventarios.handler.especificos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.especificos.services.ReadEspecificosAbstractHandler;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

import java.sql.SQLException;

import static org.jooq.impl.DSL.field;

public class ReadEspecificosHandler extends ReadEspecificosAbstractHandler {
  protected Result<Record3<Long, Long, String>> read(long responsableId, long articuloId, long tipoId, long grupoId) throws SQLException {

    DSLContext dsl = RDSConexion.getDSL();
    Condition condition = DSL.trueCondition();

    if (responsableId > 0) {
      condition = condition.and(field("atributo.responsableid").eq(responsableId));
    }
    if (articuloId > 0) {
      condition = condition.and(field("atributo.articuloid").eq(articuloId));
    }
    if (tipoId > 0) {
      condition = condition.and(field("atributo.tipoid").eq(tipoId));
    }
    if (grupoId > 0) {
      condition = condition.and(field("atributo.grupoid").eq(grupoId));
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
  protected Result<Record> read(String responsableId, String articuloId, String tipoId, String grupoId) throws SQLException {

    DSLContext dsl = RDSConexion.getDSL();
    Condition condition = DSL.trueCondition();

    if (responsableId != null && !responsableId.isEmpty()) {
      condition = condition.and(field("especifico.responsableId").eq(responsableId));
    }
    if (articuloId != null && !articuloId.isEmpty()) {
      condition = condition.and(field("especifico.articuloId").eq(articuloId));
    }
    if (tipoId != null && !tipoId.isEmpty()) {
      condition = condition.and(field("especifico.tipoId").eq(tipoId));
    }
    if (grupoId != null && !grupoId.isEmpty()) {
      condition = condition.and(field("especifico.grupoId").eq(grupoId));
    }

    return dsl.select()
            .from(ESPECIFICO_TABLE)
            .join(ESPECIFICOS_TABLE).on(field("especificos.especificoid").eq(field("especifico.id")))
            .where(condition)
            .fetch();*/
  }
/*
  @Override
  protected String mostrarResponsable(Long id) throws SQLException {
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
  protected String mostrarGrupo(Long id) throws SQLException {
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
