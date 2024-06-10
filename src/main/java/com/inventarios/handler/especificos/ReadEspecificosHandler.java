package com.inventarios.handler.especificos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.especificos.services.ReadEspecificosAbstractHandler;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;

public class ReadEspecificosHandler extends ReadEspecificosAbstractHandler {
  protected Result<Record> read() throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select().from(ESPECIFICOS_TABLE).fetch();
  }

}