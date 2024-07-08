package com.inventarios.handler.parametros;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.parametros.services.DeleteParametroAbstractHandler;
import java.sql.SQLException;
import java.util.Optional;

import com.inventarios.model.Parametro;
import org.jooq.impl.DSL;

public class DeleteParametroHandler extends DeleteParametroAbstractHandler {

  protected Optional<Parametro> parametroSearch(Long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(PARAMETRO_TABLE)
            .where(DSL.field("id", Long.class).eq(id))
            .fetchOptionalInto(Parametro.class);
  }
  protected void delete(long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.deleteFrom(PARAMETRO_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
  }

}
