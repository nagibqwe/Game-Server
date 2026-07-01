/**
 * Auto generated, do not edit it
 *
 * sdkplatform配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Sdkplatform_Bean; 

	
public final class Cfg_Sdkplatform_Load implements IScriptConfig<Cfg_Sdkplatform_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Sdkplatform_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1101, new Cfg_Sdkplatform_Bean(1101,"CNY","CN","¥",1,"国内测试内网"));
        contaioners.put(1201, new Cfg_Sdkplatform_Bean(1201,"SGD","SG","S$",14,"新加坡"));
        contaioners.put(1301, new Cfg_Sdkplatform_Bean(1301,"VND","VIE","₫",7,"越南"));
        contaioners.put(1401, new Cfg_Sdkplatform_Bean(1401,"THB","TH","฿",11,"泰国安卓"));
        contaioners.put(1501, new Cfg_Sdkplatform_Bean(1501,"KRW","KR","₩",4,"韩国"));
        contaioners.put(1601, new Cfg_Sdkplatform_Bean(1601,"TWD","TW","NT$",3403,"台湾"));
        contaioners.put(1602, new Cfg_Sdkplatform_Bean(1602,"MO","TW","MOP$",3401,"澳门"));
        contaioners.put(1603, new Cfg_Sdkplatform_Bean(1603,"HK","TW","HK$",3399,"香港"));
        contaioners.put(1701, new Cfg_Sdkplatform_Bean(1701,"IDR","IN","Rp",15,"印尼"));
        contaioners.put(1801, new Cfg_Sdkplatform_Bean(1801,"JPY","JP","￥",5,"日本"));
        contaioners.put(1901, new Cfg_Sdkplatform_Bean(1901,"USD","EN","$",166,"美国"));
        contaioners.put(2001, new Cfg_Sdkplatform_Bean(2001,"EUR","EU","€",0,"欧洲"));
        contaioners.put(9001, new Cfg_Sdkplatform_Bean(9001,"CNY","QQ","¥",1,"QQ大厅"));
    }

}
