package com.game.yed;

/**
 * Created by huhu on 2017/7/28.
 */

public class Token {
    public static Token zero = new Token(Double.valueOf(0));
    public TokenType type = TokenType.None;
    private Object val = null;

    public Token() {

    }

    public Token(Double v) {
        type = TokenType.Number;
        val = v;
    }

    public Token(String v) {
        type = TokenType.String;
        val = v;
    }

    public boolean IsNumber() {
        return type == TokenType.Number;
    }

    public boolean IsString() {
        return type == TokenType.String;
    }

    public boolean IsParam() {
        return type == TokenType.Param;
    }

    public boolean IsEmpty() {
        if (type == TokenType.Number) {
            return (Double) val == 0;
        } else if (type == TokenType.String) {
            return (String) val == "";
        } else {
            return val == null;
        }
    }

    public Double ValueNumber() {
        if (type == TokenType.Number) {
            return (Double) val;
        }
        return Double.valueOf(0);
    }

    public void ValueNumber(Double value) {
        if (type == TokenType.Number) {
            val = value;
        }
    }

    public String ValueString() {
        if (type == TokenType.String) {
            return (String) val;
        }
        return "";
    }

    public void ValueString(String value) {
        if (type == TokenType.String) {
            val = value;
        }
    }

    public Object ValueParam() {
        return val;
    }

    public void ValueParam(Object value) {
        if (type == TokenType.Param) {
            val = value;
        }
    }

    public enum TokenType {
        None,
        Number,
        String,
        Param
    }
}