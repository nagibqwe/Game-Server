/**
 * Auto generated, do not edit it
 *
 * on_hook_c配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_On_hook_c_Bean; 
import config.Cfg_On_hook_c_Load;

	
public final class Cfg_On_hook_c_Container extends ConfigBase<Cfg_On_hook_c_Bean> {
	
    /**
     * 初始化on_hook_c配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_On_hook_c_Container(){
        Cfg_On_hook_c_Load load = new Cfg_On_hook_c_Load();
        Map<Integer, Cfg_On_hook_c_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_On_hook_c_Bean[] newBeanArray(int size) {
        return new Cfg_On_hook_c_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_On_hook_c_Container manager;
        
        Singleton() {
            this.manager = new Cfg_On_hook_c_Container();
        }
        
        Cfg_On_hook_c_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_On_hook_c_Container的实例对象
     *
     * @return
     */
    public static Cfg_On_hook_c_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
