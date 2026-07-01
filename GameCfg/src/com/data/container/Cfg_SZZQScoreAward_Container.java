/**
 * Auto generated, do not edit it
 *
 * SZZQScoreAward配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_SZZQScoreAward_Bean; 
import config.Cfg_SZZQScoreAward_Load;

	
public final class Cfg_SZZQScoreAward_Container extends ConfigBase<Cfg_SZZQScoreAward_Bean> {
	
    /**
     * 初始化SZZQScoreAward配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_SZZQScoreAward_Container(){
        Cfg_SZZQScoreAward_Load load = new Cfg_SZZQScoreAward_Load();
        Map<Integer, Cfg_SZZQScoreAward_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_SZZQScoreAward_Bean[] newBeanArray(int size) {
        return new Cfg_SZZQScoreAward_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_SZZQScoreAward_Container manager;
        
        Singleton() {
            this.manager = new Cfg_SZZQScoreAward_Container();
        }
        
        Cfg_SZZQScoreAward_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_SZZQScoreAward_Container的实例对象
     *
     * @return
     */
    public static Cfg_SZZQScoreAward_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
