/**
 * Auto generated, do not edit it
 *
 * changejob配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Changejob_Bean; 
import config.Cfg_Changejob_Load;

	
public final class Cfg_Changejob_Container extends ConfigBase<Cfg_Changejob_Bean> {
	
    /**
     * 初始化changejob配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Changejob_Container(){
        Cfg_Changejob_Load load = new Cfg_Changejob_Load();
        Map<Integer, Cfg_Changejob_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Changejob_Bean[] newBeanArray(int size) {
        return new Cfg_Changejob_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Changejob_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Changejob_Container();
        }
        
        Cfg_Changejob_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Changejob_Container的实例对象
     *
     * @return
     */
    public static Cfg_Changejob_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
