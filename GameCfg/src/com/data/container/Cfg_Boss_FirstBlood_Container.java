/**
 * Auto generated, do not edit it
 *
 * boss_FirstBlood配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Boss_FirstBlood_Bean; 
import config.Cfg_Boss_FirstBlood_Load;

	
public final class Cfg_Boss_FirstBlood_Container extends ConfigBase<Cfg_Boss_FirstBlood_Bean> {
	
    /**
     * 初始化boss_FirstBlood配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Boss_FirstBlood_Container(){
        Cfg_Boss_FirstBlood_Load load = new Cfg_Boss_FirstBlood_Load();
        Map<Integer, Cfg_Boss_FirstBlood_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Boss_FirstBlood_Bean[] newBeanArray(int size) {
        return new Cfg_Boss_FirstBlood_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Boss_FirstBlood_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Boss_FirstBlood_Container();
        }
        
        Cfg_Boss_FirstBlood_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Boss_FirstBlood_Container的实例对象
     *
     * @return
     */
    public static Cfg_Boss_FirstBlood_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
