/**
 * Auto generated, do not edit it
 *
 * Thai_score配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Thai_score_Bean; 
import config.Cfg_Thai_score_Load;

	
public final class Cfg_Thai_score_Container extends ConfigBase<Cfg_Thai_score_Bean> {
	
    /**
     * 初始化Thai_score配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Thai_score_Container(){
        Cfg_Thai_score_Load load = new Cfg_Thai_score_Load();
        Map<Integer, Cfg_Thai_score_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Thai_score_Bean[] newBeanArray(int size) {
        return new Cfg_Thai_score_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Thai_score_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Thai_score_Container();
        }
        
        Cfg_Thai_score_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Thai_score_Container的实例对象
     *
     * @return
     */
    public static Cfg_Thai_score_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
