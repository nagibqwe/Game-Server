/**
 * Auto generated, do not edit it
 *
 * bi配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Bi_Bean; 
import config.Cfg_Bi_Load;

	
public final class Cfg_Bi_Container extends ConfigBase<Cfg_Bi_Bean> {
	
    /**
     * 初始化bi配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Bi_Container(){
        Cfg_Bi_Load load = new Cfg_Bi_Load();
        Map<Integer, Cfg_Bi_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Bi_Bean[] newBeanArray(int size) {
        return new Cfg_Bi_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Bi_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Bi_Container();
        }
        
        Cfg_Bi_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Bi_Container的实例对象
     *
     * @return
     */
    public static Cfg_Bi_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
