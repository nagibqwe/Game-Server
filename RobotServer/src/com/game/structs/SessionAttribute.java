package com.game.structs;

public enum SessionAttribute {

    PLAYER("player"),
    USER_ID("userid"),
    ;

    private final String value;

    SessionAttribute(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
