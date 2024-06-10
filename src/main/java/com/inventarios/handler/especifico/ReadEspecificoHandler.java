package com.inventarios.handler.especifico;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.especifico.services.ReadEspecificoAbstractHandler;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Record8;
import org.jooq.Result;
import org.jooq.impl.DSL;

public class ReadEspecificoHandler extends ReadEspecificoAbstractHandler {
  protected Result<Record8<Long, Long, Long, Long, Long, Long, Long, String>> read() throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select(
                    ESPECIFICO_ID, ESPECIFICO_RESPONSABLE_ID, ESPECIFICO_ARTICULO_ID,
                    ESPECIFICO_TIPO_ID, ESPECIFICO_GRUPO_ID,
                    ESPECIFICOS_ID, ESPECIFICOS_ESPECIFICOID, ESPECIFICOS_NOMBREESPECIFICO
            )
            .from(ESPECIFICO_TABLE)
            .leftJoin(ESPECIFICOS_TABLE)
            .on(ESPECIFICO_ID.eq(ESPECIFICOS_ESPECIFICOID))
            .fetch();
  }

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
  protected String mostrarGrupo(Long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    Record record = dsl.select(GRUPO_TABLE_COLUMNA)
            .from(GRUPO_TABLE)
            .where(DSL.field("id", Long.class).eq(id))
            .fetchOne();
    return record != null ? record.getValue(GRUPO_TABLE_COLUMNA) : null;
  }

}