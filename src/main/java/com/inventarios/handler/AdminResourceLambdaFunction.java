package com.inventarios.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

/*
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import org.eclipse.microprofile.jwt.JsonWebToken;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import javax.security.auth.AuthPermission;
import java.util.Map;

@Authenticated
@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)*/
public class AdminResourceLambdaFunction implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        return null;
    }
/*    @Inject
    JsonWebToken jwt;
    @Inject
    SecurityIdentity securityIdentity;

    @GET
    @RolesAllowed("admin")
    public APIGatewayProxyResponseEvent admin() {
        Map<String, String> response = Map.of(
                "subject", jwt.getSubject(),
                "preferred_username", jwt.getClaim("preferred_username")
        );
        return buildResponse(200, response);
    }

    @GET
    @Path("/permissions")
    public APIGatewayProxyResponseEvent permissions() {
        return securityIdentity.checkPermission(new AuthPermission("Admin Resource")).onItem()
                .transform(granted -> {
                    if (granted) {
                        return securityIdentity.getAttribute("permissions");
                    }
                    return null;
                })
                .onItem().transform(permissions -> buildResponse(200, permissions))
                .await().indefinitely();
    }

    private APIGatewayProxyResponseEvent buildResponse(int statusCode, Object body) {
        try {
            String responseBody = new ObjectMapper().writeValueAsString(body);
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(statusCode)
                    .withHeaders(Map.of("Content-Type", "application/json"))
                    .withBody(responseBody);
        } catch (JsonProcessingException e) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("Error processing response body");
        }
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        // Implementa la lógica para manejar las solicitudes entrantes
        return null;
    }*/
}