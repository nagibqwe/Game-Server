/**
 * Auto generated, do not edit it
 *
 * JJCRank配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_JJCRank_Bean; 
import config.Cfg_JJCRank_Load;

	
public final class Cfg_JJCRank_Container extends ConfigBase<Cfg_JJCRank_Bean> {
	
    /**
     * 初始化JJCRank配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_JJCRank_Container(){
        Cfg_JJCRank_Load load = new Cfg_JJCRank_Load();
        Map<Integer, Cfg_JJCRank_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_JJCRank_Bean[] newBeanArray(int size) {
        return new Cfg_JJCRank_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_JJCRank_Container manager;
        
        Singleton() {
            this.manager = new Cfg_JJCRank_Container();
        }
        
        Cfg_JJCRank_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_JJCRank_Container的实例对象
     *
     * @return
     */
    public static Cfg_JJCRank_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
