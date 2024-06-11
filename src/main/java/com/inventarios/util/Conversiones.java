package com.inventarios.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Conversiones {
    public LocalDate convertirALocalDate(String fechaCompraStr) {
        if (fechaCompraStr == null || fechaCompraStr.isEmpty()) {
            return null; // Devuelve null si la cadena de fecha es nula o vacía
        }
        try {
            LocalDate fecha = LocalDate.parse(fechaCompraStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            return fecha;
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            // Manejar el error de formato de fecha aquí si es necesario
            return null; // Devuelve null si hay un error al analizar la fecha
        }
    }

    public String convertirAString(LocalDate fecha) {
        if (fecha == null) {
            return null; // Devuelve null si la fecha es nula
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fecha.format(formatter);
    }
}