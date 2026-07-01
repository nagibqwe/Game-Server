/**
 * Auto generated, do not edit it
 *
 * investPeak_Global配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_InvestPeak_Global_Bean; 
import config.Cfg_InvestPeak_Global_Load;

	
public final class Cfg_InvestPeak_Global_Container extends ConfigBase<Cfg_InvestPeak_Global_Bean> {
	
    /**
     * 初始化investPeak_Global配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_InvestPeak_Global_Container(){
        Cfg_InvestPeak_Global_Load load = new Cfg_InvestPeak_Global_Load();
        Map<Integer, Cfg_InvestPeak_Global_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_InvestPeak_Global_Bean[] newBeanArray(int size) {
        return new Cfg_InvestPeak_Global_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_InvestPeak_Global_Container manager;
        
        Singleton() {
            this.manager = new Cfg_InvestPeak_Global_Container();
        }
        
        Cfg_InvestPeak_Global_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_InvestPeak_Global_Container的实例对象
     *
     * @return
     */
    public static Cfg_InvestPeak_Global_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
