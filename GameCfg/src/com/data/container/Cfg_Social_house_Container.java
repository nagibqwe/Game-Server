/**
 * Auto generated, do not edit it
 *
 * social_house配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Social_house_Bean; 
import config.Cfg_Social_house_Load;

	
public final class Cfg_Social_house_Container extends ConfigBase<Cfg_Social_house_Bean> {
	
    /**
     * 初始化social_house配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Social_house_Container(){
        Cfg_Social_house_Load load = new Cfg_Social_house_Load();
        Map<Integer, Cfg_Social_house_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Social_house_Bean[] newBeanArray(int size) {
        return new Cfg_Social_house_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Social_house_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Social_house_Container();
        }
        
        Cfg_Social_house_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Social_house_Container的实例对象
     *
     * @return
     */
    public static Cfg_Social_house_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
