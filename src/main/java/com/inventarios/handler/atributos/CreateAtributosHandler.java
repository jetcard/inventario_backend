package com.inventarios.handler.atributos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributos.services.CreateAtributosAbstractHandler;
import com.inventarios.model.Atributos;
import java.sql.SQLException;
import org.jooq.impl.DSL;
public class CreateAtributosHandler extends CreateAtributosAbstractHandler {
  @Override
  protected void save(Atributos atributos, Long categoriaID) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(ATRIBUTOS_TABLE)
            //.set(DSL.field("descripatributo"), atributos.getDescripatributo())
            .set(DSL.field("nombreatributo"), atributos.getNombreatributo().toUpperCase())
            .set(DSL.field("categoriaId"), categoriaID)
            .execute();
  }
}
