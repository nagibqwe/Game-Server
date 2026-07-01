package com.game.attribute.manager;

/**
 * @Desc TODO
 * @Date 2020/8/12 20:25
 * @Auth ZUncle
 */
public class AttributeManager {

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        INSTANCE;                   //一个枚举的元素，它就代表了Singleton的一个实例
        AttributeManager manager;

        Singleton() {
            this.manager = new AttributeManager();
        }

        AttributeManager getProcessor() {
            return manager;
        }
    }

    public static AttributeManager getInstance() {
        return AttributeManager.Singleton.INSTANCE.getProcessor();
    }


}
