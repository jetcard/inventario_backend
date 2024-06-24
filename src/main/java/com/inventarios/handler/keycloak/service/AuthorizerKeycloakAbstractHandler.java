package com.inventarios.handler.keycloak.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.handler.keycloak.response.KeycloakResponseRest;
import com.inventarios.model.AuthorizationInfo;
import com.inventarios.util.GsonFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AuthorizerKeycloakAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
   private static final Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "X-UserId, X-Roles, " +
                "content-type, X-Custom-Header, X-Amz-Date, Authorization, X-Api-Key, X-Amz-Security-Token");
        headers.put("Access-Control-Allow-Methods", "POST");
    }

    protected abstract String extractAuthToken(APIGatewayProxyRequestEvent request);

    protected abstract AuthorizationInfo validateAuthToken(String authToken);

    protected abstract void addAuthorizationHeaders(AuthorizationInfo authInfo, APIGatewayProxyRequestEvent request);

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent request, final Context context) {
        LambdaLogger logger = context.getLogger();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
        KeycloakResponseRest responseRest=new KeycloakResponseRest();
        try {
            String authToken = extractAuthToken(request);
            //logger.log("authToken = "+authToken);
            AuthorizationInfo authInfo = validateAuthToken(authToken);
            //logger.log("authInfo = "+authInfo);
            addAuthorizationHeaders(authInfo, request);
            if (authInfo.isAdmin()) {
                System.out.println("ROL ADMIN");
                //response = adminResource.handleRequest(request, context);
            } else if(authInfo.isUser()){
                System.out.println("ROL USER");

            } else {
                System.out.println("NONE ROL");
                //response = userResource.handleRequest(request, context);
            }
            responseRest.getKeycloakResponse().setAuthorizationList(convertResultToList(authInfo));
            responseRest.setMetadata("Autorizado", "00", "Ingreso autorizado");
            Gson gson = GsonFactory.createGson();
            String output = gson.toJson(responseRest);
            logger.log(output);
            return response.withStatusCode(200).withHeaders(headers)
                    .withBody(output);
        } catch (Exception e) {
            responseRest.setMetadata("No autorizado", "-1", "Ingreso no autorizado");
            return response
                    .withBody(e.toString())
                    .withStatusCode(500);
        }
    }
    protected List<AuthorizationInfo> convertResultToList(AuthorizationInfo authInfo){
        List<AuthorizationInfo> lista=new ArrayList<>();
        lista.add(authInfo);
        return lista;
    }
}