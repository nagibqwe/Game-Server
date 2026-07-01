/**
 * Auto generated, do not edit it
 *
 * NewRedPacket配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_NewRedPacket_Bean; 
import config.Cfg_NewRedPacket_Load;

	
public final class Cfg_NewRedPacket_Container extends ConfigBase<Cfg_NewRedPacket_Bean> {
	
    /**
     * 初始化NewRedPacket配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_NewRedPacket_Container(){
        Cfg_NewRedPacket_Load load = new Cfg_NewRedPacket_Load();
        Map<Integer, Cfg_NewRedPacket_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_NewRedPacket_Bean[] newBeanArray(int size) {
        return new Cfg_NewRedPacket_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_NewRedPacket_Container manager;
        
        Singleton() {
            this.manager = new Cfg_NewRedPacket_Container();
        }
        
        Cfg_NewRedPacket_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_NewRedPacket_Container的实例对象
     *
     * @return
     */
    public static Cfg_NewRedPacket_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
