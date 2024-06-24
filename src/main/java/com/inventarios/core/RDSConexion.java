
package com.inventarios.core;

import com.amazonaws.secretsmanager.sql.AWSSecretsManagerPostgreSQLDriver;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class RDSConexion {
  public static final String DATABASE_NAME_ENV = "DBName";
  public static final String POSTGRES_SECRET_ARN_ENV = "RDSSecretArn";
  public static final String DB_ENDPOINT = "DBEnpoint";
  public static final String DB_USER = "user";
  public static final String DB_PASS = "pass";

  static final Properties info;

  static {
    System.setProperty("software.amazon.awssdk.http.service.impl",
            "software.amazon.awssdk.http.urlconnection.UrlConnectionSdkHttpService");
    var driver = new AWSSecretsManagerPostgreSQLDriver();
    info = new Properties();
    info.put("user", rdsSecretArn());
  }
  public static Connection getConnection() {
    try {
      final String dbURL = "jdbc-secretsmanager:postgresql://" + rdsEndpoint() + "/" + rdsDatabase();
      return DriverManager.getConnection(dbURL, info);
    } catch (SQLException se) {
      System.err.println(se.getMessage());
      return null;
    }
  }

  public static String rdsDatabase() {
    //return "basededatos";
    return System.getenv(DATABASE_NAME_ENV);
  }

  public static String rdsSecretArn() {
    //return "arn:aws:secretsmanager:ap-southeast-2:905418357889:secret:RDSSecret-9Y92Yutym6Nn-pHmSIO";
    return System.getenv(POSTGRES_SECRET_ARN_ENV);
  }

  public static String rdsEndpoint() {
    //return "sam-app-rdsinstance-9h2zgl16cgmg.ctcosak24j76.ap-southeast-2.rds.amazonaws.com";
    return System.getenv(DB_ENDPOINT);
  }

  public static String rdsUserDB() {
    //return "postgres";
    return System.getenv(DB_USER);
  }

  public static String rdsPassDB() {
    //return "1234567890";
    return System.getenv(DB_PASS);
  }

  public static DSLContext getDSL() {
    return (DSL.using(getConnection(), SQLDialect.POSTGRES));
  }

}
/*Pruebas
public class RDSConexion {
  public static final String DATABASE_NAME_ENV = "DBName";
  public static final String POSTGRES_SECRET_ARN_ENV = "RDSSecretArn";
  public static final String DB_ENDPOINT = "DBEnpoint";
  public static final String DB_USER = "user";
  public static final String DB_PASS = "pass";
  static final Properties info;
  private static HikariDataSource dataSource;

  static {
    System.setProperty("software.amazon.awssdk.http.service.impl",
      "software.amazon.awssdk.http.urlconnection.UrlConnectionSdkHttpService");
    var driver = new AWSSecretsManagerPostgreSQLDriver();
    info = new Properties();
    info.put("user", rdsSecretArn());

    //Pool de conexiones
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc-secretsmanager:postgresql://" + rdsEndpoint() + "/" + rdsDatabase());
    config.setDriverClassName(AWSSecretsManagerPostgreSQLDriver.class.getName());
    config.setMaximumPoolSize(10);  // Ajusta este valor segÃºn tus necesidades
    config.setUsername(rdsUserDB());
    config.setPassword(rdsPassDB());
    config.setDataSourceProperties(info);
    config.setMinimumIdle(5); // NÃºmero mÃ­nimo de conexiones inactivas en el pool
    config.setIdleTimeout(30000); // Tiempo mÃ¡ximo de inactividad antes de cerrar una conexiÃ³n
    config.setConnectionTimeout(30000); // Tiempo mÃ¡ximo de espera para obtener una conexiÃ³n del pool
    config.setMaxLifetime(1800000); // Tiempo mÃ¡ximo de vida de una conexiÃ³n
    dataSource = new HikariDataSource(config);
  }

  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  / *public static Connection getConnection() {
    try {
      final String dbURL = "jdbc-secretsmanager:postgresql://" + rdsEndpoint() + "/" + rdsDatabase();
      return DriverManager.getConnection(dbURL, info);
    } catch (SQLException se) {
      System.err.println(se.getMessage());
      return null;
    }
  }* /

  public static String rdsDatabase() {
    //return "basededatos";
    return System.getenv(DATABASE_NAME_ENV);
  }

  public static String rdsSecretArn() {
    //return "arn:aws:secretsmanager:ap-southeast-2:905418357889:secret:RDSSecret-FypwSALsXwRv-7s3vvR";
    return System.getenv(POSTGRES_SECRET_ARN_ENV);
  }

  public static String rdsEndpoint() {
    //return "sam-app-rdsinstance-uutixd0j7zlb.ctcosak24j76.ap-southeast-2.rds.amazonaws.com";
    return System.getenv(DB_ENDPOINT);
  }

  public static String rdsUserDB() {
    //return "postgres";
    return System.getenv(DB_USER);
  }

  public static String rdsPassDB() {
    //return "1234567890";
    return System.getenv(DB_PASS);
  }

  public static DSLContext getDSL() throws SQLException {
    return (DSL.using(getConnection(), SQLDialect.POSTGRES));
  }

}*/