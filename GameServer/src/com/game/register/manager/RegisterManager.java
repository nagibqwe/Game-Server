package com.game.register.manager;

import com.game.db.bean.ForbidBean;
import com.game.db.bean.roleBean;
import com.game.db.dao.roleDao;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.SessionAttribute;
import com.game.register.script.IRegisterScript;
import com.game.register.structs.UserInfo;
import com.game.register.structs.UserRoleInfo;
import com.game.script.structs.ScriptEnum;
import com.game.utils.Utils;
import game.core.script.IScript;
import game.core.util.SessionUtils;
import game.core.util.TimeUtils;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator
 */
public class RegisterManager {

    final Logger log = LogManager.getLogger(RegisterManager.class);
    //玩家roleId通信列表
    private final ConcurrentHashMap<Long, ChannelHandlerContext> role_sessions = new ConcurrentHashMap<>();
    //登录的玩家已经选择了角色
    private final ConcurrentHashMap<String, ChannelHandlerContext> userEnterRoleID = new ConcurrentHashMap<>();

    //玩家账号数据
    private final ConcurrentHashMap<Long, UserInfo> userInfos = new ConcurrentHashMap<>();

    public static String iconUpdateUrl = "";

    //已经使用的玩家名字
    private final ConcurrentHashMap<Long, String> hasUsedNameMap = new ConcurrentHashMap<>(); //<roleId, roleName>方式

    //玩家使用语言类型缓存
    private final ConcurrentHashMap<Long, Integer> languageMap = new ConcurrentHashMap<>();

    //屏蔽的账号数据，包括ip、Funcelluuid、mac地址、imei、机器唯一码
    private final ConcurrentHashMap<String, ForbidBean> forbids = new ConcurrentHashMap<>();

    //白名单账号列表
    private final List<String> whites = new ArrayList<>();

    private final roleDao dao = new roleDao();

    public roleDao getDao() {
        return dao;
    }

    public ConcurrentHashMap<String, ChannelHandlerContext> getUserEnterRoleID() {
        return userEnterRoleID;
    }

    public ConcurrentHashMap<Long, ChannelHandlerContext> getRole_sessions() {
        return role_sessions;
    }

    public ConcurrentHashMap<String, ForbidBean> getForbids() {
        return forbids;
    }

    public List<String> getWhites() {
        return whites;
    }

    public IRegisterScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.RegisterBaseScript);
        if (is instanceof IRegisterScript) {
            return (IRegisterScript) is;
        } else {
            return null;
        }
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        RegisterManager processor;

        Singleton() {
            this.processor = new RegisterManager();
        }

        RegisterManager getProcessor() {
            return processor;
        }
    }

    public static RegisterManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public UserInfo getUserInfoByUserId(long userId) {
        return userInfos.get(userId);
    }

    public UserInfo getUserInfo(long userId) {
        if (userInfos.containsKey(userId)) {
            return userInfos.get(userId);
        }
        List<UserRoleInfo> roles = dao.selectByUserId(userId);
        UserInfo info = new UserInfo();
        info.setUserId(userId);
        roles.forEach(role -> info.getRoles().put(role.getRoleId(), role));
        userInfos.put(userId, info);
        return info;
    }

    //判断是否有重名
    public boolean isUsedName(String name) {
        return Utils.isListContain(hasUsedNameMap.values(), name);
    }

    //创建角色成功后和角色改名后加入新名
    public void addRoleName(long roleId, String name) {
        hasUsedNameMap.put(roleId, name);
        //由于改表了排行榜中名字的获取方式，故需要在名字更改的时候做相应的变更
        Manager.rankListManager.deal().setName(roleId, name);
    }

    //创建角色成功后和角色改名后加入新名
    public void DelRoleName(long roleId) {
        hasUsedNameMap.remove(roleId);
    }

    //根据角色Id获取角色名
    public String getRoleName(long roleId) {
        if (hasUsedNameMap.containsKey(roleId)) {
            return hasUsedNameMap.get(roleId);
        }

        //缓存中没得则从数据库中获取看看
        String roleName = dao.selectNameById(roleId);
        if (roleName != null) {
            hasUsedNameMap.put(roleId, roleName);
            return roleName;
        }

        return "notfound"; //数据库中都取不到那就真没了
    }

    //根据角色Id获取其语言类型
    public int getLanguageType(long roleId) {
        if (languageMap.get(roleId) != null) {
            return languageMap.get(roleId);
        }

        return 0;
    }

    //创建新角色时加入其language
    public void addLanguageType(long roleId, int languageType) {
        languageMap.put(roleId, languageType);
    }

    //通过roleId获取Session
    public ChannelHandlerContext getSessionByRoleId(long roleId) {
        return role_sessions.get(roleId);
    }

    //获取所有玩家的socket
    public ConcurrentHashMap<Long, ChannelHandlerContext> getAllsessions() {
        return role_sessions;
    }

    /**
     * 加载所有使用的名字
     */
    public void loadAllUsedNames() {
        long start = TimeUtils.Time();
        log.info("开始加载名字列表");
        try {
            List<roleBean> roleList = dao.selectAllIdAndNames();
            for (roleBean role : roleList) {
                if (role.getRolename() == null) {
                    continue;
                }
                hasUsedNameMap.put(role.getRoleid(), role.getRolename());
                languageMap.put(role.getRoleid(), role.getLanguageType());
            }

        } catch (Exception e) {
            log.error(e, e);
        }
        long end = TimeUtils.Time();
        log.info("开始名字列表结束用时" + (end - start));
    }

    //角色等级提升后更新等级信息
    public void roleLvUp(Player player) {
        UserInfo user = getUserInfo(player.getUserId());
        UserRoleInfo userRoleInfo = user.getRoles().get(player.getId());
        userRoleInfo.setLevel(player.getLevel());
    }

}
