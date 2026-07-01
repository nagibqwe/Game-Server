/**
 * Auto generated, do not edit it
 *
 * clone_story配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Clone_story_Bean{
    /**
     * 副本ID
     */
    private final int cloneID;
    /**
     * 副本ID
     * @return
     */
    public final int getCloneID(){
        return cloneID;
    }
    /**
     * 需要完成的主线任务id(@_@)
     */
    private final ReadIntegerArray needTaskId;
    /**
     * 需要完成的主线任务id(@_@)
     * @return
     */
    public final ReadIntegerArray getNeedTaskId(){
        return needTaskId;
    }
    /**
     * 完成副本类型(0杀死所有怪物，1杀死指定boss，2守卫指定boss)
     */
    private final int finishType;
    /**
     * 完成副本类型(0杀死所有怪物，1杀死指定boss，2守卫指定boss)
     * @return
     */
    public final int getFinishType(){
        return finishType;
    }
    /**
     * 需要杀死或者保护的指定怪物id，没有填0
     */
    private final int needMonsterId;
    /**
     * 需要杀死或者保护的指定怪物id，没有填0
     * @return
     */
    public final int getNeedMonsterId(){
        return needMonsterId;
    }
    /**
     * 最大波数
     */
    private final int loop;
    /**
     * 最大波数
     * @return
     */
    public final int getLoop(){
        return loop;
    }
    /**
     * BOSS掉落ID
     */
    private final int dropID;
    /**
     * BOSS掉落ID
     * @return
     */
    public final int getDropID(){
        return dropID;
    }
    /**
     * 阻挡（怪物波数_阻挡名字;怪物波数_阻挡名字）
     */
    private final ReadIntegerArrayEs stop;
    /**
     * 阻挡（怪物波数_阻挡名字;怪物波数_阻挡名字）
     * @return
     */
    public final ReadIntegerArrayEs getStop(){
        return stop;
    }

    public Cfg_Clone_story_Bean(int cloneID,String needTaskIdStr,int finishType,int needMonsterId,int loop,int dropID,String stopStr){
        this.cloneID = cloneID;
        this.needTaskId = new ReadIntegerArray(needTaskIdStr,",");
        this.finishType = finishType;
        this.needMonsterId = needMonsterId;
        this.loop = loop;
        this.dropID = dropID;
        this.stop = new ReadIntegerArrayEs(stopStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("cloneID:").append(cloneID).append(";");
        str.append("needTaskId:").append(needTaskId).append(";");
        str.append("finishType:").append(finishType).append(";");
        str.append("needMonsterId:").append(needMonsterId).append(";");
        str.append("loop:").append(loop).append(";");
        str.append("dropID:").append(dropID).append(";");
        str.append("stop:").append(stop).append(";");
        return str.toString();
    }
}
