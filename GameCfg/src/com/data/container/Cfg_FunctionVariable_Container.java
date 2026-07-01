/**
 * Auto generated, do not edit it
 *
 * FunctionVariable配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_FunctionVariable_Bean; 
import config.Cfg_FunctionVariable_Load;

	
public final class Cfg_FunctionVariable_Container extends ConfigBase<Cfg_FunctionVariable_Bean> {
	
    /**
     * 初始化FunctionVariable配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_FunctionVariable_Container(){
        Cfg_FunctionVariable_Load load = new Cfg_FunctionVariable_Load();
        Map<Integer, Cfg_FunctionVariable_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_FunctionVariable_Bean[] newBeanArray(int size) {
        return new Cfg_FunctionVariable_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_FunctionVariable_Container manager;
        
        Singleton() {
            this.manager = new Cfg_FunctionVariable_Container();
        }
        
        Cfg_FunctionVariable_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_FunctionVariable_Container的实例对象
     *
     * @return
     */
    public static Cfg_FunctionVariable_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
