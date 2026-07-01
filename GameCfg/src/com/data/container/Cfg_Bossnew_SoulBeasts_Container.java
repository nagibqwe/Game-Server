/**
 * Auto generated, do not edit it
 *
 * bossnew_SoulBeasts配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Bossnew_SoulBeasts_Bean; 
import config.Cfg_Bossnew_SoulBeasts_Load;

	
public final class Cfg_Bossnew_SoulBeasts_Container extends ConfigBase<Cfg_Bossnew_SoulBeasts_Bean> {
	
    /**
     * 初始化bossnew_SoulBeasts配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Bossnew_SoulBeasts_Container(){
        Cfg_Bossnew_SoulBeasts_Load load = new Cfg_Bossnew_SoulBeasts_Load();
        Map<Integer, Cfg_Bossnew_SoulBeasts_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Bossnew_SoulBeasts_Bean[] newBeanArray(int size) {
        return new Cfg_Bossnew_SoulBeasts_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Bossnew_SoulBeasts_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Bossnew_SoulBeasts_Container();
        }
        
        Cfg_Bossnew_SoulBeasts_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Bossnew_SoulBeasts_Container的实例对象
     *
     * @return
     */
    public static Cfg_Bossnew_SoulBeasts_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
