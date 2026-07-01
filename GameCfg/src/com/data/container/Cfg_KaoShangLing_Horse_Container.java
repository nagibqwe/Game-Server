/**
 * Auto generated, do not edit it
 *
 * KaoShangLing_Horse配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_KaoShangLing_Horse_Bean; 
import config.Cfg_KaoShangLing_Horse_Load;

	
public final class Cfg_KaoShangLing_Horse_Container extends ConfigBase<Cfg_KaoShangLing_Horse_Bean> {
	
    /**
     * 初始化KaoShangLing_Horse配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_KaoShangLing_Horse_Container(){
        Cfg_KaoShangLing_Horse_Load load = new Cfg_KaoShangLing_Horse_Load();
        Map<Integer, Cfg_KaoShangLing_Horse_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_KaoShangLing_Horse_Bean[] newBeanArray(int size) {
        return new Cfg_KaoShangLing_Horse_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_KaoShangLing_Horse_Container manager;
        
        Singleton() {
            this.manager = new Cfg_KaoShangLing_Horse_Container();
        }
        
        Cfg_KaoShangLing_Horse_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_KaoShangLing_Horse_Container的实例对象
     *
     * @return
     */
    public static Cfg_KaoShangLing_Horse_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
