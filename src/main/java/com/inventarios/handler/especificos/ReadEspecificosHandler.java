package com.inventarios.handler.especificos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.especificos.services.ReadEspecificosAbstractHandler;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.sql.SQLException;

import static org.jooq.impl.DSL.field;

public class ReadEspecificosHandler extends ReadEspecificosAbstractHandler {

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
            .fetch();
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
}