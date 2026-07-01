/**
 * Auto generated, do not edit it
 *
 * npc_friend配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Npc_friend_Bean; 
import config.Cfg_Npc_friend_Load;

	
public final class Cfg_Npc_friend_Container extends ConfigBase<Cfg_Npc_friend_Bean> {
	
    /**
     * 初始化npc_friend配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Npc_friend_Container(){
        Cfg_Npc_friend_Load load = new Cfg_Npc_friend_Load();
        Map<Integer, Cfg_Npc_friend_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Npc_friend_Bean[] newBeanArray(int size) {
        return new Cfg_Npc_friend_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Npc_friend_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Npc_friend_Container();
        }
        
        Cfg_Npc_friend_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Npc_friend_Container的实例对象
     *
     * @return
     */
    public static Cfg_Npc_friend_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
