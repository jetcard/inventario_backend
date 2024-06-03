package com.inventarios.handler.grupos;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.grupos.services.ReadGrupoAbstractHandler;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;

public class ReadGrupoHandler extends ReadGrupoAbstractHandler {
  protected Result<Record> read() throws SQLException{
    var dsl = RDSConexion.getDSL();
    return dsl.select().from(GRUPO_TABLE).fetch();
  }

}