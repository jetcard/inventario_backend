package com.inventarios.handler.parametros;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.parametros.services.ReadParametroAbstractHandler;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;
public class ReadParametroHandler extends ReadParametroAbstractHandler {
  protected Result<Record> read() throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select().from(PARAMETRO_TABLE).fetch();
  }

}