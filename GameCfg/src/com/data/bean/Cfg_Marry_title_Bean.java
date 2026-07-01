/**
 * Auto generated, do not edit it
 *
 * marry_title配置表
 */
package com.data.bean;

	
public class Cfg_Marry_title_Bean{
    /**
     * 夫妻关系等级
     */
    private final int level;
    /**
     * 夫妻关系等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 名称
     */
    private final String titleName;
    /**
     * 名称
     * @return
     */
    public final String getTitleName(){
        return titleName;
    }
    /**
     * 对应Title中ID
     */
    private final int titleId;
    /**
     * 对应Title中ID
     * @return
     */
    public final int getTitleId(){
        return titleId;
    }
    /**
     * 对应marry_lock中ID
对应心锁的等级
     */
    private final int lock;
    /**
     * 对应marry_lock中ID
对应心锁的等级
     * @return
     */
    public final int getLock(){
        return lock;
    }
    /**
     * 激活需要亲密度大小
     */
    private final int needValue;
    /**
     * 激活需要亲密度大小
     * @return
     */
    public final int getNeedValue(){
        return needValue;
    }
    /**
     * 是否公告（0不公告；1公告）
     */
    private final int radio;
    /**
     * 是否公告（0不公告；1公告）
     * @return
     */
    public final int getRadio(){
        return radio;
    }

    public Cfg_Marry_title_Bean(int level,String titleName,int titleId,int lock,int needValue,int radio){
        this.level = level;
        this.titleName = titleName;
        this.titleId = titleId;
        this.lock = lock;
        this.needValue = needValue;
        this.radio = radio;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("level:").append(level).append(";");
        str.append("titleName:").append(titleName).append(";");
        str.append("titleId:").append(titleId).append(";");
        str.append("lock:").append(lock).append(";");
        str.append("needValue:").append(needValue).append(";");
        str.append("radio:").append(radio).append(";");
        return str.toString();
    }
}
