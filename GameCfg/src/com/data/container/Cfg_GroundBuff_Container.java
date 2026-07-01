/**
 * Auto generated, do not edit it
 *
 * GroundBuff配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_GroundBuff_Bean; 
import config.Cfg_GroundBuff_Load;

	
public final class Cfg_GroundBuff_Container extends ConfigBase<Cfg_GroundBuff_Bean> {
	
    /**
     * 初始化GroundBuff配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_GroundBuff_Container(){
        Cfg_GroundBuff_Load load = new Cfg_GroundBuff_Load();
        Map<Integer, Cfg_GroundBuff_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_GroundBuff_Bean[] newBeanArray(int size) {
        return new Cfg_GroundBuff_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_GroundBuff_Container manager;
        
        Singleton() {
            this.manager = new Cfg_GroundBuff_Container();
        }
        
        Cfg_GroundBuff_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_GroundBuff_Container的实例对象
     *
     * @return
     */
    public static Cfg_GroundBuff_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
