/**
 * Auto generated, do not edit it
 *
 * limit_gold_shop配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Limit_gold_shop_Bean; 
import config.Cfg_Limit_gold_shop_Load;

	
public final class Cfg_Limit_gold_shop_Container extends ConfigBase<Cfg_Limit_gold_shop_Bean> {
	
    /**
     * 初始化limit_gold_shop配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Limit_gold_shop_Container(){
        Cfg_Limit_gold_shop_Load load = new Cfg_Limit_gold_shop_Load();
        Map<Integer, Cfg_Limit_gold_shop_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Limit_gold_shop_Bean[] newBeanArray(int size) {
        return new Cfg_Limit_gold_shop_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Limit_gold_shop_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Limit_gold_shop_Container();
        }
        
        Cfg_Limit_gold_shop_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Limit_gold_shop_Container的实例对象
     *
     * @return
     */
    public static Cfg_Limit_gold_shop_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
