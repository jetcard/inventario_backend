package com.inventarios.handler.especifico.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.inventarios.handler.especifico.response.EspecificoResponseRest;
import com.inventarios.model.*;
import java.sql.SQLException;
import java.util.*;

import com.inventarios.util.GsonFactory;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class CreateEspecificoAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    protected final static Table<Record> ESPECIFICO_TABLE = DSL.table("especifico");
    protected final static Table<Record> ESPECIFICOS_TABLE = DSL.table("especificos");
    final static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
        headers.put("Access-Control-Allow-Methods", "POST");
    }
    protected abstract int getEspecificoID(Especifico especifico) throws SQLException;
    protected abstract void save(Especifico especifico) throws SQLException;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        //input.setHeaders(headers);
        EspecificoResponseRest responseRest = new EspecificoResponseRest();
        LambdaLogger logger = context.getLogger();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        List<Especifico> list = new ArrayList<>();
        //List<Especificos> especificosList = new ArrayList<>();
        List<EspecificacionTecnica> especificacionesList = new ArrayList<>();

        String output ="";
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(input.getBody());

            //String body = input.getBody();
            String body = "{\"responsableId\":4,\"articuloId\":3,\"tipoId\":1,\"grupoId\":1,\"codinventario\":\"VQVREE\",\"modelo\":\"NINAS\",\"marca\":\"ZZDZCSCW\",\"nroserie\":\"V1VV3A\",\"fechaingreso\":\"2024-06-12\",\"fechaingresostr\":\"2024-06-12\",\"importe\":\"522515\",\"moneda\":\"S/\",\"descripcion\":\"As\",\"proveedorId\":1,\"especificaciones\":[{\"atributo\":\"COLOR\",\"nombreespecifico\":\"AZUL\"},{\"atributo\":\"DIMENSION\",\"nombreespecifico\":\"123\"}]}";
            if (body != null && !body.isEmpty()) {
                logger.log(body);

                Gson gson = GsonFactory.createGson();
                Especifico especifico = gson.fromJson(body, Especifico.class);
                ///Especifico especifico = mapper.readValue(body, Especifico.class);
                if (especifico == null) {
                    return response
                            .withBody("El cuerpo de la solicitud no contiene datos válidos para un activo")
                            .withStatusCode(400);
                }
                if (especifico != null) {
                    List<Especificos> especificosListaInicial = new ArrayList<>();

                    // Initialize default value for especificoid
                    Long defaultEspecificoId = 0L;

                    // Create a default Especificos object
                    Especificos defaultEspecificos = new Especificos();
                    defaultEspecificos.setEspecificoid(defaultEspecificoId);
                    defaultEspecificos.setNombreespecifico(""); // You can set other properties if necessary

                    // Add the default Especificos object to especificosList
                    especificosListaInicial.add(defaultEspecificos);

                    // Set the especificosList to the especificos property of especifico
                    /*EspecificacionTecnica especificacionTecnica=new EspecificacionTecnica();
                    List<EspecificacionTecnica> especificacionTecnicaList = new ArrayList<>();
                    List<EspecificacionTecnica> especificaciones = jsonNode.get("especificaciones").asLong();
                    especifico.setEspecificaciones(especificacionTecnicaList);*/
                    //Especificos(especificosListaInicial);

                    for (EspecificacionTecnica especificacion : especifico.getEspecificaciones()) {
                        // Aquí puedes hacer cualquier ajuste necesario en los atributos de EspecificacionTecnica
                        especificacionesList.add(especificacion);
                    }

                    logger.log("especifico = "+especifico);

                    Long responsableId = jsonNode.get("responsableId").asLong();
                    logger.log("responsableId = "+responsableId);
                    Long articuloId = jsonNode.get("articuloId").asLong();
                    logger.log("articuloId = "+articuloId);
                    Long tipoId = jsonNode.get("tipoId").asLong();
                    logger.log("tipoId = "+tipoId);
                    Long grupoId = jsonNode.get("grupoId").asLong();
                    logger.log("grupoId = "+grupoId);
                    Long proveedorId = jsonNode.get("proveedorId").asLong();
                    logger.log("proveedorId = "+proveedorId);

                    Responsable responsable=new Responsable();
                    responsable.setId(responsableId);

                    Articulo articulo=new Articulo();
                    articulo.setId(articuloId);

                    Tipo tipo=new Tipo();
                    tipo.setId(tipoId);

                    Grupo grupo=new Grupo();
                    grupo.setId(grupoId);

                    Proveedor proveedor=new Proveedor();
                    proveedor.setId(proveedorId);

                    especifico.setResponsable(responsable);
                    especifico.setArticulo(articulo);
                    especifico.setTipo(tipo);
                    especifico.setGrupo(grupo);
                    especifico.setProveedor(proveedor);

                    especifico.getResponsable().setId(responsableId);
                    especifico.getArticulo().setId(articuloId);
                    especifico.getTipo().setId(tipoId);
                    especifico.getGrupo().setId(grupoId);
                    especifico.getProveedor().setId(proveedorId);

                    long especificoID = getEspecificoID(especifico);
                    especifico.setId(especificoID);

                    JsonNode especificosNode = jsonNode.get("especificaciones");
                    if (especificosNode != null && especificosNode.isArray()) {
                        for (JsonNode especificoNode : especificosNode) {
                            EspecificacionTecnica especificacionTecnica=new EspecificacionTecnica();
                            especificacionTecnica.setAtributo(especificoNode.get("atributo")!=null?especificoNode.get("atributo").asText():"");
                            especificacionTecnica.setNombreespecifico(especificoNode.get("nombreespecifico")!=null?especificoNode.get("nombreespecifico").asText():"");
                            especificacionesList.add(especificacionTecnica);

                            /*
                            //especificoNode.get("especificoid").asText();
                            //logger.log("especificoNode.get(especificoid).asText() = " + especificoNode.get("especificoid")!=null?especificoNode.get("especificoid").asText():"");
                            Especificos especificos = new Especificos();
                            especificos.setEspecificoid(especifico.getId());
                            ///especificos.setEspecifico(especifico);
                            //especificos.setNombreespecifico(especificoNode.get("nombreespecifico").asText(""));
                            especificos.setNombreespecifico(especificoNode.get("nombreespecifico")!=null?especificoNode.get("nombreespecifico").asText():"");
                            especificosList.add(especificos);
                            */

                        }
                    }
                    especifico.setEspecificaciones(especificacionesList);//setEspecificos(especificosList);
                    logger.log(":::::::::::::::::::::::::::::::::: PREPARANDO PARA INSERTAR ::::::::::::::::::::::::::::::::::");
                    save(especifico);
                    logger.log(":::::::::::::::::::::::::::::::::: INSERCIÓN COMPLETA ::::::::::::::::::::::::::::::::::");
                    list.add(especifico);
                    responseRest.getEspecificoResponse().setListaespecificos(list);
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