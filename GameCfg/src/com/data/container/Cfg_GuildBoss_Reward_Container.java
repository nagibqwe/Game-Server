/**
 * Auto generated, do not edit it
 *
 * guildBoss_Reward配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_GuildBoss_Reward_Bean; 
import config.Cfg_GuildBoss_Reward_Load;

	
public final class Cfg_GuildBoss_Reward_Container extends ConfigBase<Cfg_GuildBoss_Reward_Bean> {
	
    /**
     * 初始化guildBoss_Reward配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_GuildBoss_Reward_Container(){
        Cfg_GuildBoss_Reward_Load load = new Cfg_GuildBoss_Reward_Load();
        Map<Integer, Cfg_GuildBoss_Reward_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_GuildBoss_Reward_Bean[] newBeanArray(int size) {
        return new Cfg_GuildBoss_Reward_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_GuildBoss_Reward_Container manager;
        
        Singleton() {
            this.manager = new Cfg_GuildBoss_Reward_Container();
        }
        
        Cfg_GuildBoss_Reward_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_GuildBoss_Reward_Container的实例对象
     *
     * @return
     */
    public static Cfg_GuildBoss_Reward_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
