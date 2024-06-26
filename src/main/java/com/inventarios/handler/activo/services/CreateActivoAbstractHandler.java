package com.inventarios.handler.activo.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.inventarios.handler.activo.response.ActivoResponseRest;
import java.sql.SQLException;
import java.util.*;
import com.inventarios.model.*;
import com.inventarios.util.GsonFactory;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class CreateActivoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    protected final static Table<Record> ACTIVO_TABLE = DSL.table("activo");
    protected final static Table<Record> ESPECIFICACIONES_TABLE = DSL.table("especificaciones");
    final static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
        headers.put("Access-Control-Allow-Methods", "POST");
    }
    protected abstract int getEspecificoID(Activo activo) throws SQLException;
    protected abstract void save(Activo activo, List<Especificaciones> especificacionesList) throws SQLException;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        //input.setHeaders(headers);
        ActivoResponseRest responseRest = new ActivoResponseRest();
        LambdaLogger logger = context.getLogger();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        List<Activo> list = new ArrayList<>();
        List<Especificaciones> especificacionesList = new ArrayList<>();
        String output ="";
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(input.getBody());

            String body = input.getBody();
            //String body = "{\"custodioId\":4,\"articuloId\":3,\"tipoId\":1,\"categoriaId\":1,\"codinventario\":\"VQVREE\",\"modelo\":\"NINAS\",\"marca\":\"ZZDZCSCW\",\"nroserie\":\"V1VV3A\",\"fechaingreso\":\"2024-06-12\",\"fechaingresostr\":\"2024-06-12\",\"importe\":\"522515\",\"moneda\":\"S/\",\"descripcion\":\"As\",\"proveedorId\":1,\"especificaciones\":[{\"atributo\":\"COLOR\",\"nombreespecifico\":\"AZUL\"},{\"atributo\":\"DIMENSION\",\"nombreespecifico\":\"123\"}]}";
            if (body != null && !body.isEmpty()) {
                logger.log(body);

                Gson gson = GsonFactory.createGson();
                Activo activo = gson.fromJson(body, Activo.class);
                ///Especifico especifico = mapper.readValue(body, Especifico.class);
                if (activo == null) {
                    return response
                            .withBody("El cuerpo de la solicitud no contiene datos válidos para un activo")
                            .withStatusCode(400);
                }
                if (activo != null) {
                    //List<Especificaciones> especificacionesListaInicial = new ArrayList<>();

                    // Initialize default value for especificoid
                    //Long defaultEspecificoId = 0L;

                    // Create a default especificaciones object
                    /*Especificaciones defaultespecificaciones = new Especificaciones();
                    defaultespecificaciones.setEspecificoid(defaultEspecificoId);
                    defaultespecificaciones.setNombreespecifico(""); // You can set other properties if necessary

                    // Add the default especificaciones object to especificacionesList
                    especificacionesListaInicial.add(defaultespecificaciones);
                    */
                    // Set the especificacionesList to the especificaciones property of especifico
                    /*EspecificacionTecnica especificacionTecnica=new EspecificacionTecnica();
                    List<EspecificacionTecnica> especificacionTecnicaList = new ArrayList<>();
                    List<EspecificacionTecnica> especificaciones = jsonNode.get("especificaciones").asLong();
                    especifico.setEspecificaciones(especificacionTecnicaList);*/
                    //especificaciones(especificacionesListaInicial);
                    /*
                    for (EspecificacionTecnica especificacion : activo.getEspecificaciones()) {
                        // Aquí puedes hacer cualquier ajuste necesario en los atributos de EspecificacionTecnica
                        especificacionesList.add(especificacion);
                    }*/

                    logger.log("especifico = "+ activo);

                    Long custodioId = jsonNode.get("custodioId").asLong();
                    logger.log("custodioId = "+custodioId);
                    Long articuloId = jsonNode.get("articuloId").asLong();
                    logger.log("articuloId = "+articuloId);
                    Long tipoId = jsonNode.get("tipoId").asLong();
                    logger.log("tipoId = "+tipoId);
                    Long categoriaId = jsonNode.get("categoriaId").asLong();
                    logger.log("categoriaId = "+categoriaId);
                    Long proveedorId = jsonNode.get("proveedorId").asLong();
                    logger.log("proveedorId = "+proveedorId);

                    Custodio custodio =new Custodio();
                    custodio.setId(custodioId);

                    Articulo articulo=new Articulo();
                    articulo.setId(articuloId);

                    Tipo tipo=new Tipo();
                    tipo.setId(tipoId);

                    Categoria categoria =new Categoria();
                    categoria.setId(categoriaId);

                    Proveedor proveedor=new Proveedor();
                    proveedor.setId(proveedorId);

                    activo.setResponsable(custodio);
                    activo.setArticulo(articulo);
                    activo.setTipo(tipo);
                    activo.setGrupo(categoria);
                    activo.setProveedor(proveedor);

                    activo.getResponsable().setId(custodioId);
                    activo.getArticulo().setId(articuloId);
                    activo.getTipo().setId(tipoId);
                    activo.getGrupo().setId(categoriaId);
                    activo.getProveedor().setId(proveedorId);

                    long especificoID = getEspecificoID(activo);
                    activo.setId(especificoID);

                    JsonNode especificacionesNode = jsonNode.get("especificaciones");
                    if (especificacionesNode != null && especificacionesNode.isArray()) {
                        for (JsonNode especificoNode : especificacionesNode) {
                            Especificaciones especificaciones=new Especificaciones();
                            especificaciones.setNombreatributo(especificoNode.get("nombreatributo")!=null?especificoNode.get("nombreatributo").asText():"");
                            especificaciones.setDescripcionatributo(especificoNode.get("descripcionatributo")!=null?especificoNode.get("descripcionatributo").asText():"");
                            especificacionesList.add(especificaciones);
                        }
                    }
                    activo.setEspecificaciones(especificacionesList);//setespecificaciones(especificacionesList);
                    logger.log(":::::::::::::::::::::::::::::::::: PREPARANDO PARA INSERTAR ::::::::::::::::::::::::::::::::::");
                    save(activo, especificacionesList);
                    logger.log(":::::::::::::::::::::::::::::::::: INSERCIÓN COMPLETA ::::::::::::::::::::::::::::::::::");
                    list.add(activo);
                    responseRest.getActivoResponse().setListaactivos(list);
                    responseRest.setMetadata("Respuesta ok", "00", "Especifico guardado");
                }
                output = GsonFactory.createGson().toJson(responseRest);
                logger.log(output);
            }
            return response.withStatusCode(200)
                    .withBody(output);
        } catch (Exception e) {
            responseRest.setMetadata("Respuesta nok", "-1", "Error al guardar");
            return response
                    .withBody(e.toString())
                    .withStatusCode(500);
        }
    }
}