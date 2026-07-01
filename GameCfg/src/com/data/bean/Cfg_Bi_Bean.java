/**
 * Auto generated, do not edit it
 *
 * bi配置表
 */
package com.data.bean;

	
public class Cfg_Bi_Bean{
    /**
     * 唯一id
     */
    private final int Id;
    /**
     * 唯一id
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 枚举类型，用于生成代码
     */
    private final String IdCode;
    /**
     * 枚举类型，用于生成代码
     * @return
     */
    public final String getIdCode(){
        return IdCode;
    }
    /**
     * 组（废弃，暂不删除，避免报错，开关由isOpen字段直接控制）
     */
    private final int Group;
    /**
     * 组（废弃，暂不删除，避免报错，开关由isOpen字段直接控制）
     * @return
     */
    public final int getGroup(){
        return Group;
    }
    /**
     * 统计项开关（0关，1开）
表示改项是否统计，主要为了暂时节约消耗，不必要的统计项可暂时关闭
     */
    private final int isOpen;
    /**
     * 统计项开关（0关，1开）
表示改项是否统计，主要为了暂时节约消耗，不必要的统计项可暂时关闭
     * @return
     */
    public final int getIsOpen(){
        return isOpen;
    }

    public Cfg_Bi_Bean(int Id,String IdCode,int Group,int isOpen){
        this.Id = Id;
        this.IdCode = IdCode;
        this.Group = Group;
        this.isOpen = isOpen;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("IdCode:").append(IdCode).append(";");
        str.append("Group:").append(Group).append(";");
        str.append("isOpen:").append(isOpen).append(";");
        return str.toString();
    }
}
