package com.inventarios.handler.marca.response;

import com.inventarios.model.Marca;
import java.util.List;

public class MarcaResponse {
    private List<Marca> listamarcaes;

    public List<Marca> getListamarcaes() {
        return listamarcaes;
    }

    public void setListamarcaes(List<Marca> listamarcaes) {
        this.listamarcaes = listamarcaes;
    }
}
