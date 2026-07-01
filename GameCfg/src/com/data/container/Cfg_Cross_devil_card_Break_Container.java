/**
 * Auto generated, do not edit it
 *
 * Cross_devil_card_Break配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Cross_devil_card_Break_Bean; 
import config.Cfg_Cross_devil_card_Break_Load;

	
public final class Cfg_Cross_devil_card_Break_Container extends ConfigBase<Cfg_Cross_devil_card_Break_Bean> {
	
    /**
     * 初始化Cross_devil_card_Break配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Cross_devil_card_Break_Container(){
        Cfg_Cross_devil_card_Break_Load load = new Cfg_Cross_devil_card_Break_Load();
        Map<Integer, Cfg_Cross_devil_card_Break_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Cross_devil_card_Break_Bean[] newBeanArray(int size) {
        return new Cfg_Cross_devil_card_Break_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Cross_devil_card_Break_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Cross_devil_card_Break_Container();
        }
        
        Cfg_Cross_devil_card_Break_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Cross_devil_card_Break_Container的实例对象
     *
     * @return
     */
    public static Cfg_Cross_devil_card_Break_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
