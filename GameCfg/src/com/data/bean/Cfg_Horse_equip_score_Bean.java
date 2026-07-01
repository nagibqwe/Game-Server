/**
 * Auto generated, do not edit it
 *
 * Horse_equip_score配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Horse_equip_score_Bean{
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
     * 脉轮id
     */
    private final int site;
    /**
     * 脉轮id
     * @return
     */
    public final int getSite(){
        return site;
    }
    /**
     * 需要的脉轮评分
     */
    private final int need_score;
    /**
     * 需要的脉轮评分
     * @return
     */
    public final int getNeed_score(){
        return need_score;
    }
    /**
     * 评价内容
     */
    private final String name;
    /**
     * 评价内容
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 外显特效配置
     */
    private final int VFX;
    /**
     * 外显特效配置
     * @return
     */
    public final int getVFX(){
        return VFX;
    }
    /**
     * 激活时的公告频道(4跑马灯)
     */
    private final int notice;
    /**
     * 激活时的公告频道(4跑马灯)
     * @return
     */
    public final int getNotice(){
        return notice;
    }
    /**
     * 掉落物品后聊天发送(0世界4系统14传闻）
     */
    private final ReadIntegerArray chatchannel;
    /**
     * 掉落物品后聊天发送(0世界4系统14传闻）
     * @return
     */
    public final ReadIntegerArray getChatchannel(){
        return chatchannel;
    }

    public Cfg_Horse_equip_score_Bean(int Id,int site,int need_score,String name,int VFX,int notice,String chatchannelStr){
        this.Id = Id;
        this.site = site;
        this.need_score = need_score;
        this.name = name;
        this.VFX = VFX;
        this.notice = notice;
        this.chatchannel = new ReadIntegerArray(chatchannelStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("site:").append(site).append(";");
        str.append("need_score:").append(need_score).append(";");
        str.append("name:").append(name).append(";");
        str.append("VFX:").append(VFX).append(";");
        str.append("notice:").append(notice).append(";");
        str.append("chatchannel:").append(chatchannel).append(";");
        return str.toString();
    }
}
