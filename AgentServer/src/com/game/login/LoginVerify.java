package com.game.login;

import com.game.db.bean.ForbidBean;
import com.game.db.dao.ForbidDao;
import com.game.db.dao.WhiteDao;
import com.game.login.script.ILoginScript;
import game.core.script.IScript;
import game.core.script.ScriptManager;
import game.core.util.TimeUtils;
import game.message.LoginMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @doc 登录验证
 */
public class LoginVerify {

    private static final Logger logger = LogManager.getLogger(LoginVerify.class);

    //屏蔽的账号数据，包括ip、553uuid、mac地址、imei、机器唯一码
    private final ConcurrentHashMap<String, ForbidBean> forbids = new ConcurrentHashMap<>();

    //白名单账号列表
    private final List<String> whites = new ArrayList<>();

    //渠道名列表
    private final List<String> platforms = new ArrayList<>();

    /**
     * 获取LoginVerify的实例对象
     *
     * @return
     */
    public static LoginVerify getInstance() {
        return Singleton.INSTANCE.getManager();
    }

    public static void verify(ChannelHandlerContext iosession, LoginMessage.ReqLogin messInfo) {

        logger.info("渠道账号:" + messInfo.getPlatformUid() + " 渠道：" + messInfo.getPlatformName() + " 开始验证。。。");
        deal().check(iosession, messInfo);
    }

    public static ILoginScript deal() {
        IScript is = ScriptManager.getInstance().GetScriptClass(1);
        if (is instanceof ILoginScript) {
            return (ILoginScript) is;
        } else {
            logger.info("渠道账号:没有找到执行的脚本处理！");
            return null;
        }
    }

    public ConcurrentHashMap<String, ForbidBean> getForbids() {
        return forbids;
    }

    public List<String> getWhites() {
        return whites;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    /**
     * 加载屏蔽账号数据和白名单数据
     */
    public void loadDatas() {
        ForbidDao dao = new ForbidDao();
        List<ForbidBean> list = dao.selectAll();
        for (ForbidBean bean : list) {
            forbids.put(bean.getStr(), bean);
        }
        WhiteDao whitedao = new WhiteDao();
        whites.addAll(whitedao.selectAll());
        platforms.addAll(whitedao.selectAllPlatforms());
    }

    public boolean isWhite(String str) {
        return whites.contains(str);
    }

    public boolean isForbiden(String str) {
        if (!forbids.containsKey(str)) {
            return false;
        }
        ForbidBean bean = forbids.get(str);
        if (bean.getTime() == -1) {
            return true;
        }
        return (int) (TimeUtils.Time() / 1000) < bean.getTime();
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;

        LoginVerify manager;

        Singleton() {
            this.manager = new LoginVerify();
        }

        LoginVerify getManager() {
            return manager;
        }
    }

}
