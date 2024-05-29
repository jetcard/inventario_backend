package com.inventarios.handler;
/*
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.impl.DSL;
import org.keycloak.common.VerificationException;
import org.keycloak.jose.jws.JWSInputException;
import org.keycloak.representations.AccessToken;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;*/

public class AuthorizerKeycloakHandler {
/*    private static final LambdaLogger logger = new MyLambdaLogger();
    @Override
    protected String extractAuthToken(APIGatewayProxyRequestEvent input) {
        Map<String, String> headers = input.getHeaders();
        logger.log("headers = "+headers);
        if (headers != null) {
            // Extraer el encabezado de autorización del cuerpo de la solicitud
            String authHeader = headers.get("Authorization");
            logger.log("authHeader = "+authHeader);
            // Verificar si el encabezado de autorización es nulo o no comienza con "Bearer "
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                logger.log("authHeader = "+authHeader.substring(7));
                return authHeader.substring(7); // La longitud de "Bearer " es 7
            }
        }
        return null;
    }

    @Override
    protected AuthorizationInfo validateAuthToken(String authToken) {
        if (authToken != null) {
            try {
                // Decodifica el token JWT
                Claims claims = Jwts.parser()
                        .setSigningKey(getKeycloakPublicKey()) // Método para obtener la clave pública de Keycloak
                        .parseClaimsJws(authToken)
                        .getBody();
                logger.log("claims = "+claims);
                // Extrae los roles de los claims
                List<String> roles = extractRolesFromClaims(claims);
                logger.log("roles =====> "+roles);
                // Extrae el nombre de usuario o el ID de usuario desde los claims
                String userId = claims.getSubject(); // O usa otro claim como "preferred_username" si es necesario
                logger.log("userId =====> "+userId);
                return new AuthorizationInfo(userId, roles);
            } catch (JwtException e){// | SignatureException e) {
                // Maneja la excepción si la firma del token no es válida
                e.printStackTrace();
            }
        }
        return null; // Devuelve null si el token no es válido
    }

    // Método para extraer los roles desde los claims del token
    private List<String> extractRolesFromClaims(Claims claims) {
        List<String> roles = new ArrayList<>();
        Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");
        if (realmAccess != null) {
            roles = (List<String>) realmAccess.get("roles");
        }
        logger.log("roles = " + roles);
        return roles;
    }

    // Método para extraer los roles desde el token
    private List<String> extractRolesFromToken(AccessToken token) {
        logger.log("extractRolesFromToken(token) ");
        List<String> roles = new ArrayList<>();
        AccessToken.Access realmAccess = token.getRealmAccess();
        if (realmAccess != null) {
            roles.addAll(realmAccess.getRoles());
        }
        logger.log("roles: "+roles);
        return roles;
    }

    @Override
    protected void addAuthorizationHeaders(AuthorizationInfo authInfo, APIGatewayProxyRequestEvent request){
        // Implementa la lógica para agregar los encabezados HTTP de autorización a la solicitud
        if (authInfo != null) {
            logger.log("authInfo = "+authInfo);
            // Por ejemplo, puedes agregar el ID de usuario como un encabezado personalizado
            // y los roles como encabezados de autorización
            request.getHeaders().put("X-UserId", authInfo.getUserId());
            request.getHeaders().put("X-Roles", String.join(",", authInfo.getRoles()));

            logger.log("request.getHeaders() X-UserId = "+request.getHeaders().get("X-UserId"));
            logger.log("request.getHeaders() X-Roles = "+request.getHeaders().get("X-Roles"));

        } else {
            logger.log("authInfo is null, cannot add authorization headers");
        }
    }

}

class MyLambdaLogger implements LambdaLogger {
    @Override
    public void log(String message) {
        // Aquí puedes redirigir los mensajes de registro a donde quieras, por ejemplo, a System.out
        System.out.println(message);
    }

    @Override
    public void log(byte[] message) {
        // No necesitas implementar este método para este caso
    }*/
}