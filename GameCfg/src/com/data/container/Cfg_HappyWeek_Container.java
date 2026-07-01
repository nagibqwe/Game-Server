/**
 * Auto generated, do not edit it
 *
 * HappyWeek配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_HappyWeek_Bean; 
import config.Cfg_HappyWeek_Load;

	
public final class Cfg_HappyWeek_Container extends ConfigBase<Cfg_HappyWeek_Bean> {
	
    /**
     * 初始化HappyWeek配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_HappyWeek_Container(){
        Cfg_HappyWeek_Load load = new Cfg_HappyWeek_Load();
        Map<Integer, Cfg_HappyWeek_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_HappyWeek_Bean[] newBeanArray(int size) {
        return new Cfg_HappyWeek_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_HappyWeek_Container manager;
        
        Singleton() {
            this.manager = new Cfg_HappyWeek_Container();
        }
        
        Cfg_HappyWeek_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_HappyWeek_Container的实例对象
     *
     * @return
     */
    public static Cfg_HappyWeek_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
