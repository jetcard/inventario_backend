package com.inventarios.handler.activo.services;

//import com.inventarios.model.especificacionesFiltro;

public abstract class BusquedaPorIdsAbstractHandler{/* implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final Table<?> ACTIVO_TABLE = DSL.table("activo");
  protected static final Field<String> ACTIVO_TABLE_COLUMNA = DSL.field("nombreespecifico", String.class);
  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }
  //protected abstract Result<Record> filtraPorIds(especificacionesFiltro filter) throws SQLException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    LambdaLogger logger = context.getLogger();
    ActivoResponseRest responseRest = new ActivoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    logger.log("buscar: " + idString);
    String output = "";
    especificacionesFiltro filtro=new especificacionesFiltro();
    try {
      Result<Record> result = filtraPorIds(filtro);
      responseRest.getActivoResponse().setListaactivos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Activos encontrados");
      output = new Gson().toJson(responseRest);
      return response.withStatusCode(200)
                    .withBody(output);
  } catch (Exception e) {
        responseRest.setMetadata("Respuesta nok", "-1", "Error al guardar");
            return response
                    .withBody(e.toString())
        .withStatusCode(500);
        }
  }

  protected List<Activo> convertResultToList(Result<Record> result) {
    List<Activo> listaActivos = new ArrayList<>();
    for (Record record : result) {
      Activo activo = new Activo();
      activo.setId(record.getValue("id", Long.class));
      activo.setResponsable(record.getValue("custodioId", Responsable.class));
      activo.setArticulo(record.getValue("articuloId", Articulo.class));
      ///especifico.setespecificaciones(record.getValue("descripespecifico", List.class));
      listaActivos.add(activo);
    }
    return listaActivos;
  }
*/
}