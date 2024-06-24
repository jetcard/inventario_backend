package com.inventarios.handler.keycloak.response;

import com.inventarios.handler.ResponseRest;

public class KeycloakResponseRest extends ResponseRest {
    private KeycloakResponse keycloakResponse=new KeycloakResponse();

    public KeycloakResponse getKeycloakResponse() {
        return keycloakResponse;
    }
    public void setKeycloakResponse(KeycloakResponse keycloakResponse) {
        this.keycloakResponse = keycloakResponse;
    }
}