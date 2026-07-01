/**
 * Auto generated, do not edit it
 *
 * peakBattleStage配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_PeakBattleStage_Bean; 
import config.Cfg_PeakBattleStage_Load;

	
public final class Cfg_PeakBattleStage_Container extends ConfigBase<Cfg_PeakBattleStage_Bean> {
	
    /**
     * 初始化peakBattleStage配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_PeakBattleStage_Container(){
        Cfg_PeakBattleStage_Load load = new Cfg_PeakBattleStage_Load();
        Map<Integer, Cfg_PeakBattleStage_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_PeakBattleStage_Bean[] newBeanArray(int size) {
        return new Cfg_PeakBattleStage_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_PeakBattleStage_Container manager;
        
        Singleton() {
            this.manager = new Cfg_PeakBattleStage_Container();
        }
        
        Cfg_PeakBattleStage_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_PeakBattleStage_Container的实例对象
     *
     * @return
     */
    public static Cfg_PeakBattleStage_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
