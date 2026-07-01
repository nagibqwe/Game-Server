/**
 * Auto generated, do not edit it
 *
 * new_sever_rank配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_New_sever_rank_Bean{
    /**
     * id
（ID=1时为等级比拼持续2天，
ID=3时代表当日充值比拼，当前暂时没有使用）
     */
    private final int id;
    /**
     * id
（ID=1时为等级比拼持续2天，
ID=3时代表当日充值比拼，当前暂时没有使用）
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 0表示读取排行榜
1表示今日充值
     */
    private final int type;
    /**
     * 0表示读取排行榜
1表示今日充值
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * type为0时读取的排行榜的ID
     */
    private final int parm;
    /**
     * type为0时读取的排行榜的ID
     * @return
     */
    public final int getParm(){
        return parm;
    }
    /**
     * 服务器结束时间，1表示开服当天23:59分，具体时间点通过global配置
始终只开启1个，功能开放时开启
     */
    private final int serverEndTime;
    /**
     * 服务器结束时间，1表示开服当天23:59分，具体时间点通过global配置
始终只开启1个，功能开放时开启
     * @return
     */
    public final int getServerEndTime(){
        return serverEndTime;
    }
    /**
     * 真实具体给与奖励的排名
     */
    private final int rew_rank;
    /**
     * 真实具体给与奖励的排名
     * @return
     */
    public final int getRew_rank(){
        return rew_rank;
    }
    /**
     * 做假排名的方式[A,B]
假排名=取整(（真实排名-配置A）配置B+配置A)
     */
    private final ReadIntegerArray show_fake_ranke;
    /**
     * 做假排名的方式[A,B]
假排名=取整(（真实排名-配置A）配置B+配置A)
     * @return
     */
    public final ReadIntegerArray getShow_fake_ranke(){
        return show_fake_ranke;
    }
    /**
     * 显示名字
     */
    private final String showname;
    /**
     * 显示名字
     * @return
     */
    public final String getShowname(){
        return showname;
    }
    /**
     * 激活时的公告频道(10跑马灯)
     */
    private final int notice;
    /**
     * 激活时的公告频道(10跑马灯)
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

    public Cfg_New_sever_rank_Bean(int id,int type,int parm,int serverEndTime,int rew_rank,String show_fake_rankeStr,String showname,int notice,String chatchannelStr){
        this.id = id;
        this.type = type;
        this.parm = parm;
        this.serverEndTime = serverEndTime;
        this.rew_rank = rew_rank;
        this.show_fake_ranke = new ReadIntegerArray(show_fake_rankeStr,",");
        this.showname = showname;
        this.notice = notice;
        this.chatchannel = new ReadIntegerArray(chatchannelStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("parm:").append(parm).append(";");
        str.append("serverEndTime:").append(serverEndTime).append(";");
        str.append("rew_rank:").append(rew_rank).append(";");
        str.append("show_fake_ranke:").append(show_fake_ranke).append(";");
        str.append("showname:").append(showname).append(";");
        str.append("notice:").append(notice).append(";");
        str.append("chatchannel:").append(chatchannel).append(";");
        return str.toString();
    }
}
