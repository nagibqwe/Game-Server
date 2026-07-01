/**
 * Auto generated, do not edit it
 *
 * attributeAdd配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_AttributeAdd_Bean; 
import config.Cfg_AttributeAdd_Load;

	
public final class Cfg_AttributeAdd_Container extends ConfigBase<Cfg_AttributeAdd_Bean> {
	
    /**
     * 初始化attributeAdd配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_AttributeAdd_Container(){
        Cfg_AttributeAdd_Load load = new Cfg_AttributeAdd_Load();
        Map<Integer, Cfg_AttributeAdd_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_AttributeAdd_Bean[] newBeanArray(int size) {
        return new Cfg_AttributeAdd_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_AttributeAdd_Container manager;
        
        Singleton() {
            this.manager = new Cfg_AttributeAdd_Container();
        }
        
        Cfg_AttributeAdd_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_AttributeAdd_Container的实例对象
     *
     * @return
     */
    public static Cfg_AttributeAdd_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
