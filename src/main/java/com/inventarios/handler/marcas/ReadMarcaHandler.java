package com.inventarios.handler.marcas;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.marcas.services.ReadMarcaAbstractHandler;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;
public class ReadMarcaHandler extends ReadMarcaAbstractHandler {
  protected Result<Record> read() throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select().from(MARCA_TABLE).fetch();
  }

}