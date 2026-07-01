/**
 * Auto generated, do not edit it
 *
 * redPacket配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_RedPacket_Bean; 

	
public final class Cfg_RedPacket_Load implements IScriptConfig<Cfg_RedPacket_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_RedPacket_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1001, new Cfg_RedPacket_Bean(1001,1,"首充红包",1,50));
        contaioners.put(1002, new Cfg_RedPacket_Bean(1002,2,"累积充值500蓝钻",500,100));
        contaioners.put(1003, new Cfg_RedPacket_Bean(1003,2,"累积充值1000蓝钻",1000,300));
        contaioners.put(1004, new Cfg_RedPacket_Bean(1004,2,"累积充值3000蓝钻",3000,500));
        contaioners.put(1005, new Cfg_RedPacket_Bean(1005,2,"累积充值5000蓝钻",5000,1000));
        contaioners.put(1006, new Cfg_RedPacket_Bean(1006,2,"累积充值10000蓝钻",10000,1000));
        contaioners.put(1007, new Cfg_RedPacket_Bean(1007,2,"累积充值30000蓝钻",30000,1000));
        contaioners.put(1008, new Cfg_RedPacket_Bean(1008,2,"累积充值50000蓝钻",50000,1000));
        contaioners.put(1009, new Cfg_RedPacket_Bean(1009,2,"累积充值100000蓝钻",100000,1000));
        contaioners.put(1010, new Cfg_RedPacket_Bean(1010,2,"累积充值300000蓝钻",300000,1000));
        contaioners.put(1011, new Cfg_RedPacket_Bean(1011,2,"累积充值500000蓝钻",500000,1000));
        contaioners.put(1012, new Cfg_RedPacket_Bean(1012,2,"累积充值1000000蓝钻",1000000,1000));
        contaioners.put(1013, new Cfg_RedPacket_Bean(1013,2,"累积充值3000000蓝钻",3000000,1000));
        contaioners.put(1014, new Cfg_RedPacket_Bean(1014,2,"累积充值5000000蓝钻",5000000,1000));
        contaioners.put(2001, new Cfg_RedPacket_Bean(2001,3,"每日首充红包",1,50));
        contaioners.put(2002, new Cfg_RedPacket_Bean(2002,4,"每日充值100蓝钻",100,50));
        contaioners.put(2003, new Cfg_RedPacket_Bean(2003,4,"每日充值300蓝钻",300,100));
        contaioners.put(2004, new Cfg_RedPacket_Bean(2004,4,"每日充值500蓝钻",500,100));
        contaioners.put(2005, new Cfg_RedPacket_Bean(2005,4,"每日充值1000蓝钻",1000,100));
        contaioners.put(2006, new Cfg_RedPacket_Bean(2006,4,"每日充值2000蓝钻",2000,300));
        contaioners.put(2007, new Cfg_RedPacket_Bean(2007,4,"每日充值5000蓝钻",5000,300));
        contaioners.put(2008, new Cfg_RedPacket_Bean(2008,4,"每日充值10000蓝钻",10000,300));
    }

}
