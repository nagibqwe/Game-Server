/**
 * Auto generated, do not edit it
 *
 * NatureTalisman配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_NatureTalisman_Bean; 
import config.Cfg_NatureTalisman_Load;

	
public final class Cfg_NatureTalisman_Container extends ConfigBase<Cfg_NatureTalisman_Bean> {
	
    /**
     * 初始化NatureTalisman配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_NatureTalisman_Container(){
        Cfg_NatureTalisman_Load load = new Cfg_NatureTalisman_Load();
        Map<Integer, Cfg_NatureTalisman_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_NatureTalisman_Bean[] newBeanArray(int size) {
        return new Cfg_NatureTalisman_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_NatureTalisman_Container manager;
        
        Singleton() {
            this.manager = new Cfg_NatureTalisman_Container();
        }
        
        Cfg_NatureTalisman_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_NatureTalisman_Container的实例对象
     *
     * @return
     */
    public static Cfg_NatureTalisman_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
