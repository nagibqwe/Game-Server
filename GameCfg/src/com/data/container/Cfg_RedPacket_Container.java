/**
 * Auto generated, do not edit it
 *
 * redPacket配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_RedPacket_Bean; 
import config.Cfg_RedPacket_Load;

	
public final class Cfg_RedPacket_Container extends ConfigBase<Cfg_RedPacket_Bean> {
	
    /**
     * 初始化redPacket配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_RedPacket_Container(){
        Cfg_RedPacket_Load load = new Cfg_RedPacket_Load();
        Map<Integer, Cfg_RedPacket_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_RedPacket_Bean[] newBeanArray(int size) {
        return new Cfg_RedPacket_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_RedPacket_Container manager;
        
        Singleton() {
            this.manager = new Cfg_RedPacket_Container();
        }
        
        Cfg_RedPacket_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_RedPacket_Container的实例对象
     *
     * @return
     */
    public static Cfg_RedPacket_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
