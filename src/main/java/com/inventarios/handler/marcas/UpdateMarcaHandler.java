package com.inventarios.handler.marcas;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.marcas.services.UpdateMarcaAbstractHandler;
import java.sql.SQLException;
import java.util.Optional;
import com.inventarios.model.Marca;
import org.jooq.impl.DSL;

public class UpdateMarcaHandler extends UpdateMarcaAbstractHandler {
  protected Optional<Marca> marcaSearch(Long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(MARCA_TABLE)
            .where(DSL.field("id", Long.class).eq(id))
            .fetchOptionalInto(Marca.class);
  }
  protected void update(Long id, String nombre, String descripcion) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.update(MARCA_TABLE)
      .set(DSL.field("nombre"), nombre)
      .set(DSL.field("descripcion"), descripcion)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
