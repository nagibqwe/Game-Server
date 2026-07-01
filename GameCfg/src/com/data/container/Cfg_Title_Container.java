/**
 * Auto generated, do not edit it
 *
 * title配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Title_Bean; 
import config.Cfg_Title_Load;

	
public final class Cfg_Title_Container extends ConfigBase<Cfg_Title_Bean> {
	
    /**
     * 初始化title配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Title_Container(){
        Cfg_Title_Load load = new Cfg_Title_Load();
        Map<Integer, Cfg_Title_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Title_Bean[] newBeanArray(int size) {
        return new Cfg_Title_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Title_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Title_Container();
        }
        
        Cfg_Title_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Title_Container的实例对象
     *
     * @return
     */
    public static Cfg_Title_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
