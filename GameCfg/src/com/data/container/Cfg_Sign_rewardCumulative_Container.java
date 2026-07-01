/**
 * Auto generated, do not edit it
 *
 * sign_rewardCumulative配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Sign_rewardCumulative_Bean; 
import config.Cfg_Sign_rewardCumulative_Load;

	
public final class Cfg_Sign_rewardCumulative_Container extends ConfigBase<Cfg_Sign_rewardCumulative_Bean> {
	
    /**
     * 初始化sign_rewardCumulative配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Sign_rewardCumulative_Container(){
        Cfg_Sign_rewardCumulative_Load load = new Cfg_Sign_rewardCumulative_Load();
        Map<Integer, Cfg_Sign_rewardCumulative_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Sign_rewardCumulative_Bean[] newBeanArray(int size) {
        return new Cfg_Sign_rewardCumulative_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Sign_rewardCumulative_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Sign_rewardCumulative_Container();
        }
        
        Cfg_Sign_rewardCumulative_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Sign_rewardCumulative_Container的实例对象
     *
     * @return
     */
    public static Cfg_Sign_rewardCumulative_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
