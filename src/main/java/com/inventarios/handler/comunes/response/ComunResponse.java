package com.inventarios.handler.comunes.response;

import com.inventarios.model.Comun;
import java.util.List;

public class ComunResponse {
    private List<Comun> listacomunes;

    public List<Comun> getListacomunes() {
        return listacomunes;
    }

    public void setListacomunes(List<Comun> listacomunes) {
        this.listacomunes = listacomunes;
    }
}
