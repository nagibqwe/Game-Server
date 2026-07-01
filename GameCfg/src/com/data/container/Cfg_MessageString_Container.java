/**
 * Auto generated, do not edit it
 *
 * messagestring
 */
package com.data.container;

import com.data.ConfigBase;
import config.Cfg_MessageString_Load;
import java.util.Map;

/**
 *
 */
public final class Cfg_MessageString_Container extends ConfigBase<Object> {
    
    @SuppressWarnings({"deprecation"})
    public Cfg_MessageString_Container() {
        Cfg_MessageString_Load load = new Cfg_MessageString_Load();
        Map<Integer, Object> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Object[] newBeanArray(int size) {
        return new Object[size];
    }
    
}
