package com.inventarios.core;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class RDSConexion {
  public static final String DATABASE_NAME_ENV = "DBName";
  public static final String POSTGRES_SECRET_ARN_ENV = "RDSSecretArn";
  public static final String DB_ENDPOINT = "DBEnpoint";
  public static final String DB_USER = "user";
  public static final String DB_PASS = "pass";
  private static HikariDataSource dataSource;

  static {
    initDataSource();
  }
  private static void initDataSource() {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:postgresql://" + rdsEndpoint() + "/" + rdsDatabase());
    config.setUsername(rdsUserDB());
    config.setPassword(rdsPassDB());
    config.setMaximumPoolSize(150);
    config.setUsername(rdsUserDB());
    config.setPassword(rdsPassDB());
    config.setMinimumIdle(15); // Número máximo de conexiones inactivas en el pool
    config.setIdleTimeout(30000); // Tiempo máximo de inactividad antes de cerrar una conexión
    config.setConnectionTimeout(30000); // Tiempo máximo de espera para obtener una conexión del pool
    config.setMaxLifetime(1800000);

    dataSource = new HikariDataSource(config);
  }
  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }
  public static String rdsDatabase() {
    //return "basededatos";
    return System.getenv(DATABASE_NAME_ENV);
  }
  public static String rdsSecretArn() {
    //return "arn:aws:secretsmanager:ap-southeast-2:905418357889:secret:RDSSecret-j511ScBfxQT5-xT9Wwm";
    return System.getenv(POSTGRES_SECRET_ARN_ENV);
  }
  public static String rdsEndpoint() {
    //return "sam-app-rdsinstance-k8vqxmtu1kkm.ctcosak24j76.ap-southeast-2.rds.amazonaws.com";
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
    return DSL.using(getConnection(), SQLDialect.POSTGRES);
  }

  public static void terminateActiveConnections() {
    Connection conn = null;
    Statement stmt = null;

    try {
      conn = getConnection();
      stmt = conn.createStatement();

      String sql = "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = '" + rdsDatabase() + "' AND pid <> pg_backend_pid();";
      stmt.execute(sql);

      System.out.println("Consultas para terminar conexiones ejecutadas correctamente.");
    } catch (SQLException e) {
      System.out.println("Error al ejecutar la consulta para terminar conexiones: " + e.getMessage());
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
      } catch (SQLException e) {
        System.out.println("Error al cerrar Statement: " + e.getMessage());
      }

      try {
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException e) {
        System.out.println("Error al cerrar conexión: " + e.getMessage());
      }
    }
  }
}