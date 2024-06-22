package com.inventarios.core;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;

@ApplicationScoped
public class MiServicio {

    @Inject
    DataSource dataSource;

    public void hacerAlgoConBaseDeDatos() {
        try (Connection connection = dataSource.getConnection()) {
            // Usa la conexión para realizar operaciones con la base de datos
        } catch (SQLException e) {
            // Manejo de excepciones
            e.printStackTrace();
        }
    }
}
