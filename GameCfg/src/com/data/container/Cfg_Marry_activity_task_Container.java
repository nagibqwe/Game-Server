/**
 * Auto generated, do not edit it
 *
 * marry_activity_task配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Marry_activity_task_Bean; 
import config.Cfg_Marry_activity_task_Load;

	
public final class Cfg_Marry_activity_task_Container extends ConfigBase<Cfg_Marry_activity_task_Bean> {
	
    /**
     * 初始化marry_activity_task配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Marry_activity_task_Container(){
        Cfg_Marry_activity_task_Load load = new Cfg_Marry_activity_task_Load();
        Map<Integer, Cfg_Marry_activity_task_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Marry_activity_task_Bean[] newBeanArray(int size) {
        return new Cfg_Marry_activity_task_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Marry_activity_task_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Marry_activity_task_Container();
        }
        
        Cfg_Marry_activity_task_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Marry_activity_task_Container的实例对象
     *
     * @return
     */
    public static Cfg_Marry_activity_task_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
