/**
 * Auto generated, do not edit it
 *
 * FirstRecharge配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_FirstRecharge_Bean; 
import config.Cfg_FirstRecharge_Load;

	
public final class Cfg_FirstRecharge_Container extends ConfigBase<Cfg_FirstRecharge_Bean> {
	
    /**
     * 初始化FirstRecharge配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_FirstRecharge_Container(){
        Cfg_FirstRecharge_Load load = new Cfg_FirstRecharge_Load();
        Map<Integer, Cfg_FirstRecharge_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_FirstRecharge_Bean[] newBeanArray(int size) {
        return new Cfg_FirstRecharge_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_FirstRecharge_Container manager;
        
        Singleton() {
            this.manager = new Cfg_FirstRecharge_Container();
        }
        
        Cfg_FirstRecharge_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_FirstRecharge_Container的实例对象
     *
     * @return
     */
    public static Cfg_FirstRecharge_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
