package com.inventarios.core;

import java.sql.SQLException;

public class DatabaseOperationException extends Exception {
    public DatabaseOperationException(String message, SQLException cause) {
        super(message, cause);
        RDSConexion.terminateActiveConnections();
    }

    public DatabaseOperationException(SQLException cause) {
        super(cause);
        RDSConexion.terminateActiveConnections();
    }
}
