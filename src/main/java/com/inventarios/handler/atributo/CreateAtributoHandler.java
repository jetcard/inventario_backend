package com.inventarios.handler.atributo;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributo.services.CreateAtributoAbstractHandler;
import com.inventarios.model.Atributo;
import com.inventarios.model.Atributos;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record8;
import org.jooq.Result;
import org.jooq.impl.DSL;
import java.sql.SQLException;
import java.util.List;

public class CreateAtributoHandler extends CreateAtributoAbstractHandler {

  @Override
  public int getAtributoID(Atributo atributo) throws SQLException {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      int atributoid = dsl.transactionResult(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);
        return insertAtributo(transactionalDsl, atributo);
      });
      return atributoid;
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
  }

  @Override
  public void save(Atributo atributo, List<Atributos> atributosList) throws SQLException{
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);
        long atributoid = atributo.getId();
        System.out.println("atributoid = "+ atributo.getId());
        atributo.setId(atributoid);
        for (Atributos atributos : atributosList) {
          atributos.setAtributoid(atributo.getId());
          System.out.println("atributos.getAtributoid() para insertAtributos = "+atributos.getAtributoid());
          insertAtributos(transactionalDsl, atributos);
        }
      });
    } catch (Exception e) {
    }
  }

  private int insertAtributo(DSLContext dsl, Atributo atributo) {
    return dsl.insertInto(ATRIBUTO_TABLE)
            .set(DSL.field("custodioid"), atributo.getCustodio().getId())
            .set(DSL.field("articuloid"), atributo.getArticulo().getId())
            .set(DSL.field("categoriaid"), atributo.getCategoria().getId())
            .set(DSL.field("tipoid"), atributo.getTipo().getId())
            .returningResult(DSL.field("id"))
            .fetchOne()
            .getValue(DSL.field("id", Integer.class));
  }

  private void insertAtributos(DSLContext dsl, Atributos atributos) {
    dsl.insertInto(ATRIBUTOS_TABLE)
            .set(DSL.field("atributoid"), atributos.getAtributoid())
            .set(DSL.field("nombreatributo"), atributos.getNombreatributo().toUpperCase())
            .execute();
  }

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