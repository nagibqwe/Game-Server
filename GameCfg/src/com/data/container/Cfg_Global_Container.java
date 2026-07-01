/**
 * Auto generated, do not edit it
 *
 * 全局表加载
 */
package com.data.container;

import com.data.ConfigBase;
import config.Cfg_Global_Load;
import java.util.Map;

/**
 *
 */
public final class Cfg_Global_Container extends ConfigBase<Object> {
    
    @SuppressWarnings({"deprecation"})
    public Cfg_Global_Container() {
        Cfg_Global_Load load = new Cfg_Global_Load();
        Map<Integer, Object> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Object[] newBeanArray(int size) {
        return new Object[size];
    }
    
}
