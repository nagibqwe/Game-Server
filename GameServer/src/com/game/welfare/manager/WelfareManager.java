package com.game.welfare.manager;

import com.data.FunctionStart;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.welfare.script.IWelfareFreeGiftScript;
import com.game.welfare.script.IWelfareScript;
import game.core.command.CommandProcessor;
import game.core.script.IScript;
import game.core.script.ScriptManager;
import game.message.WelfareMessage.WelfareType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WelfareManager extends CommandProcessor {
    private final Logger log = LogManager.getLogger(WelfareManager.class);

    private enum Singleton {
        INSTANCE;
        WelfareManager manager;

        Singleton() {
            this.manager = new WelfareManager();
        }

        WelfareManager getProcessor() {
            return manager;
        }
    }

    public static WelfareManager getInstance() {
        return WelfareManager.Singleton.INSTANCE.getProcessor();
    }

    private WelfareManager() {
        super("WelfareManager");
    }

    /**
     * 玩家上线
     * @param player
     */
    public void playerOnline(Player player) {
        for (int i = WelfareType.TypeStart_VALUE + 1; i < WelfareType.TypeEnd_VALUE; i++) {
            IScript iScript = getScript(WelfareType.valueOf(i));
            if (iScript == null)
                continue;

            // 上线刷新
            ((IWelfareScript) iScript).playerOnline(player);

            // 推送数据
            ((IWelfareScript) iScript).freshDataNtf(player);
        }

        IScript script = ScriptManager.getInstance().GetScriptClass(ScriptEnum.WelfareFreeGiftScript);
        if (script != null) {
            if(script instanceof IWelfareFreeGiftScript){
                IWelfareFreeGiftScript welfareFreeGiftScript = (IWelfareFreeGiftScript)script;
                welfareFreeGiftScript.playerOnline(player);
            }
        }

    }

    /**
     * 通过福利类型获取脚本ID
     * @param welfareType
     * @return
     */
    private int getScriptID(WelfareType welfareType) {
        switch (welfareType) {
            case LoginGift:
                return ScriptEnum.LoginGiftBaseScript;
            case DayGift:
                return ScriptEnum.DayGiftBaseScript;
            case DayCheckIn:
                return ScriptEnum.DayCheckInBaseScript;
            case FeelingExp:
                return ScriptEnum.FeelingExpBaseScript;
            case GrowthFund:
                return ScriptEnum.GrowthFundBaseScript;
            case ExchangeGift:
                return ScriptEnum.ExchangeGiftBaseScript;
            case ExclusiveCard:
                return ScriptEnum.ExclusiveCardBaseScript;
            case LevelGift:
                return ScriptEnum.LevelGiftBaseScript;
            case UpdateNotice:
                return ScriptEnum.UpdateNoticScript;
            case InvestPeak:
                return ScriptEnum.InvestPeak;
            default:
                return -1;
        }
    }

    /**
     * 返回福利的脚本
     * @param welfareType
     * @return
     */
    public IWelfareScript getScript(WelfareType welfareType) {
        int id = getScriptID(welfareType);
        if (id == -1) {
            log.error("福利类型错误：" + welfareType.getNumber());
            return null;
        }

        IScript script = ScriptManager.getInstance().GetScriptClass(id);
        if (script == null) {
            log.error("没有找到该福利类型的脚本：" + welfareType.getNumber() + " 脚本ID：" + id);
            return null;
        }
        return (IWelfareScript) script;
    }

    /**
     * 福利活动是否开启
     * @param player
     * @param welfareType
     * @return
     */
    public boolean isOpen(Player player, WelfareType welfareType) {
        int st = -1;
        switch (welfareType) {
            case LoginGift:
                st = FunctionStart.WelfareLoginGift;
                break;
            case DayGift:
                st = FunctionStart.WelfareDailyGift;
                break;
            case DayCheckIn:
                st = FunctionStart.WelfareDailyCheck;
                break;
            case FeelingExp:
                st = FunctionStart.WelfareWuDao;
                break;
            case GrowthFund:
                st = FunctionStart.WelfareInvestment;
                break;
            case ExchangeGift:
                st = FunctionStart.WelfareExchangeGift;
                break;
            case ExclusiveCard:
                st = FunctionStart.WelfareCard;
                break;
            case InvestPeak:
                st = FunctionStart.WelfareIPeakFund;
                break;
            default:
        }

        if (st == -1)
            return false;

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Welfare))
            return false;

        return Manager.controlManager.deal().isOpenFunction(player, st);
    }

    /**
     * 如果处理过程中发生异常，记录错误日志信息.
     *
     * @param message 错误信息描述
     */
    @Override
    public void writeError(String message) {
        log.error("福利模块失败：" + message);
    }

    /**
     * 如果处理过程中发生异常，记录错误日志信息.
     *
     * @param message 错误信息描述
     * @param t       产生错误的异常类
     */
    @Override
    public void writeError(String message, Throwable t) {
        log.error("福利模块失败：" + message, t);
    }
    //    /**
//     * 方法一对集合进行深拷贝 注意需要对泛型类进行序列化(实现Serializable)
//     * @param src
//     * @param <T>
//     * @return
//     * @throws IOException
//     * @throws ClassNotFoundException
//     */
//    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
//        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
//        ObjectOutputStream out = new ObjectOutputStream(byteOut);
//        out.writeObject(src);
//
//        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
//        ObjectInputStream in = new ObjectInputStream(byteIn);
//        @SuppressWarnings("unchecked")
//        List<T> dest = (List<T>) in.readObject();
//        return dest;
//    }
}
