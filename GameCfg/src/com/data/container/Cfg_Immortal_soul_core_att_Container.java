/**
 * Auto generated, do not edit it
 *
 * immortal_soul_core_att配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Immortal_soul_core_att_Bean; 
import config.Cfg_Immortal_soul_core_att_Load;

	
public final class Cfg_Immortal_soul_core_att_Container extends ConfigBase<Cfg_Immortal_soul_core_att_Bean> {
	
    /**
     * 初始化immortal_soul_core_att配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Immortal_soul_core_att_Container(){
        Cfg_Immortal_soul_core_att_Load load = new Cfg_Immortal_soul_core_att_Load();
        Map<Integer, Cfg_Immortal_soul_core_att_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Immortal_soul_core_att_Bean[] newBeanArray(int size) {
        return new Cfg_Immortal_soul_core_att_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Immortal_soul_core_att_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Immortal_soul_core_att_Container();
        }
        
        Cfg_Immortal_soul_core_att_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Immortal_soul_core_att_Container的实例对象
     *
     * @return
     */
    public static Cfg_Immortal_soul_core_att_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
