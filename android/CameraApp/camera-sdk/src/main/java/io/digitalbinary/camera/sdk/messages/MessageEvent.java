package io.digitalbinary.camera.sdk.messages;

public class MessageEvent {
    private final String name;

    public MessageEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
