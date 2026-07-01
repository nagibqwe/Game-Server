/**
 * Auto generated, do not edit it
 *
 * shielding_symbol配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Shielding_symbol_Bean; 
import config.Cfg_Shielding_symbol_Load;

	
public final class Cfg_Shielding_symbol_Container extends ConfigBase<Cfg_Shielding_symbol_Bean> {
	
    /**
     * 初始化shielding_symbol配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Shielding_symbol_Container(){
        Cfg_Shielding_symbol_Load load = new Cfg_Shielding_symbol_Load();
        Map<Integer, Cfg_Shielding_symbol_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Shielding_symbol_Bean[] newBeanArray(int size) {
        return new Cfg_Shielding_symbol_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Shielding_symbol_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Shielding_symbol_Container();
        }
        
        Cfg_Shielding_symbol_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Shielding_symbol_Container的实例对象
     *
     * @return
     */
    public static Cfg_Shielding_symbol_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
