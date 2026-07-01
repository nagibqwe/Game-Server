/**
 * Auto generated, do not edit it
 *
 * UrlMarquee配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_UrlMarquee_Bean; 
import config.Cfg_UrlMarquee_Load;

	
public final class Cfg_UrlMarquee_Container extends ConfigBase<Cfg_UrlMarquee_Bean> {
	
    /**
     * 初始化UrlMarquee配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_UrlMarquee_Container(){
        Cfg_UrlMarquee_Load load = new Cfg_UrlMarquee_Load();
        Map<Integer, Cfg_UrlMarquee_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_UrlMarquee_Bean[] newBeanArray(int size) {
        return new Cfg_UrlMarquee_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_UrlMarquee_Container manager;
        
        Singleton() {
            this.manager = new Cfg_UrlMarquee_Container();
        }
        
        Cfg_UrlMarquee_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_UrlMarquee_Container的实例对象
     *
     * @return
     */
    public static Cfg_UrlMarquee_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
