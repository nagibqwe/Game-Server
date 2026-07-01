/**
 * Auto generated, do not edit it
 *
 * FallingSky_Task配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_FallingSky_Task_Bean; 
import config.Cfg_FallingSky_Task_Load;

	
public final class Cfg_FallingSky_Task_Container extends ConfigBase<Cfg_FallingSky_Task_Bean> {
	
    /**
     * 初始化FallingSky_Task配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_FallingSky_Task_Container(){
        Cfg_FallingSky_Task_Load load = new Cfg_FallingSky_Task_Load();
        Map<Integer, Cfg_FallingSky_Task_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_FallingSky_Task_Bean[] newBeanArray(int size) {
        return new Cfg_FallingSky_Task_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_FallingSky_Task_Container manager;
        
        Singleton() {
            this.manager = new Cfg_FallingSky_Task_Container();
        }
        
        Cfg_FallingSky_Task_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_FallingSky_Task_Container的实例对象
     *
     * @return
     */
    public static Cfg_FallingSky_Task_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
