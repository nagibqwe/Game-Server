/**
 * Auto generated, do not edit it
 *
 * GemGrade配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_GemGrade_Bean; 
import config.Cfg_GemGrade_Load;

	
public final class Cfg_GemGrade_Container extends ConfigBase<Cfg_GemGrade_Bean> {
	
    /**
     * 初始化GemGrade配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_GemGrade_Container(){
        Cfg_GemGrade_Load load = new Cfg_GemGrade_Load();
        Map<Integer, Cfg_GemGrade_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_GemGrade_Bean[] newBeanArray(int size) {
        return new Cfg_GemGrade_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_GemGrade_Container manager;
        
        Singleton() {
            this.manager = new Cfg_GemGrade_Container();
        }
        
        Cfg_GemGrade_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_GemGrade_Container的实例对象
     *
     * @return
     */
    public static Cfg_GemGrade_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
