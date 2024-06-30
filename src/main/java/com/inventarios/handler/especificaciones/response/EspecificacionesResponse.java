package com.inventarios.handler.especificaciones.response;

import com.inventarios.model.Especificaciones;
import java.util.List;
public class EspecificacionesResponse {
    private List<Especificaciones> listaespecificacioness;
    public List<Especificaciones> getListaespecificacioness() {
        return listaespecificacioness;
    }
    public void setListaespecificacioness(List<Especificaciones> listaespecificacioness) {
        this.listaespecificacioness = listaespecificacioness;
    }
}
