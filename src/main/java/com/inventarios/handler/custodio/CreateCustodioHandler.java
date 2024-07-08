package com.inventarios.handler.custodio;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.custodio.services.CreateCustodioAbstractHandler;
import java.sql.SQLException;
import java.util.List;

import com.inventarios.model.Atributo;
import com.inventarios.model.Atributos;
import com.inventarios.model.Custodio;
import com.inventarios.model.Proveedor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

public class CreateCustodioHandler extends CreateCustodioAbstractHandler {

  protected void save(String arearesponsable, String nombresyapellidos) throws SQLException {
    var dsl = RDSConexion.getDSL();
    dsl.insertInto(RESPONSABLE_TABLE)
      .set(DSL.field("arearesponsable"), arearesponsable)
      .set(DSL.field("nombresyapellidos"), nombresyapellidos)
      .execute();
  }
/*
  @Override
  public void save(Custodio custodio, List<Proveedor> proveedoresList) throws SQLException{
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);
        long custodioid = custodio.getId();
        System.out.println("custodioid = "+ custodioid.getId());
        custodio.setId(custodioid);
        for (Proveedor proveedores : proveedoresList) {
          proveedores.setCustodioid(custodio.getId());
          System.out.println("proveedores.getCustodioid() para insertProveedores = "+proveedores.getCustodioid());
          insertAtributos(transactionalDsl, atributos);
        }
      });
    } catch (Exception e) {
    }
  }*/

}
