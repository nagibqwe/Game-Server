/**
 * Auto generated, do not edit it
 *
 * peakBattleJoinReward配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_PeakBattleJoinReward_Bean; 
import config.Cfg_PeakBattleJoinReward_Load;

	
public final class Cfg_PeakBattleJoinReward_Container extends ConfigBase<Cfg_PeakBattleJoinReward_Bean> {
	
    /**
     * 初始化peakBattleJoinReward配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_PeakBattleJoinReward_Container(){
        Cfg_PeakBattleJoinReward_Load load = new Cfg_PeakBattleJoinReward_Load();
        Map<Integer, Cfg_PeakBattleJoinReward_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_PeakBattleJoinReward_Bean[] newBeanArray(int size) {
        return new Cfg_PeakBattleJoinReward_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_PeakBattleJoinReward_Container manager;
        
        Singleton() {
            this.manager = new Cfg_PeakBattleJoinReward_Container();
        }
        
        Cfg_PeakBattleJoinReward_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_PeakBattleJoinReward_Container的实例对象
     *
     * @return
     */
    public static Cfg_PeakBattleJoinReward_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
