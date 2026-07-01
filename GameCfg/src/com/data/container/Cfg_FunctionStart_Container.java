/**
 * Auto generated, do not edit it
 *
 * FunctionStart配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_FunctionStart_Bean; 
import config.Cfg_FunctionStart_Load;

	
public final class Cfg_FunctionStart_Container extends ConfigBase<Cfg_FunctionStart_Bean> {
	
    /**
     * 初始化FunctionStart配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_FunctionStart_Container(){
        Cfg_FunctionStart_Load load = new Cfg_FunctionStart_Load();
        Map<Integer, Cfg_FunctionStart_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_FunctionStart_Bean[] newBeanArray(int size) {
        return new Cfg_FunctionStart_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_FunctionStart_Container manager;
        
        Singleton() {
            this.manager = new Cfg_FunctionStart_Container();
        }
        
        Cfg_FunctionStart_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_FunctionStart_Container的实例对象
     *
     * @return
     */
    public static Cfg_FunctionStart_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
