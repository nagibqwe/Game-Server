/**
 * Auto generated, do not edit it
 *
 * Cross_devil_hunt_Hot配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Cross_devil_hunt_Hot_Bean{
    /**
     * 编号
     */
    private final int id;
    /**
     * 编号
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 对应热度区间（前后都是闭区间）
-1代表正无穷，客户端在界面显示为Max
     */
    private final ReadIntegerArray hot;
    /**
     * 对应热度区间（前后都是闭区间）
-1代表正无穷，客户端在界面显示为Max
     * @return
     */
    public final ReadIntegerArray getHot(){
        return hot;
    }

    public Cfg_Cross_devil_hunt_Hot_Bean(int id,String hotStr){
        this.id = id;
        this.hot = new ReadIntegerArray(hotStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("hot:").append(hot).append(";");
        return str.toString();
    }
}
