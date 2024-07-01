package com.inventarios.handler.atributo;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributo.services.BusquedaPorIdAtributoAbstractHandler;
import java.sql.SQLException;

import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public class BusquedaPorIdAtributoHandler extends BusquedaPorIdAtributoAbstractHandler {

  @Override
  /*protected Result<Record> busquedaPorArticuloId(String articuloId) throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(ATRIBUTO_TABLE)
            .where(ATRIBUTO_ARTICULO_ID.eq(Long.parseLong(articuloId)))
            .fetch();
  }*/
  protected Result<Record5<Long, Long, Long, Long, Long>> busquedaPorArticuloId(String articuloId)
          throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select(
                    ATRIBUTO_ID, ATRIBUTO_RESPONSABLE_ID, ATRIBUTO_ARTICULO_ID,
                    ATRIBUTO_TIPO_ID, ATRIBUTO_GRUPO_ID
            )
            .from(ATRIBUTO_TABLE)
            .where(ATRIBUTO_ARTICULO_ID.eq(Long.parseLong(articuloId)))
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
}