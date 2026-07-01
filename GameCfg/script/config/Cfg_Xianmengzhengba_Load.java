/**
 * Auto generated, do not edit it
 *
 * xianmengzhengba配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Xianmengzhengba_Bean; 

	
public final class Cfg_Xianmengzhengba_Load implements IScriptConfig<Cfg_Xianmengzhengba_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Xianmengzhengba_Bean> contaioners){
        contaioners.clear();
        contaioners.put(101, new Cfg_Xianmengzhengba_Bean(101,"加入仙盟",1,"11,1","10001,5,1,9}12,2000,1,9",1,"[010101]加入仙盟[-][158720]({0}/{1})[-]",50000));
        contaioners.put(102, new Cfg_Xianmengzhengba_Bean(102,"在仙盟拍卖行上架一次商品",1,"230,1","10001,5,1,9}3,500000,1,9",2,"[010101]在仙盟拍卖行上架一次商品[-][158720]({0}/{1})[-]",1304000));
        contaioners.put(103, new Cfg_Xianmengzhengba_Bean(103,"完成五次仙盟任务",1,"32,5","10001,5,1,9}2,50,1,9",3,"[010101]完成五次仙盟任务[-][158720]({0}/{1})[-]",56300));
        contaioners.put(104, new Cfg_Xianmengzhengba_Bean(104,"参与一次仙盟首领击杀",1,"142,1","10001,5,1,9}2,50,1,9",4,"[010101]参与一次仙盟首领击杀[-][158720]({0}/{1})[-]",58000));
        contaioners.put(201, new Cfg_Xianmengzhengba_Bean(201,"参与仙盟战作为盟主摧毁上古意志",2,"232,1","10004,5,1,9}2,200,1,9}30901,1,1,9",1,"[010101]在仙盟战中以盟主身份获得天仙仙盟第一名[-][158720]({0}/{1})[-]",57000));
        contaioners.put(202, new Cfg_Xianmengzhengba_Bean(202,"参与仙盟战并成功摧毁上古意志",2,"231,1","10004,3,1,9}2,100,1,9}11001,5,1,9",2,"[010101]参与仙盟战并成功摧毁上古意志[-][158720]({0}/{1})[-]",57000));
        contaioners.put(203, new Cfg_Xianmengzhengba_Bean(203,"参与一次仙盟战",2,"137,1","10004,2,1,9}2,50,1,9}16001,5,1,9",3,"[010101]参与仙盟战[-][158720]({0}/{1})[-]",57000));
        contaioners.put(304, new Cfg_Xianmengzhengba_Bean(304," 8个部位穿戴圣装",3,"236,8","81003,2,1,9}15,2000,1,9}2,50,1,9",1,"[010101]8个部位穿戴圣装[-][158720]({0}/{1})[-]",222100));
        contaioners.put(305, new Cfg_Xianmengzhengba_Bean(305," 10个部位穿戴圣装",3,"236,10","81003,4,1,9}15,4000,1,9}2,100,1,9",2,"[010101]10个部位穿戴圣装[-][158720]({0}/{1})[-]",222100));
        contaioners.put(306, new Cfg_Xianmengzhengba_Bean(306,"圣装强化总等级达到10级",3,"233,10","15,6000,1,9}16001,5,1,9}2,50,1,9",3,"[010101]圣装强化总等级达到10级[-][158720]({0}/{1})[-]",222200));
        contaioners.put(307, new Cfg_Xianmengzhengba_Bean(307,"激活一个5阶金色以上斗心",3,"234,5,6,1","83105,1,1,0}83106,1,1,1}16001,10,1,9}2,100,1,9",4,"[010101]激活5阶金色以上斗心[-][158720]({0}/{1})[-]",222100));
        contaioners.put(308, new Cfg_Xianmengzhengba_Bean(308,"合成一个5阶以上圣装",3,"235,5,1","16001,10,1,9}3,200000,1,9}2,50,1,9",5,"[010101]合成一个5阶以上红色圣装[-][158720]({0}/{1})[-]",222300));
        contaioners.put(309, new Cfg_Xianmengzhengba_Bean(309," 圣装系统战力达到50000",3,"131,50000","83105,1,1,0}83106,1,1,1}16001,10,1,9}2,50,1,9",6,"[010101]圣装战力达到50000[-][158720]({0}/{1})[-]",222100));
        contaioners.put(310, new Cfg_Xianmengzhengba_Bean(310,"圣装系统战力达到210000",3,"131,210000","83117,1,1,0}83118,1,1,1}16001,20,1,9}2,100,1,9",7,"[010101]圣装战力达到210000[-][158720]({0}/{1})[-]",222100));
    }

}
