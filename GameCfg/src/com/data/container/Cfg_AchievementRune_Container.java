/**
 * Auto generated, do not edit it
 *
 * AchievementRune配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_AchievementRune_Bean; 
import config.Cfg_AchievementRune_Load;

	
public final class Cfg_AchievementRune_Container extends ConfigBase<Cfg_AchievementRune_Bean> {
	
    /**
     * 初始化AchievementRune配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_AchievementRune_Container(){
        Cfg_AchievementRune_Load load = new Cfg_AchievementRune_Load();
        Map<Integer, Cfg_AchievementRune_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_AchievementRune_Bean[] newBeanArray(int size) {
        return new Cfg_AchievementRune_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_AchievementRune_Container manager;
        
        Singleton() {
            this.manager = new Cfg_AchievementRune_Container();
        }
        
        Cfg_AchievementRune_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_AchievementRune_Container的实例对象
     *
     * @return
     */
    public static Cfg_AchievementRune_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
