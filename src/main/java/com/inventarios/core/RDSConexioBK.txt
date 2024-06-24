package com.inventarios.core;

import com.amazonaws.secretsmanager.sql.AWSSecretsManagerPostgreSQLDriver;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import jakarta.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@ApplicationScoped
public class RDSConexion {
  // Define your secrets manager secret ID
  private static final String SECRET_ID = "RDSSecret-i7hLv6JBmB3s";

  @Produces
  @Singleton
  DataSource buildDataSource() {
    return DataSource.from("postgresql", this::dataSource);
  }

  private AgroalDataSource dataSource() {
    var secretsManagerDriver = new AWSSecretsManagerPostgreSQLDriver();
    var config = new HikariConfig();

    // AWS Secrets Manager configuration
    Properties info = new Properties();
    info.put("secretsManagerSecretId", SECRET_ID);

    config.setDataSourceProperties(info);
    config.setJdbcUrl("jdbc-secretsmanager:postgresql://sam-app-rdsinstance-yzmysskmudjz.ctcosak24j76.ap-southeast-2.rds.amazonaws.com/basededatos");
    config.setDriverClassName(AWSSecretsManagerPostgreSQLDriver.class.getName());
    config.setMaximumPoolSize(50);  // Adjust based on your needs
    config.setMinimumIdle(25);      // Adjust based on your needs

    return new HikariDataSource(config);
  }

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc-secretsmanager:postgresql://your_db_endpoint/db_name", info);
  }

  public static DSLContext getDSL() throws SQLException {
    return (DSL.using(getConnection(), SQLDialect.POSTGRES));
  }
}



/*import com.amazonaws.secretsmanager.sql.AWSSecretsManagerPostgreSQLDriver;
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
  private static HikariDataSource dataSource;

  static {
    System.setProperty("software.amazon.awssdk.http.service.impl",
            "software.amazon.awssdk.http.urlconnection.UrlConnectionSdkHttpService");
    var driver = new AWSSecretsManagerPostgreSQLDriver();
    info = new Properties();
    //info.put("user", rdsSecretArn());
    info.put("secretsManagerSecretId", rdsSecretArn());
/*
    //Pool de conexiones
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc-secretsmanager:postgresql://" + rdsEndpoint() + "/" + rdsDatabase());
    config.setDriverClassName(AWSSecretsManagerPostgreSQLDriver.class.getName());
    config.setMaximumPoolSize(50);  // Ajusta este valor según tus necesidades10
    config.setUsername(rdsUserDB());
    config.setPassword(rdsPassDB());
    config.setDataSourceProperties(info);
    config.setMinimumIdle(25); // Número mínimo de conexiones inactivas en el pool5
    config.setIdleTimeout(30000); // Tiempo máximo de inactividad antes de cerrar una conexión
    config.setConnectionTimeout(30000); // Tiempo máximo de espera para obtener una conexión del pool
    config.setMaxLifetime(1800000); // Tiempo máximo de vida de una conexión
    dataSource = new HikariDataSource(config);*/
  }
/*
  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }*/

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
    return "basededatos";
    //return System.getenv(DATABASE_NAME_ENV);
  }

  public static String rdsSecretArn() {
    return "arn:aws:secretsmanager:ap-southeast-2:905418357111:secret:RDSSecret-i7hLv6JBmB3s-ZL0JcV";
    //return System.getenv(POSTGRES_SECRET_ARN_ENV);
  }

  public static String rdsEndpoint() {
    return "sam-app-rdsinstance-yzmysskmudjz.ctcosak24222.ap-southeast-2.rds.amazonaws.com";
    //return System.getenv(DB_ENDPOINT);
  }

  public static String rdsUserDB() {
    return "postgres";
    //return System.getenv(DB_USER);
  }

  public static String rdsPassDB() {
    return "1234567890";
    //return System.getenv(DB_PASS);
  }

  public static DSLContext getDSL() throws SQLException {
    return (DSL.using(getConnection(), SQLDialect.POSTGRES));
  }


  public static void main(String[] args) throws SQLException {
    DSLContext dslContext = RDSConexion.getDSL();
    if (dslContext != null) {
      System.out.println("Conexión exitosa");
    } else {
      System.out.println("Error en la conexión");
    }
  }*/

}


/*import com.amazonaws.secretsmanager.sql.AWSSecretsManagerPostgreSQLDriver;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;

public class RDSConexion {

  private static final HikariDataSource dataSource;

  static {
    // Configuración de HikariCP
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:postgresql://" + rdsEndpoint() + "/" + rdsDatabase());
    config.setUsername(rdsUserDB());
    config.setPassword(rdsPassDB());
    config.setDriverClassName(AWSSecretsManagerPostgreSQLDriver.class.getName());
    config.setMaximumPoolSize(50);  // Ajusta este valor según tus necesidades
    config.setMinimumIdle(25); // Número mínimo de conexiones inactivas en el pool
    config.setIdleTimeout(30000); // Tiempo máximo de inactividad antes de cerrar una conexión
    config.setConnectionTimeout(30000); // Tiempo máximo de espera para obtener una conexión del pool
    config.setMaxLifetime(1800000); // Tiempo máximo de vida de una conexión

    dataSource = new HikariDataSource(config);
  }

  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  public static DSLContext getDSL() throws SQLException {
    return DSL.using(getConnection(), SQLDialect.POSTGRES);
  }

  public static void closeDataSource() {
    if (dataSource != null) {
      dataSource.close();
    }
  }

  public static String rdsDatabase() {
    return "basededatos";
  }

  public static String rdsEndpoint() {
    return "sam-app-rdsinstance-yzmysskmudjz.ctcosak24j76.ap-southeast-2.rds.amazonaws.com";
  }

  public static String rdsUserDB() {
    return "postgres";
    //return System.getenv(DB_USER);
  }

  public static String rdsPassDB() {
    return "1234567890";
    //return System.getenv(DB_PASS);
  }

  public static void main(String[] args) {
    try {
      DSLContext dslContext = RDSConexion.getDSL();
      if (dslContext != null) {
        System.out.println("Conexión exitosa");
      } else {
        System.out.println("Error en la conexión");
      }
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
      e.printStackTrace();
    } catch (Exception e) {
      System.err.println("Exception: " + e.getMessage());
      e.printStackTrace();
    } finally {
      closeDataSource();
    }
  }
}
*/