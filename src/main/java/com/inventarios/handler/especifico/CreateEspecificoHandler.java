package com.inventarios.handler.especifico;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.especifico.services.CreateEspecificoAbstractHandler;
import com.inventarios.model.Especifico;
import com.inventarios.model.Especificos;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import java.sql.SQLException;
import java.util.List;

public class CreateEspecificoHandler extends CreateEspecificoAbstractHandler {

  @Override
  public int getEspecificoID(Especifico especifico) throws SQLException {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      int especificoid = dsl.transactionResult(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);
        return insertEspecifico(transactionalDsl, especifico);
      });
      return especificoid;
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
  }


  /*
  public void save(Especifico especifico, List<Especificos> especificosList) throws SQLException {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        long especificoid = especifico.getId();
        especifico.setId(especificoid);

        for (Especificos especificos : especificosList) {
          especificos.setEspecificoid(especifico.getId());
          //especificos.setEspecifico(especifico);
          ///especificos.setEspecifico(null); // Rompe la referencia cíclica
          insertEspecificos(transactionalDsl, especificos);
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
      throw new SQLException("Error saving especifico and especificos", e);
    }
  }*/

  @Override
  public void save(Especifico especifico, List<Especificos> especificosList) throws SQLException{
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        long especificoid = especifico.getId();//updateEspecifico(transactionalDsl, especifico);
        System.out.println("especificoid = "+especifico.getId());
        especifico.setId(especificoid);
        System.out.println("WHERE especificoid = "+especifico.getId());
        System.out.println("WHERE getResponsable = "+especifico.getResponsable());
        System.out.println("WHERE getArticulo = "+especifico.getArticulo());
        System.out.println("WHERE getEspecificos = "+especifico.getEspecificos().get(0));
        //System.out.println("WHERE especificoid = "+especificoid);

        for (Especificos especificos : especificosList) {
          especificos.setEspecificoid(especifico.getId());
          //especificos.setEspecifico(especifico);
          //especificos.setEspecifico(new Especifico(especifico.getId(), especifico.getResponsable(), especifico.getArticulo()));
          //especificos.setEspecifico(especifico); // Establecer la relación con el Especifico padre
          System.out.println("especificos.getEspecificoid() para insertEspecificos = "+especificos.getEspecificoid());
          insertEspecificos(transactionalDsl, especificos);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }

  ///@Override
  /*public void save1xx(Especifico especifico, List<Especificos> especificosList) {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        /*insertEspecifico(transactionalDsl, especifico);
        System.out.println("WHERE especificoid = "+especifico.getId());
        System.out.println("WHERE getResponsable = "+especifico.getResponsable());
        System.out.println("WHERE getArticulo = "+especifico.getArticulo());
        System.out.println("WHERE getEspecificos = "+especifico.getEspecificos().get(0));
        //System.out.println("WHERE especificoid = "+especificoid);* /

        for (Especificos especificos : especificosList) {
          especificos.setEspecifico(especifico);
          //especificos.setEspecifico(new Especifico(especifico.getId(), especifico.getResponsable(), especifico.getArticulo()));
          //especificos.setEspecifico(especifico); // Establecer la relación con el Especifico padre
          insertEspecificos(transactionalDsl, especificos);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }*/

  private int insertEspecifico(DSLContext dsl, Especifico especifico) {
    return dsl.insertInto(ESPECIFICO_TABLE)
            .set(DSL.field("responsableid"), especifico.getResponsable().getId())
            .set(DSL.field("articuloid"), especifico.getArticulo().getId())
            .returningResult(DSL.field("id"))
            .fetchOne()
            .getValue(DSL.field("id", Integer.class));
  }

  private int updateEspecifico(DSLContext dsl, Especifico especifico) {
    return dsl.update(ESPECIFICO_TABLE)
          .set(DSL.field("responsableid"), especifico.getResponsable().getId())
          .set(DSL.field("articuloid"), especifico.getArticulo().getId())
          .where(DSL.field("id").eq(especifico.getId())) // Especificar la condición de actualización
          .execute(); // Ejecutar la actualización y devolver el número de filas afectadas
}

  /*private void insertEspecificox(DSLContext dsl, Especifico especifico) {
    dsl.insertInto(ESPECIFICO_TABLE)
            .set(DSL.field("responsableId"), especifico.getResponsable().getId())
            .set(DSL.field("articuloId"), especifico.getArticulo().getId())
            .execute();
  }*/

  private void insertEspecificos(DSLContext dsl, Especificos especificos) {
    dsl.insertInto(ESPECIFICOS_TABLE)
            //.set(DSL.field("especificoid"), especificos.getEspecifico().getId())
            .set(DSL.field("especificoid"), especificos.getEspecificoid())
            .set(DSL.field("nombreespecifico"), especificos.getNombreespecifico().toUpperCase())
            .execute();
  }

  /*public void save(Especifico especifico, List<Especificos> especificosList) {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long especificoId = insertEspecifico(transactionalDsl, especifico);

        for (Especificos especificoItem : especificosList) {
          // Establecer la relación con el especifico usando el constructor que acepta el ID del especifico
          especificoItem.setEspecifico(new Especifico(especificoId));
          insertEspecificos(transactionalDsl, especificoItem);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }

  private Long insertEspecifico(DSLContext dsl, Especifico especifico) {
    return dsl.insertInto(ESPECIFICO_TABLE)
            .set(DSL.field("responsableId"), especifico.getResponsable().getId())
            .set(DSL.field("articuloId"), especifico.getArticulo().getId())
            .returningResult(DSL.field("especificoId"))
            .fetchOne()
            .getValue(DSL.field("especificoId", Long.class));
  }

  private void insertEspecificos(DSLContext dsl, Especificos especificos) {
    dsl.insertInto(ESPECIFICOS_TABLE)
            .set(DSL.field("especificoId"), especificos.getEspecifico().getId())
            .set(DSL.field("nombreespecifico"), especificos.getNombreespecifico())
            .set(DSL.field("descripespecifico"), especificos.getDescripespecifico())
            .execute();
  }*/
  /*public void save(Especifico especifico, List<Especificos> especificosList) {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long especificoId = insertEspecifico(transactionalDsl, especifico);

        for (Especificos especificoItem : especificosList) {
          // Establecer la relación con el especifico usando el constructor que acepta el ID del especifico
          especificoItem.setEspecifico(new Especifico(especificoId));
          insertEspecificos(transactionalDsl, especificoItem);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }

  private Long insertEspecifico(DSLContext dsl, Especifico especifico) {
    return dsl.insertInto(ESPECIFICO_TABLE)
            .set(DSL.field("responsableId"), especifico.getResponsable().getId())
            .set(DSL.field("articuloId"), especifico.getArticulo().getId())
            .returningResult(DSL.field("especificoId"))
            .fetchOne()
            .getValue(DSL.field("especificoId", Long.class));
  }

  private void insertEspecificos(DSLContext dsl, Especificos especificos) {
    dsl.insertInto(ESPECIFICOS_TABLE)
            .set(DSL.field("especificoId"), especificos.getEspecifico().getId())
            .set(DSL.field("especifico"), especificos.getEspecifico())
            .execute();
  }*/
}

  /*public void save(Especifico especifico, List<Especificos> especificosList) {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long especificoId = insertEspecifico(transactionalDsl, especifico);

        for (Especificos especificos : especificosList) {
          especificos.setEspecifico(new Especifico(especificoId)); // Establecer la relación con el especifico
          insertEspecificos(transactionalDsl, especificos);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }

  private Long insertEspecifico(DSLContext dsl, Especifico especifico) {
    return dsl.insertInto(ESPECIFICO_TABLE)
            .set(DSL.field("responsableId"), especifico.getResponsable().getId())
            .set(DSL.field("articuloId"), especifico.getArticulo().getId())
            .returningResult(DSL.field("id"))
            .fetchOne()
            .getValue(DSL.field("id", Long.class));
  }

  private void insertEspecificos(DSLContext dsl, Especificos especificos) {
    dsl.insertInto(ESPECIFICOS_TABLE)
            .set(DSL.field("especificoId"), especificos.getEspecifico().getId())
            .set(DSL.field("especifico"), especificos.getEspecifico())
            .execute();
  }*/
  /*public void save(Especifico especifico, List<Especificos> especificosList) {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long especificoId = insertEspecifico(transactionalDsl, especifico);

        for (Especificos especificos : especificosList) {
          insertEspecificos(transactionalDsl, especificoId, especificos);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }

  private Long insertEspecifico(DSLContext dsl, Especifico especifico) {
    return dsl.insertInto(ESPECIFICO_TABLE)
            .set(DSL.field("responsableId"), especifico.getResponsable().getId())
            .set(DSL.field("articuloId"), especifico.getArticulo().getId())
            .returningResult(DSL.field("id"))
            .fetchOne()
            .getValue(DSL.field("id", Long.class));
  }

  private void insertEspecificos(DSLContext dsl, Long especificoId, Especificos especificos) {
    dsl.insertInto(ESPECIFICOS_TABLE)
            .set(DSL.field("especificoId"), especificoId)
            .set(DSL.field("descripcion"), especificos.getDescripcion())
            .execute();
  }*/
  /*protected void save(Especifico especifico, List<Especificos> especificosList) {
    DSLContext dsl = DatabaseConnection.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long especificoId = insertEspecifico(transactionalDsl, especifico);

        for (Especificos especificos : especificosList) {
          insertEspecificos(transactionalDsl, especificoId, especificos);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }

  private Long insertEspecifico(DSLContext dsl, Especifico especifico) {
    return dsl.insertInto(ESPECIFICO_TABLE)
            .set(DSL.field("responsableId"), especifico.getResponsable().getId())
            .set(DSL.field("articuloId"), especifico.getArticulo().getId())
            .returningResult(DSL.field("id"))
            .fetchOne()
            .getValue(DSL.field("id", Long.class));
  }

  private void insertEspecificos(DSLContext dsl, Long especificoId, Especificos especificos) {
    dsl.insertInto(ESPECIFICOS_TABLE)
            .set(DSL.field("especificoId"), especificoId)
            .set(DSL.field("descripcion"), especificos.getDescripcion())
            .execute();
  }*/
  /*@Override
  protected void save(Especifico especifico, List<Especificos> especificosList) {
    DSLContext dsl = DatabaseConnection.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long especificoId = transactionalDsl.insertInto(ESPECIFICO_TABLE)
                .set(DSL.field("responsableId"), especifico.getResponsable().getId())
                .set(DSL.field("articuloId"), especifico.getArticulo().getId())
                .returningResult(DSL.field("id"))
                .fetchOne()
                .getValue(DSL.field("id", Long.class));

        for (Especificos especificos : especificosList) {
          transactionalDsl.insertInto(ESPECIFICOS_TABLE)
                  .set(DSL.field("especificoId"), especificoId)
                  .set(DSL.field("descripcion"), especificos.getDescripcion())
                  .execute();
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }*/

  /*protected void save(Especifico especifico, Especificos especificos, Long grupoID) {
    DSLContext dsl = DatabaseConnection.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long especificoId = transactionalDsl.insertInto(ESPECIFICO_TABLE)
                .set(DSL.field("responsableId"), especifico.getResponsable().getId())
                .set(DSL.field("articuloId"), especifico.getArticulo().getId())
                .returningResult(DSL.field("id"))
                .fetchOne()
                .getValue(DSL.field("id", Long.class));

        transactionalDsl.insertInto(ESPECIFICOS_TABLE)
                .set(DSL.field("especificoId"), especificoId)
                .set(DSL.field("descripcion"), especificos.getDescripcion())
                .execute();
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }*/
  /*protected void save(Especifico especifico, Especificos especificos, Long grupoID) {
    DSLContext dsl = DatabaseConnection.getDSL();
    Long especificoId = dsl.insertInto(ESPECIFICO_TABLE)
            .set(DSL.field("responsableId"), especifico.getResponsable().getId())
            .set(DSL.field("articuloId"), especifico.getArticulo().getId())
            .returningResult(DSL.field("id"))
            .fetchOne()
            .getValue(DSL.field("id", Long.class));
    dsl.insertInto(ESPECIFICOS_TABLE)
            .set(DSL.field("especificoId"), especificoId)
            .set(DSL.field("descripcion"), especificos.getDescripcion())
            .execute();
  }*/

    /*
    var dsl = DatabaseConnection.getDSL();
    dsl.insertInto(ESPECIFICO_TABLE)
            .set(DSL.field("responsable"), especifico.getResponsable())
            .set(DSL.field("articulo"), especifico.getArticulo())
            .set(DSL.field("grupoId"), grupoID)
            .execute();*/