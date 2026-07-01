/**
 * Auto generated, do not edit it
 *
 * TimingMail配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_TimingMail_Bean; 
import config.Cfg_TimingMail_Load;

	
public final class Cfg_TimingMail_Container extends ConfigBase<Cfg_TimingMail_Bean> {
	
    /**
     * 初始化TimingMail配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_TimingMail_Container(){
        Cfg_TimingMail_Load load = new Cfg_TimingMail_Load();
        Map<Integer, Cfg_TimingMail_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_TimingMail_Bean[] newBeanArray(int size) {
        return new Cfg_TimingMail_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_TimingMail_Container manager;
        
        Singleton() {
            this.manager = new Cfg_TimingMail_Container();
        }
        
        Cfg_TimingMail_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_TimingMail_Container的实例对象
     *
     * @return
     */
    public static Cfg_TimingMail_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
