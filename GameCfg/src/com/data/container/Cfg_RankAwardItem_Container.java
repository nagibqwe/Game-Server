/**
 * Auto generated, do not edit it
 *
 * RankAwardItem配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_RankAwardItem_Bean; 
import config.Cfg_RankAwardItem_Load;

	
public final class Cfg_RankAwardItem_Container extends ConfigBase<Cfg_RankAwardItem_Bean> {
	
    /**
     * 初始化RankAwardItem配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_RankAwardItem_Container(){
        Cfg_RankAwardItem_Load load = new Cfg_RankAwardItem_Load();
        Map<Integer, Cfg_RankAwardItem_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_RankAwardItem_Bean[] newBeanArray(int size) {
        return new Cfg_RankAwardItem_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_RankAwardItem_Container manager;
        
        Singleton() {
            this.manager = new Cfg_RankAwardItem_Container();
        }
        
        Cfg_RankAwardItem_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_RankAwardItem_Container的实例对象
     *
     * @return
     */
    public static Cfg_RankAwardItem_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
