package com.inventarios.handler.categoria;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.categoria.services.ReadCategoriaAbstractHandler;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;
public class ReadCategoriaHandler extends ReadCategoriaAbstractHandler {
  @Override
  protected Result<Record> read() throws SQLException{
    var dsl = RDSConexion.getDSL();
    return dsl.select().from(GRUPO_TABLE).fetch();
  }

}