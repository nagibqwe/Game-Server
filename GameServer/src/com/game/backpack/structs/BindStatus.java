package com.game.backpack.structs;

public enum BindStatus {
    NotBind(0),    // 非绑定
    Bind(1),       // 绑定
    All(2),        // 不检查，即绑定非绑定都行

    ;
    private final int value;

    BindStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public boolean compare(int status) {
        return this.value == status;
    }

    public boolean compare(BindStatus status) {
        return this.value == status.getValue();
    }

    public boolean getBind() {
        return !this.compare(NotBind);
    }

    /**
     * 1是绑定，0是非绑
     * @param bind
     * @return
     */
    public static boolean getBind(int bind) {
        // 使用更安全的方式
        return !(bind == NotBind.getValue());
    }

    /**
     * 1是绑定，0是非绑
     * @param bind
     * @return
     */
    public static BindStatus bind(int bind) {
        if (bind == 0)
            return NotBind;
        return Bind;
    }
}