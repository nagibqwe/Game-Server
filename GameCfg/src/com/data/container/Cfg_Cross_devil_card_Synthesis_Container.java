/**
 * Auto generated, do not edit it
 *
 * Cross_devil_card_Synthesis配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Cross_devil_card_Synthesis_Bean; 
import config.Cfg_Cross_devil_card_Synthesis_Load;

	
public final class Cfg_Cross_devil_card_Synthesis_Container extends ConfigBase<Cfg_Cross_devil_card_Synthesis_Bean> {
	
    /**
     * 初始化Cross_devil_card_Synthesis配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Cross_devil_card_Synthesis_Container(){
        Cfg_Cross_devil_card_Synthesis_Load load = new Cfg_Cross_devil_card_Synthesis_Load();
        Map<Integer, Cfg_Cross_devil_card_Synthesis_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Cross_devil_card_Synthesis_Bean[] newBeanArray(int size) {
        return new Cfg_Cross_devil_card_Synthesis_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Cross_devil_card_Synthesis_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Cross_devil_card_Synthesis_Container();
        }
        
        Cfg_Cross_devil_card_Synthesis_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Cross_devil_card_Synthesis_Container的实例对象
     *
     * @return
     */
    public static Cfg_Cross_devil_card_Synthesis_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
