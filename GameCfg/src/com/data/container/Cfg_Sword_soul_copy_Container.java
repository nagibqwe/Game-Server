/**
 * Auto generated, do not edit it
 *
 * sword_soul_copy配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Sword_soul_copy_Bean; 
import config.Cfg_Sword_soul_copy_Load;

	
public final class Cfg_Sword_soul_copy_Container extends ConfigBase<Cfg_Sword_soul_copy_Bean> {
	
    /**
     * 初始化sword_soul_copy配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Sword_soul_copy_Container(){
        Cfg_Sword_soul_copy_Load load = new Cfg_Sword_soul_copy_Load();
        Map<Integer, Cfg_Sword_soul_copy_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Sword_soul_copy_Bean[] newBeanArray(int size) {
        return new Cfg_Sword_soul_copy_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Sword_soul_copy_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Sword_soul_copy_Container();
        }
        
        Cfg_Sword_soul_copy_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Sword_soul_copy_Container的实例对象
     *
     * @return
     */
    public static Cfg_Sword_soul_copy_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
