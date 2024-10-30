package io.digitalbinary.camera.sdk.messages;

public class SuccessEvent {
    private final String name;

    public SuccessEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
