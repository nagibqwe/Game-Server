/**
 * Auto generated, do not edit it
 *
 * on_hook配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadLongArray; 
	
public class Cfg_On_hook_Bean{
    /**
     * 模板等级
     */
    private final int level;
    /**
     * 模板等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 模板经验（秒）
     */
    private final long exp;
    /**
     * 模板经验（秒）
     * @return
     */
    public final long getExp(){
        return exp;
    }
    /**
     * 模板战力
     */
    private final int battle;
    /**
     * 模板战力
     * @return
     */
    public final int getBattle(){
        return battle;
    }
    /**
     * 普通掉落,掉落ID(@;@)client ignore
     */
    private final ReadIntegerArray normaldrop;
    /**
     * 普通掉落,掉落ID(@;@)client ignore
     * @return
     */
    public final ReadIntegerArray getNormaldrop(){
        return normaldrop;
    }
    /**
     * 任务id(@;@)client ignore
     */
    private final ReadIntegerArray task_id;
    /**
     * 任务id(@;@)client ignore
     * @return
     */
    public final ReadIntegerArray getTask_id(){
        return task_id;
    }
    /**
     * 任务掉落id(@;@)client ignore
     */
    private final ReadIntegerArray taskdrop;
    /**
     * 任务掉落id(@;@)client ignore
     * @return
     */
    public final ReadIntegerArray getTaskdrop(){
        return taskdrop;
    }
    /**
     * 地图经验
     */
    private final ReadLongArray map_exp;
    /**
     * 地图经验
     * @return
     */
    public final ReadLongArray getMap_exp(){
        return map_exp;
    }

    public Cfg_On_hook_Bean(int level,long exp,int battle,String normaldropStr,String task_idStr,String taskdropStr,String map_expStr){
        this.level = level;
        this.exp = exp;
        this.battle = battle;
        this.normaldrop = new ReadIntegerArray(normaldropStr,",");
        this.task_id = new ReadIntegerArray(task_idStr,",");
        this.taskdrop = new ReadIntegerArray(taskdropStr,",");
        this.map_exp = new ReadLongArray(map_expStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("level:").append(level).append(";");
        str.append("exp:").append(exp).append(";");
        str.append("battle:").append(battle).append(";");
        str.append("normaldrop:").append(normaldrop).append(";");
        str.append("task_id:").append(task_id).append(";");
        str.append("taskdrop:").append(taskdrop).append(";");
        str.append("map_exp:").append(map_exp).append(";");
        return str.toString();
    }
}
