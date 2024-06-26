package com.inventarios.handler.especifico;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.especifico.services.ReadEspecificoAbstractHandler;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import com.inventarios.model.AuthorizationInfo;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public class ReadEspecificoHandler extends ReadEspecificoAbstractHandler {
  protected Result<Record18<Long, Long, Long, Long, Long, Long, Long, Long, String, String, String, String, String, LocalDate, String, String, String, String>> read() throws SQLException {
    var dsl = RDSConexion.getDSL();
    /*return dsl.select(
                    ESPECIFICO_ID,
                    ESPECIFICO_RESPONSABLE_ID,
                    ESPECIFICO_ARTICULO_ID,
                    ESPECIFICO_TIPO_ID,
                    ESPECIFICO_GRUPO_ID,
                    ESPECIFICO_PROVEEDOR_ID,
                    ESPECIFICOS_ID,
                    ESPECIFICOS_ESPECIFICOID,
                    ESPECIFICOS_NOMBREESPECIFICO,
                    ESPECIFICO_CODINVENTARIO,
                    ESPECIFICO_MODELO,
                    ESPECIFICO_MARCA,
                    ESPECIFICO_NROSERIE,
                    ESPECIFICO_FECHAINGRESO,
                    ESPECIFICO_FECHAINGRESOSTR,
                    ESPECIFICO_MONEDA,
                    ESPECIFICO_IMPORTE,
                    ESPECIFICO_DESCRIPCION
            )
            .from(ESPECIFICO_TABLE)
            .leftJoin(ESPECIFICOS_TABLE).on(ESPECIFICO_ID.eq(ESPECIFICOS_ESPECIFICOID))
            .leftJoin(PROVEEDOR_TABLE).on(ESPECIFICO_PROVEEDOR_ID.eq(PROVEEDOR_ID))
            .fetch();*/
    return dsl.select(
                    ESPECIFICO_ID,
                    ESPECIFICO_RESPONSABLE_ID,
                    ESPECIFICO_ARTICULO_ID,
                    ESPECIFICO_TIPO_ID,
                    ESPECIFICO_GRUPO_ID,
                    ESPECIFICO_PROVEEDOR_ID,
                    ESPECIFICOS_ID,
                    ESPECIFICOS_ESPECIFICOID,
                    ESPECIFICOS_NOMBREESPECIFICO,
                    ESPECIFICO_CODINVENTARIO,
                    ESPECIFICO_MODELO,
                    ESPECIFICO_MARCA,
                    ESPECIFICO_NROSERIE,
                    ESPECIFICO_FECHAINGRESO,
                    ESPECIFICO_FECHAINGRESOSTR,
                    ESPECIFICO_MONEDA,
                    ESPECIFICO_IMPORTE,
                    ESPECIFICO_DESCRIPCION
            )
            .from(ESPECIFICO_TABLE)
            .leftJoin(ESPECIFICOS_TABLE)
            .on(ESPECIFICO_ID.eq(ESPECIFICOS_ESPECIFICOID))
            .fetch();
  }

  @Override
  protected String mostrarResponsable(Long id) throws SQLException {
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
  protected String mostrarGrupo(Long id) throws SQLException {
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
                  .withIssuer("https://examensolucion-u8698.vm.elestio.app/realms/ExamenSolucion")
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
              System.out.println("Roles: ");
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