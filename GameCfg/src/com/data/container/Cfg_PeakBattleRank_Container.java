/**
 * Auto generated, do not edit it
 *
 * peakBattleRank配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_PeakBattleRank_Bean; 
import config.Cfg_PeakBattleRank_Load;

	
public final class Cfg_PeakBattleRank_Container extends ConfigBase<Cfg_PeakBattleRank_Bean> {
	
    /**
     * 初始化peakBattleRank配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_PeakBattleRank_Container(){
        Cfg_PeakBattleRank_Load load = new Cfg_PeakBattleRank_Load();
        Map<Integer, Cfg_PeakBattleRank_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_PeakBattleRank_Bean[] newBeanArray(int size) {
        return new Cfg_PeakBattleRank_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_PeakBattleRank_Container manager;
        
        Singleton() {
            this.manager = new Cfg_PeakBattleRank_Container();
        }
        
        Cfg_PeakBattleRank_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_PeakBattleRank_Container的实例对象
     *
     * @return
     */
    public static Cfg_PeakBattleRank_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
