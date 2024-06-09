package com.inventarios.handler.atributo;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributo.services.ReadAtributoAbstractHandler;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Record8;
import org.jooq.Result;
import org.jooq.impl.DSL;

public class ReadAtributoHandler extends ReadAtributoAbstractHandler {
  protected Result<Record8<Long, Long, Long, Long, Long, Long, Long, String>> read() throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select(
                    ATRIBUTO_ID, ATRIBUTO_RESPONSABLE_ID, ATRIBUTO_ARTICULO_ID,
                    ATRIBUTO_TIPO_ID, ATRIBUTO_GRUPO_ID,
                    ATRIBUTOS_ID, ATRIBUTOS_ATRIBUTOID, ATRIBUTOS_NOMBREATRIBUTO
            )
            .from(ATRIBUTO_TABLE)
            .leftJoin(ATRIBUTOS_TABLE)
            .on(ATRIBUTO_ID.eq(ATRIBUTOS_ATRIBUTOID))
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