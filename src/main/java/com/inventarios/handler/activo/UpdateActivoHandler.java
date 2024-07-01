package com.inventarios.handler.activo;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.activo.services.UpdateActivoAbstractHandler;
import com.inventarios.model.Articulo;
import com.inventarios.model.Categoria;
import com.inventarios.model.Custodio;
import java.sql.SQLException;
import com.inventarios.model.Tipo;
import org.jooq.impl.DSL;
public class UpdateActivoHandler extends UpdateActivoAbstractHandler {

  protected void update(Long id,
                        Custodio custodio,
                        Articulo articulo,
                        Tipo tipo,
                        Categoria categoria) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.update(ACTIVO_TABLE)
      .set(DSL.field("responsable"), custodio)
      .set(DSL.field("articulo"), articulo)
            .set(DSL.field("tipo"), tipo)
            .set(DSL.field("grupo"), categoria)
            .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}