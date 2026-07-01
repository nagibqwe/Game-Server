/**
 * Auto generated, do not edit it
 *
 * social_decorate配置表
 */
package com.data.bean;

	
public class Cfg_Social_decorate_Bean{
    /**
     * ID
     */
    private final int id;
    /**
     * ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 名称
     */
    private final String name;
    /**
     * 名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 类型（1，装饰；2，挂件）
     */
    private final int type;
    /**
     * 类型（1，装饰；2，挂件）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 排序
     */
    private final int order;
    /**
     * 排序
     * @return
     */
    public final int getOrder(){
        return order;
    }
    /**
     * 资源ID
     */
    private final int res;
    /**
     * 资源ID
     * @return
     */
    public final int getRes(){
        return res;
    }

    public Cfg_Social_decorate_Bean(int id,String name,int type,int order,int res){
        this.id = id;
        this.name = name;
        this.type = type;
        this.order = order;
        this.res = res;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("type:").append(type).append(";");
        str.append("order:").append(order).append(";");
        str.append("res:").append(res).append(";");
        return str.toString();
    }
}
