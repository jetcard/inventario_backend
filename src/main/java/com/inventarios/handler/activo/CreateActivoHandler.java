package com.inventarios.handler.activo;

import com.inventarios.core.RDSConexion;
import com.inventarios.handler.activo.services.CreateActivoAbstractHandler;
import com.inventarios.model.Activo;
import com.inventarios.model.Especificaciones;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import java.sql.SQLException;
import java.util.List;
public class CreateActivoHandler extends CreateActivoAbstractHandler {
  @Override
  public int getEspecificacionID(Activo activo) throws SQLException {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      int especificacionid = dsl.transactionResult(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);
        return insertActivo(transactionalDsl, activo);
      });
      return especificacionid;
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
  }
/*
  @RestController
  @RequestMapping("/api")
  public class AtributoController {

    @GetMapping("/getAtributos")
    public ResponseEntity<List<String>> getAtributos(
            @RequestParam int custodioId,
            @RequestParam int articuloId,
            @RequestParam int tipoId,
            @RequestParam int categoriaId) {

      List<String> atributos = new ArrayList<>();

      if (custodioId == 5 && articuloId == 5 && tipoId == 1 && categoriaId == 3) {
        atributos = Arrays.asList("ALTO", "ANCHO", "LARGO");
      } else {
        atributos = Arrays.asList("1", "2");
      }

      return ResponseEntity.ok(atributos);
    }
  }
*/
  /*
  public void save(Activo activo) throws SQLException{
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);
        long especificoid = activo.getId();//updateEspecifico(transactionalDsl, especifico);
        System.out.println("especificoid = "+ activo.getId());
        activo.setId(especificoid);
        for (Especificaciones especificaciones : especificacionesList) {
          especificaciones.setEspecificoid(especifico.getId());
          insertespecificaciones(transactionalDsl, especificaciones);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }*/

  @Override
  public void save(Activo activo, List<Especificaciones> especificacionesList) throws SQLException{
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);
        long especificacionid = activo.getId();//updateEspecifico(transactionalDsl, especifico);
        System.out.println("especificacionid = "+ activo.getId());
        activo.setId(especificacionid);
        for (Especificaciones especificaciones : especificacionesList) {
          especificaciones.setEspecificacionid(activo.getId());
          insertespecificaciones(transactionalDsl, especificaciones);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }

  private int insertActivo(DSLContext dsl, Activo activo) {
    return dsl.insertInto(ACTIVO_TABLE)
            .set(DSL.field("custodioid"), activo.getCustodio().getId())
            .set(DSL.field("articuloid"), activo.getArticulo().getId())
            .set(DSL.field("categoriaid"), activo.getCategoria().getId())
            .set(DSL.field("tipoid"), activo.getTipo().getId())
            .set(DSL.field("codinventario"), activo.getCodinventario().toUpperCase())
            .set(DSL.field("modelo"), activo.getModelo().toUpperCase())
            .set(DSL.field("marca"), activo.getMarca().toUpperCase())
            .set(DSL.field("nroserie"), activo.getNroserie().toUpperCase())
            .set(DSL.field("fechaingreso"), activo.getFechaingreso())
            .set(DSL.field("fechaingresostr"), activo.getFechaingresostr())
            .set(DSL.field("importe"), activo.getImporte())
            .set(DSL.field("moneda"), activo.getMoneda())
            .set(DSL.field("descripcion"), activo.getDescripcion().toUpperCase())
            .set(DSL.field("proveedorid"), activo.getProveedorId())
            .returningResult(DSL.field("id"))
            .fetchOne()
            .getValue(DSL.field("id", Integer.class));
  }

  private int updateEspecifico(DSLContext dsl, Activo activo) {
    return dsl.update(ACTIVO_TABLE)
          .set(DSL.field("custodioid"), activo.getCustodio().getId())
          .set(DSL.field("articuloid"), activo.getArticulo().getId())
          .set(DSL.field("categoriaid"), activo.getCategoria().getId())
          .set(DSL.field("tipoid"), activo.getTipo().getId())
          .where(DSL.field("id").eq(activo.getId())) // Especificar la condición de actualización
          .execute(); // Ejecutar la actualización y devolver el número de filas afectadas
}

  private void insertespecificaciones(DSLContext dsl, Especificaciones especificaciones) {
    dsl.insertInto(ESPECIFICACIONES_TABLE)
            //.set(DSL.field("especificoid"), especificaciones.getEspecifico().getId())
            .set(DSL.field("especificacionid"), especificaciones.getEspecificacionid())
            .set(DSL.field("nombreatributo"), especificaciones.getNombreatributo().toUpperCase())
            .set(DSL.field("descripcionatributo"), especificaciones.getDescripcionatributo().toUpperCase())
            .execute();
  }

    /*private void insertEspecificox(DSLContext dsl, Especifico especifico) {
    dsl.insertInto(ACTIVO_TABLE)
            .set(DSL.field("custodioId"), especifico.getResponsable().getId())
            .set(DSL.field("articuloId"), especifico.getArticulo().getId())
            .execute();
  }*/

  ///@Override
  /*public void save1xx(Especifico especifico, List<especificaciones> especificacionesList) {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        /*insertEspecifico(transactionalDsl, especifico);
        System.out.println("WHERE especificoid = "+especifico.getId());
        System.out.println("WHERE getResponsable = "+especifico.getResponsable());
        System.out.println("WHERE getArticulo = "+especifico.getArticulo());
        System.out.println("WHERE getespecificaciones = "+especifico.getespecificaciones().get(0));
        //System.out.println("WHERE especificoid = "+especificoid);* /

        for (especificaciones especificaciones : especificacionesList) {
          especificaciones.setEspecifico(especifico);
          //especificaciones.setEspecifico(new Especifico(especifico.getId(), especifico.getResponsable(), especifico.getArticulo()));
          //especificaciones.setEspecifico(especifico); // Establecer la relación con el Especifico padre
          insertespecificaciones(transactionalDsl, especificaciones);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }*/

  /*
  public void save(Especifico especifico, List<especificaciones> especificacionesList) throws SQLException {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        long especificoid = especifico.getId();
        especifico.setId(especificoid);

        for (especificaciones especificaciones : especificacionesList) {
          especificaciones.setEspecificoid(especifico.getId());
          //especificaciones.setEspecifico(especifico);
          ///especificaciones.setEspecifico(null); // Rompe la referencia cíclica
          insertespecificaciones(transactionalDsl, especificaciones);
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
      throw new SQLException("Error saving especifico and especificaciones", e);
    }
  }*/

  /*public void save(Especifico especifico, List<especificaciones> especificacionesList) {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long especificoId = insertEspecifico(transactionalDsl, especifico);

        for (especificaciones especificoItem : especificacionesList) {
          // Establecer la relación con el especifico usando el constructor que acepta el ID del especifico
          especificoItem.setEspecifico(new Especifico(especificoId));
          insertespecificaciones(transactionalDsl, especificoItem);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }

  private Long insertEspecifico(DSLContext dsl, Especifico especifico) {
    return dsl.insertInto(ACTIVO_TABLE)
            .set(DSL.field("custodioId"), especifico.getResponsable().getId())
            .set(DSL.field("articuloId"), especifico.getArticulo().getId())
            .returningResult(DSL.field("especificoId"))
            .fetchOne()
            .getValue(DSL.field("especificoId", Long.class));
  }

  private void insertespecificaciones(DSLContext dsl, especificaciones especificaciones) {
    dsl.insertInto(ESPECIFICACIONES_TABLE)
            .set(DSL.field("especificoId"), especificaciones.getEspecifico().getId())
            .set(DSL.field("nombreespecifico"), especificaciones.getNombreespecifico())
            .set(DSL.field("descripespecifico"), especificaciones.getDescripespecifico())
            .execute();
  }*/
  /*public void save(Especifico especifico, List<especificaciones> especificacionesList) {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long especificoId = insertEspecifico(transactionalDsl, especifico);

        for (especificaciones especificoItem : especificacionesList) {
          // Establecer la relación con el especifico usando el constructor que acepta el ID del especifico
          especificoItem.setEspecifico(new Especifico(especificoId));
          insertespecificaciones(transactionalDsl, especificoItem);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }

  private Long insertEspecifico(DSLContext dsl, Especifico especifico) {
    return dsl.insertInto(ACTIVO_TABLE)
            .set(DSL.field("custodioId"), especifico.getResponsable().getId())
            .set(DSL.field("articuloId"), especifico.getArticulo().getId())
            .returningResult(DSL.field("especificoId"))
            .fetchOne()
            .getValue(DSL.field("especificoId", Long.class));
  }

  private void insertespecificaciones(DSLContext dsl, especificaciones especificaciones) {
    dsl.insertInto(ESPECIFICACIONES_TABLE)
            .set(DSL.field("especificoId"), especificaciones.getEspecifico().getId())
            .set(DSL.field("activo"), especificaciones.getEspecifico())
            .execute();
  }*/
}

  /*public void save(Especifico especifico, List<especificaciones> especificacionesList) {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long especificoId = insertEspecifico(transactionalDsl, especifico);

        for (especificaciones especificaciones : especificacionesList) {
          especificaciones.setEspecifico(new Especifico(especificoId)); // Establecer la relación con el especifico
          insertespecificaciones(transactionalDsl, especificaciones);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }

  private Long insertEspecifico(DSLContext dsl, Especifico especifico) {
    return dsl.insertInto(ACTIVO_TABLE)
            .set(DSL.field("custodioId"), especifico.getResponsable().getId())
            .set(DSL.field("articuloId"), especifico.getArticulo().getId())
            .returningResult(DSL.field("id"))
            .fetchOne()
            .getValue(DSL.field("id", Long.class));
  }

  private void insertespecificaciones(DSLContext dsl, especificaciones especificaciones) {
    dsl.insertInto(ESPECIFICACIONES_TABLE)
            .set(DSL.field("especificoId"), especificaciones.getEspecifico().getId())
            .set(DSL.field("activo"), especificaciones.getEspecifico())
            .execute();
  }*/
  /*public void save(Especifico especifico, List<especificaciones> especificacionesList) {
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long especificoId = insertEspecifico(transactionalDsl, especifico);

        for (especificaciones especificaciones : especificacionesList) {
          insertespecificaciones(transactionalDsl, especificoId, especificaciones);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }

  private Long insertEspecifico(DSLContext dsl, Especifico especifico) {
    return dsl.insertInto(ACTIVO_TABLE)
            .set(DSL.field("custodioId"), especifico.getResponsable().getId())
            .set(DSL.field("articuloId"), especifico.getArticulo().getId())
            .returningResult(DSL.field("id"))
            .fetchOne()
            .getValue(DSL.field("id", Long.class));
  }

  private void insertespecificaciones(DSLContext dsl, Long especificoId, especificaciones especificaciones) {
    dsl.insertInto(ESPECIFICACIONES_TABLE)
            .set(DSL.field("especificoId"), especificoId)
            .set(DSL.field("descripcion"), especificaciones.getDescripcion())
            .execute();
  }*/
  /*protected void save(Especifico especifico, List<especificaciones> especificacionesList) {
    DSLContext dsl = DatabaseConnection.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long especificoId = insertEspecifico(transactionalDsl, especifico);

        for (especificaciones especificaciones : especificacionesList) {
          insertespecificaciones(transactionalDsl, especificoId, especificaciones);
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }

  private Long insertEspecifico(DSLContext dsl, Especifico especifico) {
    return dsl.insertInto(ACTIVO_TABLE)
            .set(DSL.field("custodioId"), especifico.getResponsable().getId())
            .set(DSL.field("articuloId"), especifico.getArticulo().getId())
            .returningResult(DSL.field("id"))
            .fetchOne()
            .getValue(DSL.field("id", Long.class));
  }

  private void insertespecificaciones(DSLContext dsl, Long especificoId, especificaciones especificaciones) {
    dsl.insertInto(ESPECIFICACIONES_TABLE)
            .set(DSL.field("especificoId"), especificoId)
            .set(DSL.field("descripcion"), especificaciones.getDescripcion())
            .execute();
  }*/
  /*@Override
  protected void save(Especifico especifico, List<especificaciones> especificacionesList) {
    DSLContext dsl = DatabaseConnection.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long especificoId = transactionalDsl.insertInto(ACTIVO_TABLE)
                .set(DSL.field("custodioId"), especifico.getResponsable().getId())
                .set(DSL.field("articuloId"), especifico.getArticulo().getId())
                .returningResult(DSL.field("id"))
                .fetchOne()
                .getValue(DSL.field("id", Long.class));

        for (especificaciones especificaciones : especificacionesList) {
          transactionalDsl.insertInto(ESPECIFICACIONES_TABLE)
                  .set(DSL.field("especificoId"), especificoId)
                  .set(DSL.field("descripcion"), especificaciones.getDescripcion())
                  .execute();
        }
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }*/

  /*protected void save(Especifico especifico, especificaciones especificaciones, Long categoriaId) {
    DSLContext dsl = DatabaseConnection.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);

        Long especificoId = transactionalDsl.insertInto(ACTIVO_TABLE)
                .set(DSL.field("custodioId"), especifico.getResponsable().getId())
                .set(DSL.field("articuloId"), especifico.getArticulo().getId())
                .returningResult(DSL.field("id"))
                .fetchOne()
                .getValue(DSL.field("id", Long.class));

        transactionalDsl.insertInto(ESPECIFICACIONES_TABLE)
                .set(DSL.field("especificoId"), especificoId)
                .set(DSL.field("descripcion"), especificaciones.getDescripcion())
                .execute();
      });
    } catch (Exception e) {
      // Manejar el error de la transacción
    }
  }*/
  /*protected void save(Especifico especifico, especificaciones especificaciones, Long categoriaId) {
    DSLContext dsl = DatabaseConnection.getDSL();
    Long especificoId = dsl.insertInto(ACTIVO_TABLE)
            .set(DSL.field("custodioId"), especifico.getResponsable().getId())
            .set(DSL.field("articuloId"), especifico.getArticulo().getId())
            .returningResult(DSL.field("id"))
            .fetchOne()
            .getValue(DSL.field("id", Long.class));
    dsl.insertInto(ESPECIFICACIONES_TABLE)
            .set(DSL.field("especificoId"), especificoId)
            .set(DSL.field("descripcion"), especificaciones.getDescripcion())
            .execute();
  }*/

    /*
    var dsl = DatabaseConnection.getDSL();
    dsl.insertInto(ACTIVO_TABLE)
            .set(DSL.field("responsable"), especifico.getResponsable())
            .set(DSL.field("articulo"), especifico.getArticulo())
            .set(DSL.field("categoriaId"), categoriaId)
            .execute();*/