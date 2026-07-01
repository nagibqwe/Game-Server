/**
 * Auto generated, do not edit it
 *
 * Rank_base配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Rank_base_Bean; 
import config.Cfg_Rank_base_Load;

	
public final class Cfg_Rank_base_Container extends ConfigBase<Cfg_Rank_base_Bean> {
	
    /**
     * 初始化Rank_base配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Rank_base_Container(){
        Cfg_Rank_base_Load load = new Cfg_Rank_base_Load();
        Map<Integer, Cfg_Rank_base_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Rank_base_Bean[] newBeanArray(int size) {
        return new Cfg_Rank_base_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Rank_base_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Rank_base_Container();
        }
        
        Cfg_Rank_base_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Rank_base_Container的实例对象
     *
     * @return
     */
    public static Cfg_Rank_base_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
