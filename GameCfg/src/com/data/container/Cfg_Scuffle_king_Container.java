/**
 * Auto generated, do not edit it
 *
 * scuffle_king配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Scuffle_king_Bean; 
import config.Cfg_Scuffle_king_Load;

	
public final class Cfg_Scuffle_king_Container extends ConfigBase<Cfg_Scuffle_king_Bean> {
	
    /**
     * 初始化scuffle_king配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Scuffle_king_Container(){
        Cfg_Scuffle_king_Load load = new Cfg_Scuffle_king_Load();
        Map<Integer, Cfg_Scuffle_king_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Scuffle_king_Bean[] newBeanArray(int size) {
        return new Cfg_Scuffle_king_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Scuffle_king_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Scuffle_king_Container();
        }
        
        Cfg_Scuffle_king_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Scuffle_king_Container的实例对象
     *
     * @return
     */
    public static Cfg_Scuffle_king_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
