package com.inventarios.handler.atributo;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributo.services.UpdateAtributoAbstractHandler;
import com.inventarios.model.Articulo;
import com.inventarios.model.Categoria;
import com.inventarios.model.Custodio;
import java.sql.SQLException;

import com.inventarios.model.Tipo;
import org.jooq.impl.DSL;

public class UpdateAtributoHandler extends UpdateAtributoAbstractHandler {

  protected void update(Long id,
                        Custodio custodio,
                        Articulo articulo,
                        Tipo tipo,
                        Categoria categoria) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.update(ATRIBUTO_TABLE)
      .set(DSL.field("responsable"), custodio)
      .set(DSL.field("articulo"), articulo)
            .set(DSL.field("tipo"), tipo)
            .set(DSL.field("grupo"), categoria)
            .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
