package com.inventarios.handler.activo;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
/*
import com.auth0.jwt.JWTVerificationException;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
//import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.jwk.Jwk;
import com.auth0.jwt.jwk.JwkProvider;
import com.auth0.jwt.jwk.JwkProviderBuilder;
import com.auth0.jwt.jwk.SigningKeyNotFoundException;*/


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventarios.core.RDSConexion;
import java.math.BigInteger;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import com.inventarios.handler.activo.services.ReadActivoAbstractHandler;
import com.inventarios.model.AuthorizationInfo;
import com.inventarios.util.JWTUtils;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public class ReadActivoHandler extends ReadActivoAbstractHandler {
  protected Result<Record19<Long, Long, Long, Long, Long, Long, Long, Long, String, String, String, String, String, String, LocalDate, String, String, String, String>> read() throws SQLException {
    var dsl = RDSConexion.getDSL();
    return dsl.select(
                    ACTIVO_ID,
                    ACTIVO_RESPONSABLE_ID,
                    ACTIVO_ARTICULO_ID,
                    ACTIVO_TIPO_ID,
                    ACTIVO_GRUPO_ID,
                    ACTIVO_PROVEEDOR_ID,
                    ESPECIFICACIONES_ID,
                    ESPECIFICACIONES_ESPECIFICOID,
                    ESPECIFICACIONES_NOMBREESPECIFICO,
                    ESPECIFICACIONES_DESCRIPESPECIFICO,
                    ACTIVO_CODINVENTARIO,
                    ACTIVO_MODELO,
                    ACTIVO_MARCA,
                    ACTIVO_NROSERIE,
                    ACTIVO_FECHAINGRESO,
                    ACTIVO_FECHAINGRESOSTR,
                    ACTIVO_MONEDA,
                    ACTIVO_IMPORTE,
                    ACTIVO_DESCRIPCION
            )
            .from(ACTIVO_TABLE)
            .leftJoin(ESPECIFICACIONES_TABLE)
            .on(ACTIVO_ID.eq(ESPECIFICACIONES_ESPECIFICOID))
            .fetch();
  }

  @Override
  protected String mostrarCustodio(Long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    Record record = dsl.select(RESPONSABLE_TABLE_COLUMNA)
            .from(RESPONSABLE_TABLE)
            .where(DSL.field("id", Long.class).eq(id))
            .fetchOne();
    return record != null ? record.getValue(RESPONSABLE_TABLE_COLUMNA) : null;
  }
  @Override
  protected String mostrarArticulo(Long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    Record record = dsl.select(ARTICULO_TABLE_COLUMNA)
            .from(ARTICULO_TABLE)
            .where(DSL.field("id", Long.class).eq(id))
            .fetchOne();
    return record != null ? record.getValue(ARTICULO_TABLE_COLUMNA) : null;
  }
  @Override
  protected String mostrarTipoBien(Long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    Record record = dsl.select(TIPO_TABLE_COLUMNA)
            .from(TIPO_TABLE)
            .where(DSL.field("id", Long.class).eq(id))
            .fetchOne();
    return record != null ? record.getValue(TIPO_TABLE_COLUMNA) : null;
  }
  @Override
  protected String mostrarCategoria(Long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    Record record = dsl.select(GRUPO_TABLE_COLUMNA)
            .from(GRUPO_TABLE)
            .where(DSL.field("id", Long.class).eq(id))
            .fetchOne();
    return record != null ? record.getValue(GRUPO_TABLE_COLUMNA) : null;
  }
  @Override
  protected String mostrarProveedor(Long id) throws SQLException {
    var dsl = RDSConexion.getDSL();
    Record record = dsl.select(PROVEEDOR_TABLE_COLUMNA)
            .from(PROVEEDOR_TABLE)
            .where(DSL.field("id", Long.class).eq(id))
            .fetchOne();
    return record != null ? record.getValue(PROVEEDOR_TABLE_COLUMNA) : null;
  }

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
/*
  public static void main(String[] args) {
    try {
      String token = "your-jwt-token-here";
      String keyId = JWTUtils.getKeyIdFromToken(token);

      if (keyId == null) {
        System.err.println("No Key ID found in the token");
        return;
      }

      URL url = new URL("https://examensolucion-u8698.vm.elestio.app/realms/Inventario/protocol/openid-connect/certs");
      JwkProvider provider = new JwkProviderBuilder(url)
              .cached(10, 24, TimeUnit.HOURS) // cache up to 10 jwks for 24 hours
              .rateLimited(10, 1, TimeUnit.MINUTES) // if not cached, only allow 10 requests per minute
              .build();

      Jwk jwk = provider.get(keyId);
      RSAPublicKey publicKey = (RSAPublicKey) jwk.getPublicKey();

      Algorithm algorithm = Algorithm.RSA256(publicKey, null);
      JWTVerifier verifier = JWT.require(algorithm)
              .withIssuer("https://examensolucion-u8698.vm.elestio.app/realms/Inventario")
              .build();

      DecodedJWT jwt = verifier.verify(token);
      System.out.println("Token is valid!");
      System.out.println("Subject: " + jwt.getSubject());

    } catch (JWTVerificationException e) {
      e.printStackTrace();
      System.err.println("Invalid token: " + e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Error: " + e.getMessage());
    }
  }*/


  /*@Override
  protected AuthorizationInfo validateAuthToken(String authToken) {
    if (authToken != null) {
      try {
        // JwkProvider para obtener el JWK de Keycloak
        / *JwkProvider provider = new JwkProviderBuilder("https://examensolucion-u8698.vm.elestio.app/realms/Inventario/protocol/openid-connect/certs")
                .cached(10, 24, TimeUnit.HOURS) // cache hasta 10 JWKs durante 24 horas
                .build();* /
        // Decodificar el JWT sin verificar para obtener el ID de clave (kid)
        ///DecodedJWT jwt = JWT.decode(authToken);
        ///String kid = jwt.getKeyId();
        // Obtener el JWK del proveedor
        ///Jwk jwk = provider.get(kid);
        //Funciona:
        String jwkJson = "{\n" +
                "  \"e\": \"AQAB\",\n" +
                "  \"kty\": \"RSA\",\n" +
                "  \"n\": \"2QeQWgy8P5LeEmCO45V_CajWeKqsCmCCzVRNcOet-NC_smqelugLYlUYLXWFowqJzOcBb3Xt1B-of8MeMd-wOtiRdj2XCwLY6D3seZHp22gr9kcdXqU0S-eplVfdlW9e_xeRQyS8sqPfuNzQGDOiUSfOEpyMgErahUec5DHdACMGiYw9UWBvR6Y2sthfm8K2H3Pc8ymIyzSrDYrobzI4g7Hu1YmXFEZI7mB51rkFEO5HXV4r78IkYKvUXYuxnHN3s4fnuyzDhMucht99Eun6qI5Fk-NyDvZ62NNaSmxpL5HldiFLPH_skzCOpxSeB-qBAUasHCQXXdcl5xFAZPb8iw\"\n" +
                "}";

        try {
          RSAPublicKey publicKey = getPublicKeyFromJWK(jwkJson);
          ///PublicKey publicKey = getPublicKeyFromJWK(jwkJson);
          ///PublicKey publicKey = jwk.getPublicKey();
          Algorithm algorithm0 = Algorithm.RSA256((RSAPublicKey) publicKey, null);

          Algorithm algorithm = Algorithm.RSA256(publicKey, null);


          JWTVerifier verifier = JWT.require(algorithm)
                  .withIssuer("https://examensolucion-u8698.vm.elestio.app/realms/Inventario")
                  .build();

          DecodedJWT jwt = verifier.verify(authToken);
          ///jwt = verifier.verify(authToken);
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
              System.out.println("Roles: ");
              for (String role : roles) {
                System.out.println(role);
                // Ejemplo de verificación de rol específico
                if ("user".equals(role)) {
                  System.out.println("El usuario tiene el rol 'user'.");
                  // Aquí agregar lógica adicional relacionada con el rol 'user'
                }
                if ("admin".equals(role)) {
                  System.out.println("El usuario tiene el rol 'admin'.");
                  // Aquí agregar lógica adicional relacionada con el rol 'admin'
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
          System.out.println("Token verification failed: " + exception.getMessage());
        } catch (Exception e) {
          System.out.println("Exception occurred: " + e.getMessage());
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }*/
  // "  \"n\": \"tf1f25bAtZMGbwXkD_UNZ1z9eH91rNeZ-MIWeiGpE-g0u1Y6lBKi-vI6O0nFGTcilCTR6tcqXu9Ah6cwxPC6n0ORCUFd_VXRzjGHzrU5_Kofhb8_yPYFaAp3JAuvrj7PJNnY7RUZZibBuBpGIesrdwuDdBDprR4VzKuSKl7HZCMcAkhqNQjaSNt1UhwDb1mV22uu4NfjqaGSfp2LnRWnpUYTGZobTRE2S5kAw73kefewPCHiooryCZK_69NK5TAZWXWf-YPpFtdwmf7mFggonpWWrCuTWikEKicwdL6xn6oWYeuVlM80M2hUhNJNUSLB7fDHYli5NRqky315bsjvhw\"\n" +

  @Override
  protected AuthorizationInfo validateAuthToken(String authToken) {
    if (authToken != null) {
      try {
        String jwkJson = "{\n" +
                "  \"e\": \"AQAB\",\n" +
                "  \"kty\": \"RSA\",\n" +
                "  \"n\": \"2QeQWgy8P5LeEmCO45V_CajWeKqsCmCCzVRNcOet-NC_smqelugLYlUYLXWFowqJzOcBb3Xt1B-of8MeMd-wOtiRdj2XCwLY6D3seZHp22gr9kcdXqU0S-eplVfdlW9e_xeRQyS8sqPfuNzQGDOiUSfOEpyMgErahUec5DHdACMGiYw9UWBvR6Y2sthfm8K2H3Pc8ymIyzSrDYrobzI4g7Hu1YmXFEZI7mB51rkFEO5HXV4r78IkYKvUXYuxnHN3s4fnuyzDhMucht99Eun6qI5Fk-NyDvZ62NNaSmxpL5HldiFLPH_skzCOpxSeB-qBAUasHCQXXdcl5xFAZPb8iw\"\n" +
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
          System.out.println("claim_realm_access = " + claim_realm_access.asMap());
          Map<String, Object> realmAccess = claim_realm_access.asMap();
          List<String> roles = null;
          if (realmAccess != null) {
            Object claim_realm_roles =  realmAccess.get("roles");
            System.out.println("roles = " + claim_realm_roles);
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
                //System.out.println(role);
                // Ejemplo de verificación de rol específico
                if ("user".equals(role)) {
                  System.out.println("El usuario tiene el rol 'user'.");
                  // Aquí puedes agregar lógica adicional relacionada con el rol 'user'
                }
              }
              authorizationInfo.setRoles(roles);
            } else {
              System.out.println("No roles found.");
            }
          }
          authorizationInfo.setUserId(jwt.getClaim("sid").asString());
          authorizationInfo.setEmail(jwt.getClaim("email").asString());
          authorizationInfo.setName(jwt.getClaim("name").asString());
          authorizationInfo.setGivenName(jwt.getClaim("given_name").asString());
          authorizationInfo.setFamilyName(jwt.getClaim("family_name").asString());
          return authorizationInfo;
        } catch (JWTVerificationException exception) {
          System.out.println("Token verification failed: " + exception.getMessage());
        } catch (Exception e) {
          System.out.println("Exception occurred: " + e.getMessage());
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

  private static RSAPublicKey getPublicKeyFromJWK1(String jwkJson) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode jwkNode = mapper.readTree(jwkJson);
    String n = jwkNode.get("n").asText();
    String e = jwkNode.get("e").asText();

    byte[] nBytes = Base64.getUrlDecoder().decode(n);
    byte[] eBytes = Base64.getUrlDecoder().decode(e);

    RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(
            new java.math.BigInteger(1, nBytes),
            new java.math.BigInteger(1, eBytes)
    );
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
  }
  @Override
  protected void addAuthorizationHeaders(AuthorizationInfo authInfo, APIGatewayProxyRequestEvent request) {
    if (authInfo != null) {
      System.out.println("authInfo = "+authInfo);
      request.getHeaders().put("X-UserId", authInfo.getUserId());
      request.getHeaders().put("X-Roles", String.join(",", authInfo.getRoles()));
      System.out.println("request.getHeaders() X-UserId = "+request.getHeaders().get("X-UserId"));
      System.out.println("request.getHeaders() X-Roles = "+request.getHeaders().get("X-Roles"));
    } else {
      System.out.println("authInfo is null, cannot add authorization headers");
    }
  }
}