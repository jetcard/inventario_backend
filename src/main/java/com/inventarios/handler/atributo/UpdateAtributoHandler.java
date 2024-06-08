package com.inventarios.handler.atributo;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributo.services.UpdateAtributoAbstractHandler;
import com.inventarios.model.Articulo;
import com.inventarios.model.Grupo;
import com.inventarios.model.Responsable;
import java.sql.SQLException;

import com.inventarios.model.Tipo;
import org.jooq.impl.DSL;

public class UpdateAtributoHandler extends UpdateAtributoAbstractHandler {

  protected void update(Long id,
                        Responsable responsable,
                        Articulo articulo,
                        Tipo tipo,
                        Grupo grupo) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.update(ATRIBUTO_TABLE)
      .set(DSL.field("responsable"), responsable)
      .set(DSL.field("articulo"), articulo)
            .set(DSL.field("tipo"), tipo)
            .set(DSL.field("grupo"), grupo)
            .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
