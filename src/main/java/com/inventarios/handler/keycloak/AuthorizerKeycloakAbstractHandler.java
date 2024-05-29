package com.inventarios.handler.keycloak;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
/*
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;*/

public class AuthorizerKeycloakAbstractHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        return null;
    }
/*    private final ObjectMapper objectMapper = new ObjectMapper();
    final static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
        headers.put("Access-Control-Allow-Methods", "POST");
    }
    protected abstract String extractAuthToken(APIGatewayProxyRequestEvent request);
    protected abstract AuthorizationInfo validateAuthToken(String authToken);
    protected abstract void addAuthorizationHeaders(AuthorizationInfo authInfo, APIGatewayProxyRequestEvent request);
    private final UserResourceLambdaFunction userResource;
    private final AdminResourceLambdaFunction adminResource;
    public ControlExamenAbstractHandler() {
        this.userResource = new UserResourceLambdaFunction();
        this.adminResource = new AdminResourceLambdaFunction();
    }
    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent request, final Context context) {
        ExamenResponseRest responseRest = new ExamenResponseRest();
        ///request.setHeaders(headers);
        LambdaLogger logger = context.getLogger();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
        try {
            // Obtener el token de autorización de la solicitud
            String authToken = extractAuthToken(request);
            logger.log("authToken = "+authToken);
            // Validar el token y obtener la información de autorización
            AuthorizationInfo authInfo = validateAuthToken(authToken);
            logger.log("authInfo = "+authInfo);
            // Pasar la información de autorización a la aplicación Quarkus
            addAuthorizationHeaders(authInfo, request);

            if (authInfo.isAdmin()) {
                logger.log("================ authInfo.isAdmin() ================");
                response = adminResource.handleRequest(request, context);
            } else {
                logger.log("================ authInfo.isUser() ================");
                response = userResource.handleRequest(request, context);
            }
            List<Examen> listaexamenes = new ArrayList<>();
            String output = "";
            String contentTypeHeader = request.getHeaders().get("Content-Type");
            logger.log("Content-Type: " + contentTypeHeader);
            listaexamenes.add(e);
            responseRest.getExamenResponse().setListaexamenes(listaexamenes);
            responseRest.setMetadata("Respuesta ok", "00", "estudianteSession guardado");
            output = new Gson().toJson(responseRest);
            return response.withStatusCode(200)
                    .withBody(output);
        } catch (Exception e) {
            e.printStackTrace();
            responseRest.setMetadata("Respuesta nok", "-1", "Error al insertar");
            return response
                    .withBody(new Gson().toJson(responseRest))
                    .withStatusCode(500);
        }
    }

    public PublicKey getKeycloakPublicKey() {
        try {
            String jwksUrl = "https://examensolucion-u8698.vm.elestio.app/realms/ExamenSolucion/protocol/openid-connect/certs";
            HttpURLConnection connection = (HttpURLConnection) new URL(jwksUrl).openConnection();
            connection.setRequestMethod("GET");

            InputStream responseStream = connection.getInputStream();
            String response = new String(responseStream.readAllBytes());

            return extractPublicKeyFromJson(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    ///private
    public PublicKey extractPublicKeyFromJson(String jsonResponse) {
        try {
            Map<String, Object> responseMap = new ObjectMapper().readValue(jsonResponse, new TypeReference<Map<String, Object>>() {});
            List<Map<String, Object>> keys = (List<Map<String, Object>>) responseMap.get("keys");
            if (keys == null || keys.isEmpty()) {
                throw new RuntimeException("No keys found in the JWKS response");
            }
            String modulus0 = (String) keys.get(0).get("n");
            String exponent0 = (String) keys.get(0).get("e");

            return createPublicKey(modulus0, exponent0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    ///private
    public PublicKey createPublicKey(String modulus0, String exponent0) {
        try {
            byte[] modBytes = Base64.getUrlDecoder().decode(modulus0);
            byte[] expBytes = Base64.getUrlDecoder().decode(exponent0);

            BigInteger modBigInt = new BigInteger(1, modBytes);
            BigInteger expBigInt = new BigInteger(1, expBytes);

            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modBigInt, expBigInt);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
*/
}
