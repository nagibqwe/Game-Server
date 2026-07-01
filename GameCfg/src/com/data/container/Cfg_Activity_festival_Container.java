/**
 * Auto generated, do not edit it
 *
 * activity_festival配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Activity_festival_Bean; 
import config.Cfg_Activity_festival_Load;

	
public final class Cfg_Activity_festival_Container extends ConfigBase<Cfg_Activity_festival_Bean> {
	
    /**
     * 初始化activity_festival配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Activity_festival_Container(){
        Cfg_Activity_festival_Load load = new Cfg_Activity_festival_Load();
        Map<Integer, Cfg_Activity_festival_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Activity_festival_Bean[] newBeanArray(int size) {
        return new Cfg_Activity_festival_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Activity_festival_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Activity_festival_Container();
        }
        
        Cfg_Activity_festival_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Activity_festival_Container的实例对象
     *
     * @return
     */
    public static Cfg_Activity_festival_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
