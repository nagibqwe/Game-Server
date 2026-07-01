/**
 * Auto generated, do not edit it
 *
 * clonerobot配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Clonerobot_Bean; 
import config.Cfg_Clonerobot_Load;

	
public final class Cfg_Clonerobot_Container extends ConfigBase<Cfg_Clonerobot_Bean> {
	
    /**
     * 初始化clonerobot配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Clonerobot_Container(){
        Cfg_Clonerobot_Load load = new Cfg_Clonerobot_Load();
        Map<Integer, Cfg_Clonerobot_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Clonerobot_Bean[] newBeanArray(int size) {
        return new Cfg_Clonerobot_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Clonerobot_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Clonerobot_Container();
        }
        
        Cfg_Clonerobot_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Clonerobot_Container的实例对象
     *
     * @return
     */
    public static Cfg_Clonerobot_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
