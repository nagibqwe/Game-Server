/**
 * Auto generated, do not edit it
 *
 * Rank_base配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Rank_base_Bean; 

	
public final class Cfg_Rank_base_Load implements IScriptConfig<Cfg_Rank_base_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Rank_base_Bean> contaioners){
        contaioners.clear();
        contaioners.put(101, new Cfg_Rank_base_Bean(101,"等级",1,1,"个人信息",2,"等级",1,"RankPlayerLevel;等级",0,50,200,1,1,1));
        contaioners.put(102, new Cfg_Rank_base_Bean(102,"战力",1,2,"个人信息",1,"战力",1,"RankPlayerPower;战力",0,50,200,1,1,1));
        contaioners.put(105, new Cfg_Rank_base_Bean(105,"装备",1,3,"个人信息",9,"战力",1,"RankPlayerCloth;时装强化",0,50,200,1,1,1));
        contaioners.put(110, new Cfg_Rank_base_Bean(110,"宝石",1,4,"个人信息",44,"战力",1,"RankArenaKa;卡牌",0,50,200,0,1,1));
        contaioners.put(112, new Cfg_Rank_base_Bean(112,"洗练",1,5,"个人信息",0,"战力",1,"",0,50,200,1,1,1));
        contaioners.put(113, new Cfg_Rank_base_Bean(113,"强化",1,6,"个人信息",0,"战力",1,"",0,50,200,0,1,1));
        contaioners.put(114, new Cfg_Rank_base_Bean(114,"宝石",1,7,"个人信息",0,"战力",1,"",0,50,200,1,1,1));
        contaioners.put(115, new Cfg_Rank_base_Bean(115,"星级",1,8,"个人信息",0,"星级",1,"",0,50,200,0,1,1));
        contaioners.put(116, new Cfg_Rank_base_Bean(116,"星级",1,9,"个人信息",0,"星级",1,"",0,50,200,1,1,1));
        contaioners.put(118, new Cfg_Rank_base_Bean(118,"灵体",1,10,"个人信息",0,"灵体",1,"",0,50,200,0,1,5));
        contaioners.put(119, new Cfg_Rank_base_Bean(119,"仙甲",1,11,"个人信息",0,"灵体",1,"",0,50,200,0,1,3));
        contaioners.put(120, new Cfg_Rank_base_Bean(120,"圣装",1,12,"个人信息",0,"圣装",1,"",0,50,200,1,1,2));
        contaioners.put(121, new Cfg_Rank_base_Bean(121,"神兽",1,13,"个人信息",0,"神兽",1,"",0,50,200,1,1,0));
        contaioners.put(126, new Cfg_Rank_base_Bean(126,"消费排名",1,14,"个人信息",0,"消费",1,"",0,50,200,0,1,1));
        contaioners.put(103, new Cfg_Rank_base_Bean(103,"坐骑",2,15,"玩法信息",10,"战力",1,"RankPlayerHun;战魂",0,50,200,1,3,1));
        contaioners.put(104, new Cfg_Rank_base_Bean(104,"仙羽",2,16,"玩法信息",0,"战力",1,"",0,50,200,1,1,1));
        contaioners.put(109, new Cfg_Rank_base_Bean(109,"神兵",2,17,"玩法信息",0,"战力",1,"",0,50,200,1,1,1));
        contaioners.put(106, new Cfg_Rank_base_Bean(106,"法宝",2,18,"玩法信息",0,"战力",1,"RankArena;竞技",0,50,200,1,4,0));
        contaioners.put(117, new Cfg_Rank_base_Bean(117,"宠物",2,19,"玩法信息",0,"宠物",1,"",0,50,200,1,2,4));
        contaioners.put(122, new Cfg_Rank_base_Bean(122,"宠物御魂",2,20,"玩法信息",0,"宠物",1,"",0,50,200,1,2,4));
        contaioners.put(123, new Cfg_Rank_base_Bean(123,"宠物等级",2,21,"玩法信息",0,"宠物",1,"",0,50,200,1,2,4));
        contaioners.put(124, new Cfg_Rank_base_Bean(124,"坐骑御魂",2,22,"玩法信息",0,"坐骑",1,"",0,50,200,1,3,4));
        contaioners.put(125, new Cfg_Rank_base_Bean(125,"坐骑等级",2,23,"玩法信息",0,"坐骑",1,"",0,50,200,1,3,4));
        contaioners.put(127, new Cfg_Rank_base_Bean(127,"魂甲",2,24,"玩法信息",0,"魂甲",7,"",0,50,200,1,5,0));
        contaioners.put(128, new Cfg_Rank_base_Bean(128,"八卦",2,25,"玩法信息",0,"魂甲",7,"",0,50,200,0,1,0));
        contaioners.put(129, new Cfg_Rank_base_Bean(129,"灵魄",2,26,"玩法信息",0,"魂甲",7,"",0,50,200,1,1,0));
        contaioners.put(130, new Cfg_Rank_base_Bean(130,"魔魂",2,27,"玩法信息",0,"魂甲",7,"",0,50,200,1,1,0));
        contaioners.put(131, new Cfg_Rank_base_Bean(131,"坐骑脉轮",2,28,"玩法信息",0,"魂甲",7,"",0,50,200,1,1,0));
        contaioners.put(201, new Cfg_Rank_base_Bean(201,"竞技场",2,1025,"玩法信息",45,"层数",1,"RankArenaQiang;枪手",127,50,200,1,1,1));
        contaioners.put(202, new Cfg_Rank_base_Bean(202,"识海",2,1026,"玩法信息",0,"层数",1,"RankGuild;公会",127,50,200,0,1,0));
        contaioners.put(301, new Cfg_Rank_base_Bean(301,"宗派战力",3,1027,"其他",0,"战力",1,"RankEquip;装备",127,50,200,0,1,0));
        contaioners.put(401, new Cfg_Rank_base_Bean(401,"魅力榜",3,1028,"其他",0,"收花总数",1,"RankShen;神物",1,50,200,0,1,0));
        contaioners.put(402, new Cfg_Rank_base_Bean(402,"送花榜",3,1029,"其他",13,"送花总数",1,"RankShenWing;翅膀等级",1,50,200,0,1,0));
        contaioners.put(403, new Cfg_Rank_base_Bean(403,"亲密度榜",3,1030,"其他",13,"亲密度",1,"",0,50,200,1,1,0));
        contaioners.put(501, new Cfg_Rank_base_Bean(501,"名人堂排名",3,1031,"其他",0,"名人堂排名",1,"",0,50,100,0,1,0));
        contaioners.put(601, new Cfg_Rank_base_Bean(601,"天墟战场名人堂",3,1032,"其他",0,"天墟战场",0,"",0,50,200,0,1,0));
    }

}
