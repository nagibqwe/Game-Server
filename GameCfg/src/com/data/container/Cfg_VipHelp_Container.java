/**
 * Auto generated, do not edit it
 *
 * VipHelp配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_VipHelp_Bean; 
import config.Cfg_VipHelp_Load;

	
public final class Cfg_VipHelp_Container extends ConfigBase<Cfg_VipHelp_Bean> {
	
    /**
     * 初始化VipHelp配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_VipHelp_Container(){
        Cfg_VipHelp_Load load = new Cfg_VipHelp_Load();
        Map<Integer, Cfg_VipHelp_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_VipHelp_Bean[] newBeanArray(int size) {
        return new Cfg_VipHelp_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_VipHelp_Container manager;
        
        Singleton() {
            this.manager = new Cfg_VipHelp_Container();
        }
        
        Cfg_VipHelp_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_VipHelp_Container的实例对象
     *
     * @return
     */
    public static Cfg_VipHelp_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
