/**
 * Auto generated, do not edit it
 *
 * RollDodge配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_RollDodge_Bean; 
import config.Cfg_RollDodge_Load;

	
public final class Cfg_RollDodge_Container extends ConfigBase<Cfg_RollDodge_Bean> {
	
    /**
     * 初始化RollDodge配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_RollDodge_Container(){
        Cfg_RollDodge_Load load = new Cfg_RollDodge_Load();
        Map<Integer, Cfg_RollDodge_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_RollDodge_Bean[] newBeanArray(int size) {
        return new Cfg_RollDodge_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_RollDodge_Container manager;
        
        Singleton() {
            this.manager = new Cfg_RollDodge_Container();
        }
        
        Cfg_RollDodge_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_RollDodge_Container的实例对象
     *
     * @return
     */
    public static Cfg_RollDodge_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
