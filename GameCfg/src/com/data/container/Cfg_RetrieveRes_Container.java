/**
 * Auto generated, do not edit it
 *
 * retrieveRes配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_RetrieveRes_Bean; 
import config.Cfg_RetrieveRes_Load;

	
public final class Cfg_RetrieveRes_Container extends ConfigBase<Cfg_RetrieveRes_Bean> {
	
    /**
     * 初始化retrieveRes配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_RetrieveRes_Container(){
        Cfg_RetrieveRes_Load load = new Cfg_RetrieveRes_Load();
        Map<Integer, Cfg_RetrieveRes_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_RetrieveRes_Bean[] newBeanArray(int size) {
        return new Cfg_RetrieveRes_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_RetrieveRes_Container manager;
        
        Singleton() {
            this.manager = new Cfg_RetrieveRes_Container();
        }
        
        Cfg_RetrieveRes_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_RetrieveRes_Container的实例对象
     *
     * @return
     */
    public static Cfg_RetrieveRes_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
