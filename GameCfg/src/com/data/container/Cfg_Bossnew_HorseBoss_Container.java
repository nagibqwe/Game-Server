/**
 * Auto generated, do not edit it
 *
 * bossnew_HorseBoss配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Bossnew_HorseBoss_Bean; 
import config.Cfg_Bossnew_HorseBoss_Load;

	
public final class Cfg_Bossnew_HorseBoss_Container extends ConfigBase<Cfg_Bossnew_HorseBoss_Bean> {
	
    /**
     * 初始化bossnew_HorseBoss配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Bossnew_HorseBoss_Container(){
        Cfg_Bossnew_HorseBoss_Load load = new Cfg_Bossnew_HorseBoss_Load();
        Map<Integer, Cfg_Bossnew_HorseBoss_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Bossnew_HorseBoss_Bean[] newBeanArray(int size) {
        return new Cfg_Bossnew_HorseBoss_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Bossnew_HorseBoss_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Bossnew_HorseBoss_Container();
        }
        
        Cfg_Bossnew_HorseBoss_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Bossnew_HorseBoss_Container的实例对象
     *
     * @return
     */
    public static Cfg_Bossnew_HorseBoss_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
