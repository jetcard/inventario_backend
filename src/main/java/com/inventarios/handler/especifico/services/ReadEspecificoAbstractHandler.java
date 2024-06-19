package com.inventarios.handler.especifico.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.inventarios.handler.especifico.response.EspecificoResponseRest;
import com.inventarios.model.*;
import com.inventarios.util.GsonFactory;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.*;

public abstract class ReadEspecificoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  protected final static Table<Record> ESPECIFICO_TABLE = table("especifico");
  protected final static Table<Record> ESPECIFICOS_TABLE = table("especificos");
  protected final static Field<Long> ESPECIFICO_ID = field(name("especifico", "id"), Long.class);
  protected final static Field<Long> ESPECIFICO_RESPONSABLE_ID = field(name("especifico", "responsableid"), Long.class);
  protected final static Field<Long> ESPECIFICO_ARTICULO_ID = field(name("especifico", "articuloid"), Long.class);
  protected final static Field<Long> ESPECIFICO_GRUPO_ID = field(name("especifico", "grupoid"), Long.class);

  protected final static Field<Long> ESPECIFICO_PROVEEDOR_ID = field(name("especifico", "proveedorid"), Long.class);

  protected final static Field<Long> ESPECIFICOS_ID = field(name("especificos", "id"), Long.class);
  protected final static Field<Long> ESPECIFICOS_ESPECIFICOID = field(name("especificos", "especificoid"), Long.class);
  protected final static Field<String> ESPECIFICOS_NOMBREESPECIFICO = field(name("especificos", "nombreespecifico"), String.class);
  protected final static Field<String> ESPECIFICO_CODINVENTARIO = field(name("especifico", "codinventario"), String.class);
  protected final static Field<String> ESPECIFICO_MODELO = field(name("especifico", "modelo"), String.class);
  protected final static Field<String> ESPECIFICO_MARCA = field(name("especifico", "marca"), String.class);
  protected final static Field<String> ESPECIFICO_NROSERIE = field(name("especifico", "nroserie"), String.class);
  protected final static Field<LocalDate> ESPECIFICO_FECHAINGRESO = field(name("especifico", "fechaingreso"), LocalDate.class);
  protected final static Field<String> ESPECIFICO_FECHAINGRESOSTR = field(name("especifico", "fechaingresostr"), String.class);
  protected final static Field<String> ESPECIFICO_MONEDA = field(name("especifico", "moneda"), String.class);
  protected final static Field<String> ESPECIFICO_IMPORTE = field(name("especifico", "importe"), String.class);
  protected final static Field<String> ESPECIFICO_DESCRIPCION = field(name("especifico", "descripcion"), String.class);
  protected final static Table<Record> RESPONSABLE_TABLE = DSL.table("responsable");
  protected static final Field<String> RESPONSABLE_TABLE_COLUMNA = DSL.field("arearesponsable", String.class);
  protected final static Table<Record> ARTICULO_TABLE = table("articulo");
  protected static final Field<String> ARTICULO_TABLE_COLUMNA = DSL.field("nombrearticulo", String.class);
  protected final static Table<Record> TIPO_TABLE = DSL.table("tipo");
  protected static final Field<String> TIPO_TABLE_COLUMNA = DSL.field("nombretipo", String.class);
  protected final static Table<Record> GRUPO_TABLE = DSL.table("grupo");
  protected static final Field<String> GRUPO_TABLE_COLUMNA = DSL.field("nombregrupo", String.class);
  protected final static Table<Record> PROVEEDOR_TABLE = DSL.table("proveedor");
  protected static final Field<String> PROVEEDOR_TABLE_COLUMNA = DSL.field("razonsocial", String.class);

  protected final static Field<Long> ESPECIFICO_TIPO_ID = field(name("especifico", "tipoid"), Long.class);


  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected abstract Result<Record18<Long, Long, Long, Long, Long, Long, Long, Long, String, String, String, String, String, LocalDate, String, String, String, String>> read() throws SQLException;
  protected abstract String mostrarResponsable(Long id) throws SQLException;
  protected abstract String mostrarArticulo(Long id) throws SQLException;
  protected abstract String mostrarTipoBien(Long id) throws SQLException;
  protected abstract String mostrarGrupo(Long id) throws SQLException;
  protected abstract String mostrarProveedor(Long id) throws SQLException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    EspecificoResponseRest responseRest = new EspecificoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    String output ="";
    try {
      Result<Record18<Long, Long, Long, Long, Long, Long, Long, Long, String, String, String, String, String, LocalDate, String, String, String, String>> result = read();
      responseRest.getEspecificoResponse().setListaespecificos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Especificos encontrados");
      Gson gson = GsonFactory.createGson();
      output = gson.toJson(responseRest);
      return response.withStatusCode(200).withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al consultar");
      return response
        .withBody(e.toString())
        .withStatusCode(500);
    }
  }

  protected List<Especifico> convertResultToList(Result<Record18<Long, Long, Long, Long, Long, Long, Long, Long, String, String, String, String, String, LocalDate, String, String, String, String>> result) throws SQLException {
      Map<Long, Especifico> especificoMap = new HashMap<>();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      for (Record18<Long, Long, Long, Long, Long, Long, Long, Long, String, String, String, String, String, LocalDate, String, String, String, String> record : result) {
        Long especificoId = record.get(ESPECIFICO_ID);
        Especifico especifico = especificoMap.get(especificoId);
        if (especifico == null) {
          especifico = new Especifico();
          especifico.setId(especificoId);
          // Aquí deberías asignar los valores correspondientes a responsable y articulo
          // Consultas adicionales o mapeos deben ser implementados para Responsable y Articulo
          Responsable responsable = new Responsable();
          responsable.setId(record.get(ESPECIFICO_RESPONSABLE_ID));
          responsable.setArearesponsable(mostrarResponsable(responsable.getId()));
          especifico.setResponsable(responsable);
          //especifico.setResponsable(findResponsableById(record.get(ESPECIFICO_RESPONSABLE_ID)));
          Articulo articulo = new Articulo();
          articulo.setId(record.get(ESPECIFICO_ARTICULO_ID));
          articulo.setNombrearticulo(mostrarArticulo(articulo.getId()));
          especifico.setArticulo(articulo);
          //especifico.setArticulo(findArticuloById(record.get(ESPECIFICO_ARTICULO_ID)));

          Tipo tipo = new Tipo();
          tipo.setId(record.get(ESPECIFICO_TIPO_ID));
          tipo.setNombretipo(mostrarTipoBien(tipo.getId()));
          especifico.setTipo(tipo);

          Grupo grupo=new Grupo();
          grupo.setId(record.get(ESPECIFICO_GRUPO_ID));
          grupo.setNombregrupo(mostrarGrupo(grupo.getId()));
          especifico.setGrupo(grupo);

          //
          Proveedor proveedor=new Proveedor();
          proveedor.setId(record.get(ESPECIFICO_PROVEEDOR_ID));
          proveedor.setRazonsocial(mostrarProveedor(proveedor.getId()));
          especifico.setProveedor(proveedor);

          especifico.setCodinventario(record.getValue("codinventario", String.class));
          especifico.setModelo(record.getValue("modelo", String.class));
          especifico.setMarca(record.getValue("marca", String.class));
          especifico.setNroserie(record.getValue("nroserie", String.class));
          LocalDate fechaIngreso = record.getValue("fechaingreso", LocalDate.class);
          especifico.setFechaingreso(fechaIngreso);
          if (fechaIngreso != null) {
            String formattedDate = fechaIngreso.format(formatter);
            especifico.setFechaingresostr(formattedDate);
          }
          especifico.setMoneda(record.getValue("moneda", String.class));
          especifico.setImporte(record.getValue("importe", BigDecimal.class));
          especifico.setDescripcion(record.getValue("descripcion", String.class));

          especifico.setEspecificos(new ArrayList<>());
          especificoMap.put(especificoId, especifico);
        }
        Long especificosId = record.get(ESPECIFICOS_ID);
        if (especificosId != null) {
          Especificos especificos = new Especificos();
          especificos.setId(especificosId);
          especificos.setEspecificoid(record.get(ESPECIFICOS_ESPECIFICOID));
          especificos.setNombreespecifico(record.get(ESPECIFICOS_NOMBREESPECIFICO));
          especifico.getEspecificos().add(especificos);
        }
      }
      return new ArrayList<>(especificoMap.values());
    }
/*
    List<Especifico> listaEspecificos = new ArrayList<>();

    for (Record record : result) {
      Especifico especifico = new Especifico();
      especifico.setId(record.getValue("id", Long.class));

      / *Responsable responsable=new Responsable();
      responsable.setId(record.getValue("responsableid", Long.class));
      Articulo articulo=new Articulo();
      articulo.setId(record.getValue("articuloid", Long.class));
      especifico.setResponsable(responsable);
      especifico.setArticulo(articulo);* /

      Responsable responsable = new Responsable();
      responsable.setId(record.getValue("responsableid", Long.class));
      responsable.setArearesponsable(mostrarResponsable(responsable.getId()));
      especifico.setResponsable(responsable);

      Articulo articulo = new Articulo();
      articulo.setId(record.getValue("articuloid", Long.class));
      articulo.setNombrearticulo(mostrarArticulo(articulo.getId()));
      especifico.setArticulo(articulo);

      listaEspecificos.add(especifico);
    }
    return listaEspecificos;
  }*/

}
