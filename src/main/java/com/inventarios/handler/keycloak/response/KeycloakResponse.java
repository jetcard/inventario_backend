package com.inventarios.handler.keycloak.response;

import com.inventarios.model.AuthorizationInfo;

import java.util.List;

public class KeycloakResponse {
    private List<AuthorizationInfo> authorizationList;

    public List<AuthorizationInfo> getAuthorizationList() {
        return authorizationList;
    }

    public void setAuthorizationList(List<AuthorizationInfo> authorizationList) {
        this.authorizationList = authorizationList;
    }
}
