package com.inventarios.handler.keycloak;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventarios.handler.keycloak.service.AuthorizerKeycloakAbstractHandler;
import com.inventarios.model.AuthorizationInfo;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;

public class AuthorizerKeycloakHandler extends AuthorizerKeycloakAbstractHandler {
    //private static final LambdaLogger logger = new MyLambdaLogger();
    @Override
    protected String extractAuthToken(APIGatewayProxyRequestEvent input) {
        Map<String, String> headers = input.getHeaders();
        ////logger.log("headers = "+headers);
        if (headers != null) {
            String authHeader = headers.get("Authorization");
            //logger.log("authHeader = "+authHeader);
            //quitamos los 7 primeros caracteres de authHeader
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                //logger.log("token: "+authHeader.substring(7));
                return authHeader.substring(7);
            }
        }
        return null;
    }

    @Override
    protected AuthorizationInfo validateAuthToken(String authToken) {
        if (authToken != null) {
            try {
                String jwkJson = "{\n" +
                        "  \"e\": \"AQAB\",\n" +
                        "  \"kty\": \"RSA\",\n" +
                        "  \"n\": \"tf1f25bAtZMGbwXkD_UNZ1z9eH91rNeZ-MIWeiGpE-g0u1Y6lBKi-vI6O0nFGTcilCTR6tcqXu9Ah6cwxPC6n0ORCUFd_VXRzjGHzrU5_Kofhb8_yPYFaAp3JAuvrj7PJNnY7RUZZibBuBpGIesrdwuDdBDprR4VzKuSKl7HZCMcAkhqNQjaSNt1UhwDb1mV22uu4NfjqaGSfp2LnRWnpUYTGZobTRE2S5kAw73kefewPCHiooryCZK_69NK5TAZWXWf-YPpFtdwmf7mFggonpWWrCuTWikEKicwdL6xn6oWYeuVlM80M2hUhNJNUSLB7fDHYli5NRqky315bsjvhw\"\n" +
                        "}";
                try {
                    PublicKey publicKey = getPublicKeyFromJWK(jwkJson);
                    Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, null);
                    JWTVerifier verifier = JWT.require(algorithm)
                            .withIssuer("https://examensolucion-u8698.vm.elestio.app/realms/Inventario")
                            .build();
                    DecodedJWT jwt = verifier.verify(authToken);

                    // Extraer y utilizar reclamaciones
                    String subject = jwt.getSubject();
                    String issuer = jwt.getIssuer();

                    Map<String, Claim> claims = jwt.getClaims();
                    Claim claim_realm_access = claims.get("realm_access");
                    AuthorizationInfo authorizationInfo=new AuthorizationInfo();
                    //logger.log("claim_realm_access = " + claim_realm_access.asMap());
                    Map<String, Object> realmAccess = claim_realm_access.asMap();
                    List<String> roles = null;
                    if (realmAccess != null) {
                        Object claim_realm_roles =  realmAccess.get("roles");
                        //logger.log("roles = " + claim_realm_roles);
                        if (claim_realm_roles instanceof List) {
                            roles = new ArrayList<>((List<String>) claim_realm_roles);
                        } else if (claim_realm_roles instanceof String) {
                            roles = Collections.singletonList((String) claim_realm_roles);
                        } else {
                            //System.out.println("Invalid realm_access roles format.");
                        }
                        System.out.println("Subject: " + subject);
                        System.out.println("Issuer: " + issuer);
                        // Mostrar roles si están presentes
                        if (roles != null) {
                            //System.out.println("Roles: ");
                            for (String role : roles) {
                                System.out.println(role);
                                // Ejemplo de verificación de rol específico
                                if ("user".equals(role)) {
                                    //System.out.println("El usuario tiene el rol 'user'.");
                                    // Aquí puedes agregar lógica adicional relacionada con el rol 'user'
                                }
                            }
                            authorizationInfo.setRoles(roles);
                        } else {
                            //System.out.println("No roles found.");
                        }
                    }
                    authorizationInfo.setUserId(jwt.getClaim("sid").asString());
                    authorizationInfo.setEmail(jwt.getClaim("email").asString());
                    authorizationInfo.setName(jwt.getClaim("name").asString());
                    authorizationInfo.setGivenName(jwt.getClaim("given_name").asString());
                    authorizationInfo.setFamilyName(jwt.getClaim("family_name").asString());
                    return authorizationInfo;
                } catch (JWTVerificationException exception) {
                    //System.out.println("Token verification failed: " + exception.getMessage());
                } catch (Exception e) {
                    //System.out.println("Exception occurred: " + e.getMessage());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static PublicKey getPublicKeyFromJWK(String jwkJson) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> jwk = mapper.readValue(jwkJson, Map.class);
        BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(jwk.get("n")));
        BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(jwk.get("e")));

        RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }

    @Override
    protected void addAuthorizationHeaders(AuthorizationInfo authInfo, APIGatewayProxyRequestEvent request) {
        if (authInfo != null) {
            ////logger.log("authInfo = "+authInfo);
            request.getHeaders().put("X-UserId", authInfo.getUserId());
            request.getHeaders().put("X-Roles", String.join(",", authInfo.getRoles()));
            ////logger.log("request.getHeaders() X-UserId = "+request.getHeaders().get("X-UserId"));
            ////logger.log("request.getHeaders() X-Roles = "+request.getHeaders().get("X-Roles"));
        /*} else {
            ////logger.log("authInfo is null, cannot add authorization headers");*/
        }
    }
}