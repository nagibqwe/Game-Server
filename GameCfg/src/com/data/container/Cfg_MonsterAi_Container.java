/**
 * Auto generated, do not edit it
 *
 * monsterAi配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_MonsterAi_Bean; 
import config.Cfg_MonsterAi_Load;

	
public final class Cfg_MonsterAi_Container extends ConfigBase<Cfg_MonsterAi_Bean> {
	
    /**
     * 初始化monsterAi配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_MonsterAi_Container(){
        Cfg_MonsterAi_Load load = new Cfg_MonsterAi_Load();
        Map<Integer, Cfg_MonsterAi_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_MonsterAi_Bean[] newBeanArray(int size) {
        return new Cfg_MonsterAi_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_MonsterAi_Container manager;
        
        Singleton() {
            this.manager = new Cfg_MonsterAi_Container();
        }
        
        Cfg_MonsterAi_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_MonsterAi_Container的实例对象
     *
     * @return
     */
    public static Cfg_MonsterAi_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
