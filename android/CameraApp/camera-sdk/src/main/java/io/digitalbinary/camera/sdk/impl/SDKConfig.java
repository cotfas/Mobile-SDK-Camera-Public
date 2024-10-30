package io.digitalbinary.camera.sdk.impl;

class SDKConfig {
    private static SDKConfig instance;
    private boolean isAuthenticated;

    private SDKConfig() {
        isAuthenticated = false;
    }

    static synchronized SDKConfig getInstance() {
        if (instance == null) {
            instance = new SDKConfig();
        }
        return instance;
    }

    boolean isAuthenticated() {
        return isAuthenticated;
    }

    void setAuthenticated(boolean authenticated) {
        this.isAuthenticated = authenticated;
    }
}