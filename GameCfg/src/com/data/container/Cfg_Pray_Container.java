/**
 * Auto generated, do not edit it
 *
 * pray配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Pray_Bean; 
import config.Cfg_Pray_Load;

	
public final class Cfg_Pray_Container extends ConfigBase<Cfg_Pray_Bean> {
	
    /**
     * 初始化pray配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Pray_Container(){
        Cfg_Pray_Load load = new Cfg_Pray_Load();
        Map<Integer, Cfg_Pray_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Pray_Bean[] newBeanArray(int size) {
        return new Cfg_Pray_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Pray_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Pray_Container();
        }
        
        Cfg_Pray_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Pray_Container的实例对象
     *
     * @return
     */
    public static Cfg_Pray_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
