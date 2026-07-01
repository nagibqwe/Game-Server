/**
 * Auto generated, do not edit it
 *
 * buff配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Buff_Bean; 
import config.Cfg_Buff_Load;

	
public final class Cfg_Buff_Container extends ConfigBase<Cfg_Buff_Bean> {
	
    /**
     * 初始化buff配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Buff_Container(){
        Cfg_Buff_Load load = new Cfg_Buff_Load();
        Map<Integer, Cfg_Buff_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Buff_Bean[] newBeanArray(int size) {
        return new Cfg_Buff_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Buff_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Buff_Container();
        }
        
        Cfg_Buff_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Buff_Container的实例对象
     *
     * @return
     */
    public static Cfg_Buff_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
