package com.game.yed;

import java.util.ArrayList;

/**
 * @author gaozhaoguang
 * @desc 堆栈类
 * @date Created on 2020/8/15 16:49
 **/
public class YedStack {
    //数据堆栈处理
    private final ArrayList<Token> paramStack = new ArrayList<>();
    //参数的数据缓存
    private final Object[][] args_tmp = {
            new Object[0],
            new Object[1],
            new Object[2],
            new Object[3],
            new Object[4],
            new Object[5],
            new Object[6],
            new Object[7],
            new Object[8],
            new Object[9],
            new Object[10]
    };

    //最后执行的方法名称
    private String lastFuncName = null;

    /**
     * 分配一个对象内存,count最大为10
     * @param count
     * @return
     */
    public Object[] AllocArray(int count){
        return args_tmp[count];
    }

    /**
     * 清空堆栈
     */
    public void Clear(){
        paramStack.clear();
    }

    public Token PopToken() {
        Token _ok = null;
        if (paramStack.size() > 0) {
            _ok = paramStack.get(paramStack.size() - 1);
            paramStack.remove(paramStack.size() - 1);
        }
        return _ok;
    }

    public Token PeekToken() {
        Token _ok = null;
        if (paramStack.size() > 0) {
            _ok = paramStack.get(paramStack.size() - 1);
        }
        return _ok;
    }

    public double PopNumber() {
        Token _ok = PopToken();
        double _number = 0;
        if (_ok != null) {
            _number = _ok.ValueNumber();
        }
        return _number;
    }

    public void PushToken(Token _ok) {
        paramStack.add(_ok);
    }

    public void PushParam(String str) {
        Token _ok = new Token();
        _ok.type = Token.TokenType.Param;
        _ok.ValueParam(str);
        paramStack.add(_ok);
    }

    public void PushString(String str) {
        Token _ok = new Token(str);
        paramStack.add(_ok);
    }

    public void PushNumber(double val) {
        Token _ok = new Token(val);
        paramStack.add(_ok);
    }

    public void PushBoolean(boolean val){
        if (val) {
            PushNumber(1);
        } else {
            PushNumber(0);
        }
    }

    public void PushObject(Object o) {
        if (o == null) {
            return;
        }
        try {
            if (o.getClass() == Integer.class) {
                int v = (Integer) o;
                PushNumber(v);
            } else if (o.getClass() == Short.class) {
                short v = (Short) o;
                PushNumber(v);
            } else if (o.getClass() == Character.class) {
                char v = (Character) o;
                PushNumber(v);
            } else if (o.getClass() == Float.class) {
                float v = (Float) o;
                PushNumber(v);
            } else if (o.getClass() == Long.class) {
                long v = (Long) o;
                PushNumber(v);
            } else if (o.getClass() == Boolean.class) {
                boolean v = (Boolean) o;
                PushNumber(v ? 1 : 0);
            } else {
                double v = (Double) o;
                PushNumber(v);
            }
            return;
        } catch (Exception e) {
        }

        try {
            String v = (String) o;
            PushString(v);
            return;
        } catch (Exception e) {
        }
        Exception e = new Exception("HandleReturn error o:" + o);
        e.printStackTrace();

        YedMgr.getInstance().getAiLogger().fatal(e.getMessage(), e);
    }

    public String getLastFuncName() {
        return lastFuncName;
    }

    public void setLastFuncName(String lastFuncName) {
        this.lastFuncName = lastFuncName;
    }
}
