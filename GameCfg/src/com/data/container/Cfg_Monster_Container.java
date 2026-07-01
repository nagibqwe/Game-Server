/**
 * Auto generated, do not edit it
 *
 * monster配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Monster_Bean; 
import config.Cfg_Monster_Load;

	
public final class Cfg_Monster_Container extends ConfigBase<Cfg_Monster_Bean> {
	
    /**
     * 初始化monster配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Monster_Container(){
        Cfg_Monster_Load load = new Cfg_Monster_Load();
        Map<Integer, Cfg_Monster_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Monster_Bean[] newBeanArray(int size) {
        return new Cfg_Monster_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Monster_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Monster_Container();
        }
        
        Cfg_Monster_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Monster_Container的实例对象
     *
     * @return
     */
    public static Cfg_Monster_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
