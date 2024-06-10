package com.inventarios.handler.especificos.response;

import com.inventarios.model.Especificos;
import java.util.List;

public class EspecificosResponse {
    private List<Especificos> listaespecificoss;
    public List<Especificos> getListaespecificoss() {
        return listaespecificoss;
    }
    public void setListaespecificoss(List<Especificos> listaespecificoss) {
        this.listaespecificoss = listaespecificoss;
    }
}
