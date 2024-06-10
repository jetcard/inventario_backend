package com.inventarios.handler.especifico.response;

import com.inventarios.handler.ResponseRest;
public class EspecificoResponseRest extends ResponseRest {
    private EspecificoResponse especificoResponse = new EspecificoResponse();
    public EspecificoResponse getEspecificoResponse() {
        return especificoResponse;
    }
    public void setEspecificoResponse(EspecificoResponse especificoResponse) {
        this.especificoResponse = especificoResponse;
    }
}
