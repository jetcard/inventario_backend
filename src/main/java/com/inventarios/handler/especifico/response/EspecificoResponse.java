package com.inventarios.handler.especifico.response;

import com.inventarios.model.Especifico;

import java.util.List;

public class EspecificoResponse {
    private List<Especifico> listaespecificos;
    public List<Especifico> getListaespecificos() {
        return listaespecificos;
    }
    public void setListaespecificos(List<Especifico> listaespecificos) {
        this.listaespecificos = listaespecificos;
    }
}
