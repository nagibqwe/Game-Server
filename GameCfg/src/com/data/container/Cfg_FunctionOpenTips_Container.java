/**
 * Auto generated, do not edit it
 *
 * FunctionOpenTips配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_FunctionOpenTips_Bean; 
import config.Cfg_FunctionOpenTips_Load;

	
public final class Cfg_FunctionOpenTips_Container extends ConfigBase<Cfg_FunctionOpenTips_Bean> {
	
    /**
     * 初始化FunctionOpenTips配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_FunctionOpenTips_Container(){
        Cfg_FunctionOpenTips_Load load = new Cfg_FunctionOpenTips_Load();
        Map<Integer, Cfg_FunctionOpenTips_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_FunctionOpenTips_Bean[] newBeanArray(int size) {
        return new Cfg_FunctionOpenTips_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_FunctionOpenTips_Container manager;
        
        Singleton() {
            this.manager = new Cfg_FunctionOpenTips_Container();
        }
        
        Cfg_FunctionOpenTips_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_FunctionOpenTips_Container的实例对象
     *
     * @return
     */
    public static Cfg_FunctionOpenTips_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
