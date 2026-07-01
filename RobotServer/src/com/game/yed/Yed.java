package com.game.yed;

import java.util.HashMap;

public class Yed {
    public static YedLoader loader = null;
    public YedState nextState = null;
    public YedState curState = null;
    public YedData data = null;
    public String name = "";
    private boolean loaded = false;
    private Yed child = null;
    private HashMap<String, YedData> datas = new HashMap<String, YedData>();

    public Yed getChild() {
        return child;
    }

    public void Load(String _name) {
        if (!loaded) {
            loaded = true;
            name = _name;
            data = datas.get(_name);
            if (data == null) {
                if (loader != null) {
                    //loader(_name,OnLoadedYed);
                    boolean ret = loader.Invoke(_name, new OnLoaded() {
                        @Override
                        public void Invoke(String name, byte[] bytes) throws Exception {
                            OnLoadedYed(name, bytes);
                        }
                    });
                    if (!ret) {
                        System.out.println(_name + " 加载失败");
                    }
                }
            } else {
                Reset();
            }
        } else {
            if (child == null) {
                child = new Yed();
            }
            child.Load(_name);
        }
    }

    private void OnLoadedYed(String _name, byte[] _bytes) throws Exception {
        name = _name;
        data = datas.get(_name);
        if (data == null) {
            BinaryBuffer buffer = BinaryBuffer.Allocate(_bytes, Endian.Little);
            data = new YedData();
            try {
                data.Load(buffer);
                if (data.numStates < 1) {
                    throw new Exception("名字是:" + _name + "的AI加载出来没状态");
                }
            } catch (Exception e) {
                throw new Exception("load yed error " + _name, e);
            }
            datas.put(name, data);
        }
        Reset();
    }

    public void CleanBeforeChange() {
        child = null;
    }

    public void Reset() {
        if (data != null) {
            nextState = data.Entry();
        }
        curState = null;
    }

    public interface OnLoaded {
        void Invoke(String name, byte[] bytes) throws Exception;
    }

    public interface YedLoader {
        boolean Invoke(String name, OnLoaded onloaded);
    }
}
