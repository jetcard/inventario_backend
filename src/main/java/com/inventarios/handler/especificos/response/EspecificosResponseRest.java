package com.inventarios.handler.especificos.response;

import com.inventarios.handler.ResponseRest;

public class EspecificosResponseRest extends ResponseRest {
    private EspecificosResponse especificosResponse = new EspecificosResponse();
    public EspecificosResponse getEspecificosResponse() {
        return especificosResponse;
    }
    public void setEspecificosResponse(EspecificosResponse especificosResponse) {
        this.especificosResponse = especificosResponse;
    }
}
