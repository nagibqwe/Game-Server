/**
 * Auto generated, do not edit it
 *
 * activity_task_type配置表
 */
package com.data.bean;

	
public class Cfg_Activity_task_type_Bean{
    /**
     * key值
     */
    private final int id;
    /**
     * key值
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 后台显示的任务类型
     */
    private final String name;
    /**
     * 后台显示的任务类型
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 条件表ID
     */
    private final int FunctionVariable_id;
    /**
     * 条件表ID
     * @return
     */
    public final int getFunctionVariable_id(){
        return FunctionVariable_id;
    }
    /**
     * 跳转面板ID
     */
    private final int FunctionID;
    /**
     * 跳转面板ID
     * @return
     */
    public final int getFunctionID(){
        return FunctionID;
    }

    public Cfg_Activity_task_type_Bean(int id,String name,int FunctionVariable_id,int FunctionID){
        this.id = id;
        this.name = name;
        this.FunctionVariable_id = FunctionVariable_id;
        this.FunctionID = FunctionID;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("FunctionVariable_id:").append(FunctionVariable_id).append(";");
        str.append("FunctionID:").append(FunctionID).append(";");
        return str.toString();
    }
}
