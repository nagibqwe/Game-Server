/**
 * Auto generated, do not edit it
 *
 *
 */
package com.data.container;

import com.data.ConfigBase;
import config.Cfg_ItemChangeReason_Load;
import java.util.Map;

/**
 *
 */
public final class Cfg_ItemChangeReason_Container extends ConfigBase<Object> {
    
    @SuppressWarnings({"deprecation"})
    public Cfg_ItemChangeReason_Container() {
        Cfg_ItemChangeReason_Load load = new Cfg_ItemChangeReason_Load();
        Map<Integer, Object> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Object[] newBeanArray(int size) {
        return new Object[size];
    }


    /**
    * 用枚举来实现单例
    */
    private enum Singleton {

         INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
         Cfg_ItemChangeReason_Container manager;

         Singleton() {
         this.manager = new Cfg_ItemChangeReason_Container();
         }

         Cfg_ItemChangeReason_Container getProcessor() {
             return manager;
         }
    }

    /**
    * 获取Cfg_ItemChangeReason_Container的实例对象
    *
    * @return
    */
    public static Cfg_ItemChangeReason_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
    
}
