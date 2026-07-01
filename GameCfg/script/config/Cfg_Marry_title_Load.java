/**
 * Auto generated, do not edit it
 *
 * marry_title配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Marry_title_Bean; 

	
public final class Cfg_Marry_title_Load implements IScriptConfig<Cfg_Marry_title_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Marry_title_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Marry_title_Bean(1,"与君相知",30304,105,2013,0));
        contaioners.put(2, new Cfg_Marry_title_Bean(2,"鱼水之欢",30305,201,3344,0));
        contaioners.put(3, new Cfg_Marry_title_Bean(3,"相濡以沫",30306,301,5200,0));
        contaioners.put(4, new Cfg_Marry_title_Bean(4,"白首永偕",30307,401,9999,1));
        contaioners.put(5, new Cfg_Marry_title_Bean(5,"死生契阔",30308,501,16920,1));
        contaioners.put(6, new Cfg_Marry_title_Bean(6,"心有灵犀",30310,601,28920,1));
        contaioners.put(7, new Cfg_Marry_title_Bean(7,"琴瑟和鸣",30311,701,49999,1));
        contaioners.put(8, new Cfg_Marry_title_Bean(8,"天作之合",30312,801,99999,1));
        contaioners.put(9, new Cfg_Marry_title_Bean(9,"比翼双飞",30313,901,199999,1));
        contaioners.put(10, new Cfg_Marry_title_Bean(10,"神仙眷侣",30314,1001,249999,1));
    }

}
