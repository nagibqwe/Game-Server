/**
 * Auto generated, do not edit it
 *
 * bossnew_drop配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Bossnew_drop_Bean{
    /**
     * 掉落关联组
     */
    private final int ID;
    /**
     * 掉落关联组
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 掉落类型（0:世界boss
1:个人世界boss
2:守护宗派boss
3.九天争锋boss
4.boss之家
5.神兽岛
6.假无线层
7.境界boss
8.荒古神坛）
     */
    private final int Drop_type;
    /**
     * 掉落类型（0:世界boss
1:个人世界boss
2:守护宗派boss
3.九天争锋boss
4.boss之家
5.神兽岛
6.假无线层
7.境界boss
8.荒古神坛）
     * @return
     */
    public final int getDrop_type(){
        return Drop_type;
    }
    /**
     * 击杀归属掉落
     */
    private final int Drop_Ordinary;
    /**
     * 击杀归属掉落
     * @return
     */
    public final int getDrop_Ordinary(){
        return Drop_Ordinary;
    }
    /**
     * 关联计数下限
     */
    private final int Relation_Num_min;
    /**
     * 关联计数下限
     * @return
     */
    public final int getRelation_Num_min(){
        return Relation_Num_min;
    }
    /**
     * 关联计数上限
     */
    private final int Relation_Num_max;
    /**
     * 关联计数上限
     * @return
     */
    public final int getRelation_Num_max(){
        return Relation_Num_max;
    }
    /**
     * 次数特殊掉落
     */
    private final int Relation_Dorp;
    /**
     * 次数特殊掉落
     * @return
     */
    public final int getRelation_Dorp(){
        return Relation_Dorp;
    }
    /**
     * 排名掉落奖励(@_@)
     */
    private final ReadIntegerArray Ranking_Drop;
    /**
     * 排名掉落奖励(@_@)
     * @return
     */
    public final ReadIntegerArray getRanking_Drop(){
        return Ranking_Drop;
    }
    /**
     * 取排名的前几名（不重复）【和之前的奖励一一对应】(@_@)
     */
    private final ReadIntegerArray rank_num;
    /**
     * 取排名的前几名（不重复）【和之前的奖励一一对应】(@_@)
     * @return
     */
    public final ReadIntegerArray getRank_num(){
        return rank_num;
    }
    /**
     * 阳光掉落奖励
     */
    private final int Per_Capita_Dorp;
    /**
     * 阳光掉落奖励
     * @return
     */
    public final int getPer_Capita_Dorp(){
        return Per_Capita_Dorp;
    }
    /**
     * 境界额外奖励
     */
    private final int Per_Capita_Dorp_2;
    /**
     * 境界额外奖励
     * @return
     */
    public final int getPer_Capita_Dorp_2(){
        return Per_Capita_Dorp_2;
    }
    /**
     * 需要达到的境界
     */
    private final int state;
    /**
     * 需要达到的境界
     * @return
     */
    public final int getState(){
        return state;
    }

    public Cfg_Bossnew_drop_Bean(int ID,int Drop_type,int Drop_Ordinary,int Relation_Num_min,int Relation_Num_max,int Relation_Dorp,String Ranking_DropStr,String rank_numStr,int Per_Capita_Dorp,int Per_Capita_Dorp_2,int state){
        this.ID = ID;
        this.Drop_type = Drop_type;
        this.Drop_Ordinary = Drop_Ordinary;
        this.Relation_Num_min = Relation_Num_min;
        this.Relation_Num_max = Relation_Num_max;
        this.Relation_Dorp = Relation_Dorp;
        this.Ranking_Drop = new ReadIntegerArray(Ranking_DropStr,",");
        this.rank_num = new ReadIntegerArray(rank_numStr,",");
        this.Per_Capita_Dorp = Per_Capita_Dorp;
        this.Per_Capita_Dorp_2 = Per_Capita_Dorp_2;
        this.state = state;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("Drop_type:").append(Drop_type).append(";");
        str.append("Drop_Ordinary:").append(Drop_Ordinary).append(";");
        str.append("Relation_Num_min:").append(Relation_Num_min).append(";");
        str.append("Relation_Num_max:").append(Relation_Num_max).append(";");
        str.append("Relation_Dorp:").append(Relation_Dorp).append(";");
        str.append("Ranking_Drop:").append(Ranking_Drop).append(";");
        str.append("rank_num:").append(rank_num).append(";");
        str.append("Per_Capita_Dorp:").append(Per_Capita_Dorp).append(";");
        str.append("Per_Capita_Dorp_2:").append(Per_Capita_Dorp_2).append(";");
        str.append("state:").append(state).append(";");
        return str.toString();
    }
}
