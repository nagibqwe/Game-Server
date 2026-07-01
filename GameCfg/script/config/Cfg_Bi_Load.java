/**
 * Auto generated, do not edit it
 *
 * bi配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Bi_Bean; 

	
public final class Cfg_Bi_Load implements IScriptConfig<Cfg_Bi_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Bi_Bean> contaioners){
        contaioners.clear();
        contaioners.put(10001, new Cfg_Bi_Bean(10001,"XMTaskDailyEnter",1,1));
        contaioners.put(10002, new Cfg_Bi_Bean(10002,"XMTaskBuildEnter",1,1));
        contaioners.put(20001, new Cfg_Bi_Bean(20001,"ArenaDailyEnter",1,1));
        contaioners.put(20002, new Cfg_Bi_Bean(20002,"ArenaMainEnter",1,1));
        contaioners.put(30001, new Cfg_Bi_Bean(30001,"SkillMainEnter",1,1));
        contaioners.put(30002, new Cfg_Bi_Bean(30002,"MountMainEnter",1,1));
        contaioners.put(30003, new Cfg_Bi_Bean(30003,"WingMainEnter",1,1));
        contaioners.put(30004, new Cfg_Bi_Bean(30004,"PetMainEnter",1,1));
        contaioners.put(30005, new Cfg_Bi_Bean(30005,"FabaoMainEnter",1,1));
        contaioners.put(30006, new Cfg_Bi_Bean(30006,"ShiHaiPlayerUIEnter",1,1));
        contaioners.put(30007, new Cfg_Bi_Bean(30007,"GemLianQiEnter",1,1));
        contaioners.put(40001, new Cfg_Bi_Bean(40001,"WYJDailyEnter",1,1));
        contaioners.put(40002, new Cfg_Bi_Bean(40002,"WYJCopyMapEnter",1,1));
        contaioners.put(40004, new Cfg_Bi_Bean(40004,"WYJStrongRechallenge",1,1));
        contaioners.put(40005, new Cfg_Bi_Bean(40005,"WYJWeakRechallenge",1,1));
        contaioners.put(40006, new Cfg_Bi_Bean(40006,"WYJLeaveChallengeing",1,1));
        contaioners.put(40007, new Cfg_Bi_Bean(40007,"WYJLeaveSucceed",1,1));
        contaioners.put(40008, new Cfg_Bi_Bean(40008,"WYJLeaveFailed",1,1));
        contaioners.put(40009, new Cfg_Bi_Bean(40009,"WYJGeneralSucceed",1,1));
        contaioners.put(40010, new Cfg_Bi_Bean(40010,"WYJPerfectSucceed",1,1));
        contaioners.put(50001, new Cfg_Bi_Bean(50001,"DNYFDailyEnter",1,1));
        contaioners.put(60001, new Cfg_Bi_Bean(60001,"TJZMDailyEnter",1,1));
        contaioners.put(60002, new Cfg_Bi_Bean(60002,"TJZMCopyMapEnter",1,1));
        contaioners.put(60003, new Cfg_Bi_Bean(60003,"TJZMChallenge",1,1));
        contaioners.put(60004, new Cfg_Bi_Bean(60004,"TJZMLeave",1,1));
        contaioners.put(70001, new Cfg_Bi_Bean(70001,"LYYTDailyEnter",1,1));
        contaioners.put(70002, new Cfg_Bi_Bean(70002,"LYYTCopyMapEnter",1,1));
        contaioners.put(70003, new Cfg_Bi_Bean(70003,"LYYTChallenge",1,1));
        contaioners.put(70004, new Cfg_Bi_Bean(70004,"LYYTHearten",1,1));
        contaioners.put(70005, new Cfg_Bi_Bean(70005,"LYYTEfficiency",1,1));
        contaioners.put(70006, new Cfg_Bi_Bean(70006,"LYYTLeave",1,1));
        contaioners.put(80001, new Cfg_Bi_Bean(80001,"XMHJDailyEnter",1,1));
        contaioners.put(80002, new Cfg_Bi_Bean(80002,"XMHJCopyMapEnter",1,1));
        contaioners.put(80003, new Cfg_Bi_Bean(80003,"XMHJSingeChallenge",1,1));
        contaioners.put(80004, new Cfg_Bi_Bean(80004,"XMHJTeamAutoMatch",1,1));
        contaioners.put(80005, new Cfg_Bi_Bean(80005,"XMHJCreatTeam",1,1));
        contaioners.put(80006, new Cfg_Bi_Bean(80006,"XMHJTeamSingeChallenge",1,1));
        contaioners.put(80007, new Cfg_Bi_Bean(80007,"XMHJTeamManyChallenge",1,1));
        contaioners.put(80008, new Cfg_Bi_Bean(80008,"XMHJLeaveChallengeing",1,1));
        contaioners.put(80009, new Cfg_Bi_Bean(80009,"XMHJLeaveSucceed",1,1));
        contaioners.put(80010, new Cfg_Bi_Bean(80010,"XMHJLeaveFailed",1,1));
        contaioners.put(90001, new Cfg_Bi_Bean(90001,"SLTSingeChallenge",1,1));
        contaioners.put(90002, new Cfg_Bi_Bean(90002,"SLTTeamAutoMatch",1,1));
        contaioners.put(90003, new Cfg_Bi_Bean(90003,"SLTCreatTeam",1,1));
        contaioners.put(90004, new Cfg_Bi_Bean(90004,"SLTTeamSingeChallenge",1,1));
        contaioners.put(90005, new Cfg_Bi_Bean(90005,"SLTTeamManyChallenge",1,1));
        contaioners.put(90006, new Cfg_Bi_Bean(90006,"SLTLeaveChallengeing",1,1));
        contaioners.put(90007, new Cfg_Bi_Bean(90007,"SLTLeaveSucceed",1,1));
        contaioners.put(90008, new Cfg_Bi_Bean(90008,"SLTLeaveFailed",1,1));
        contaioners.put(90009, new Cfg_Bi_Bean(90009,"SLTDailyEnter",1,1));
        contaioners.put(90010, new Cfg_Bi_Bean(90010,"SLTCopyMapEnter",1,1));
        contaioners.put(100001, new Cfg_Bi_Bean(100001,"ZMCDEnter",1,1));
        contaioners.put(100002, new Cfg_Bi_Bean(100002,"ZMCDDetail",1,1));
        contaioners.put(100003, new Cfg_Bi_Bean(100003,"ZMCDLeave",1,1));
        contaioners.put(100004, new Cfg_Bi_Bean(100004,"ZMCDDailyEnter",1,1));
        contaioners.put(100005, new Cfg_Bi_Bean(100005,"ZMCDMainEnter",1,1));
        contaioners.put(110001, new Cfg_Bi_Bean(110001,"TMGCEnter",1,1));
        contaioners.put(110002, new Cfg_Bi_Bean(110002,"TMGCLeave",1,1));
        contaioners.put(120001, new Cfg_Bi_Bean(120001,"TDMJEnter",1,1));
        contaioners.put(120002, new Cfg_Bi_Bean(120002,"TDMJLeave",1,1));
        contaioners.put(130001, new Cfg_Bi_Bean(130001,"SJDTEnter",1,1));
        contaioners.put(130002, new Cfg_Bi_Bean(130002,"SJDTLeave",1,1));
        contaioners.put(140001, new Cfg_Bi_Bean(140001,"TXZCEnter",1,1));
        contaioners.put(140002, new Cfg_Bi_Bean(140002,"TXZCLeave",1,1));
        contaioners.put(150001, new Cfg_Bi_Bean(150001,"XMFightEnter",1,1));
        contaioners.put(150002, new Cfg_Bi_Bean(150002,"XMFightLeave",1,1));
        contaioners.put(160001, new Cfg_Bi_Bean(160001,"FDBossDailyEnter",1,1));
        contaioners.put(160002, new Cfg_Bi_Bean(160002,"FDBossMainEnter",1,1));
        contaioners.put(170001, new Cfg_Bi_Bean(170001,"WJXYDailyEnter",1,1));
        contaioners.put(170002, new Cfg_Bi_Bean(170002,"WJXYCopyMapEnter",1,1));
        contaioners.put(170003, new Cfg_Bi_Bean(170003,"WJXYGoLoopLayer",1,1));
        contaioners.put(170004, new Cfg_Bi_Bean(170004,"WJXYGo1Layer",1,1));
        contaioners.put(170005, new Cfg_Bi_Bean(170005,"WJXYGo2Layer",1,1));
        contaioners.put(170006, new Cfg_Bi_Bean(170006,"WJXYGo3Layer",1,1));
        contaioners.put(170007, new Cfg_Bi_Bean(170007,"WJXYGo4Layer",1,1));
        contaioners.put(170008, new Cfg_Bi_Bean(170008,"WJXYGo5Layer",1,1));
        contaioners.put(170009, new Cfg_Bi_Bean(170009,"WJXYGo6Layer",1,1));
        contaioners.put(170010, new Cfg_Bi_Bean(170010,"WJXYLeave",1,1));
        contaioners.put(170011, new Cfg_Bi_Bean(170011,"WXSLCopyMapEnter",1,1));
        contaioners.put(170012, new Cfg_Bi_Bean(170012,"WXSLGoNewPlayerLayer",1,1));
        contaioners.put(170013, new Cfg_Bi_Bean(170013,"WXSLGo1NewPlayerLayer",1,1));
        contaioners.put(170014, new Cfg_Bi_Bean(170014,"WXSLGo2NewPlayerLayer",1,1));
        contaioners.put(170015, new Cfg_Bi_Bean(170015,"WXSLGo3NewPlayerLayer",1,1));
        contaioners.put(180001, new Cfg_Bi_Bean(180001,"JJSYDailyEnter",1,1));
        contaioners.put(180002, new Cfg_Bi_Bean(180002,"JJSYCopyMapEnter",1,1));
        contaioners.put(190001, new Cfg_Bi_Bean(190001,"NSFYDailyEnter",1,1));
        contaioners.put(190002, new Cfg_Bi_Bean(190002,"NSFYMainEnter",1,1));
        contaioners.put(190003, new Cfg_Bi_Bean(190003,"NSFYGo1Layer",1,1));
        contaioners.put(190004, new Cfg_Bi_Bean(190004,"NSFYGo2Layer",1,1));
        contaioners.put(190005, new Cfg_Bi_Bean(190005,"NSFYGo3Layer",1,1));
        contaioners.put(190006, new Cfg_Bi_Bean(190006,"NSFYGo4Layer",1,1));
        contaioners.put(190007, new Cfg_Bi_Bean(190007,"NSFYGo5Layer",1,1));
        contaioners.put(190008, new Cfg_Bi_Bean(190008,"NSFYLeave",1,1));
        contaioners.put(200001, new Cfg_Bi_Bean(200001,"HDMJDailyEnter",1,1));
        contaioners.put(200002, new Cfg_Bi_Bean(200002,"HDMJMainEnter",1,1));
        contaioners.put(200008, new Cfg_Bi_Bean(200008,"HDMJLeave",1,1));
        contaioners.put(210001, new Cfg_Bi_Bean(210001,"TotalRechargeMainEnter",1,1));
        contaioners.put(210002, new Cfg_Bi_Bean(210002,"TotalRechargeType60",1,1));
        contaioners.put(210003, new Cfg_Bi_Bean(210003,"TotalRechargeType300",1,1));
        contaioners.put(210004, new Cfg_Bi_Bean(210004,"TotalRechargeType680",1,1));
        contaioners.put(210005, new Cfg_Bi_Bean(210005,"TotalRechargeType1280",1,1));
        contaioners.put(210006, new Cfg_Bi_Bean(210006,"TotalRechargePay60",1,1));
        contaioners.put(210007, new Cfg_Bi_Bean(210007,"TotalRechargePay300",1,1));
        contaioners.put(210008, new Cfg_Bi_Bean(210008,"TotalRechargePay680",1,1));
        contaioners.put(210009, new Cfg_Bi_Bean(210009,"TotalRechargePay1280",1,1));
        contaioners.put(220001, new Cfg_Bi_Bean(220001,"NewServerAdvantageFirstRecharge",1,1));
        contaioners.put(220002, new Cfg_Bi_Bean(220002,"NewServerAdvantageWeekCard",1,1));
        contaioners.put(220003, new Cfg_Bi_Bean(220003,"NewServerAdvantageMonthCard",1,1));
        contaioners.put(220004, new Cfg_Bi_Bean(220004,"NewServerAdvantageGrowUp",1,1));
        contaioners.put(220005, new Cfg_Bi_Bean(220005,"NewServerAdvantageTotalTask",1,1));
        contaioners.put(230001, new Cfg_Bi_Bean(230001,"PerfectLoveMarriage1",1,1));
        contaioners.put(230002, new Cfg_Bi_Bean(230002,"PerfectLoveMarriage2",1,1));
        contaioners.put(230003, new Cfg_Bi_Bean(230003,"PerfectLoveMarriage3",1,1));
        contaioners.put(230004, new Cfg_Bi_Bean(230004,"PerfectLoveTotalTask",1,1));
        contaioners.put(240001, new Cfg_Bi_Bean(240001,"GrowthWayLLEnter",1,1));
        contaioners.put(240002, new Cfg_Bi_Bean(240002,"GrowthWaySZEnter",1,1));
        contaioners.put(240003, new Cfg_Bi_Bean(240003,"GrowthWayJBEnter",1,1));
        contaioners.put(240004, new Cfg_Bi_Bean(240004,"GrowthWayXMEnter",1,1));
        contaioners.put(240005, new Cfg_Bi_Bean(240005,"GrowthWayAward1",1,1));
        contaioners.put(240006, new Cfg_Bi_Bean(240006,"GrowthWayAward2",1,1));
        contaioners.put(240007, new Cfg_Bi_Bean(240007,"GrowthWayAward3",1,1));
        contaioners.put(240008, new Cfg_Bi_Bean(240008,"GrowthWayAward4",1,1));
        contaioners.put(240009, new Cfg_Bi_Bean(240009,"GrowthWayAward5",1,1));
        contaioners.put(240010, new Cfg_Bi_Bean(240010,"GrowthWayAward6",1,1));
        contaioners.put(250001, new Cfg_Bi_Bean(250001,"SwordSoulKuaiSu",1,1));
        contaioners.put(250002, new Cfg_Bi_Bean(250002,"SwordSoulGuaJi",1,1));
        contaioners.put(250003, new Cfg_Bi_Bean(250003,"SwordSoulAwardPreview",1,1));
        contaioners.put(250004, new Cfg_Bi_Bean(250004,"SwordSoulXunBao1",1,1));
        contaioners.put(250005, new Cfg_Bi_Bean(250005,"SwordSoulXunBao10",1,1));
        contaioners.put(250006, new Cfg_Bi_Bean(250006,"SwordSoulXianPoExchange",1,1));
        contaioners.put(250007, new Cfg_Bi_Bean(250007,"SwordSoulXianPoInlay",1,1));
        contaioners.put(250008, new Cfg_Bi_Bean(250008,"SwordSoulXianPoResolve",1,1));
        contaioners.put(250009, new Cfg_Bi_Bean(250009,"SwordSoulXianPoComposition",1,1));
        contaioners.put(260001, new Cfg_Bi_Bean(260001,"MarriageGotoRedWife",1,1));
        contaioners.put(260002, new Cfg_Bi_Bean(260002,"MarriageReward",1,1));
        contaioners.put(260003, new Cfg_Bi_Bean(260003,"MarriageGetMarried",1,1));
        contaioners.put(260004, new Cfg_Bi_Bean(260004,"MarriageShop",1,1));
        contaioners.put(260005, new Cfg_Bi_Bean(260005,"MarriageDivorce",1,1));
        contaioners.put(260006, new Cfg_Bi_Bean(260006,"MarriageBoxSelfBuy",1,1));
        contaioners.put(260007, new Cfg_Bi_Bean(260007,"MarriageBoxEverdayGet",1,1));
        contaioners.put(260008, new Cfg_Bi_Bean(260008,"MarriageBoxReqPersent",1,1));
        contaioners.put(260009, new Cfg_Bi_Bean(260009,"MarriageBoxBuyPersent",1,1));
        contaioners.put(270001, new Cfg_Bi_Bean(270001,"TargetSystemReceive",1,1));
        contaioners.put(280001, new Cfg_Bi_Bean(280001,"XiSuiZhiLu",1,1));
    }

}
