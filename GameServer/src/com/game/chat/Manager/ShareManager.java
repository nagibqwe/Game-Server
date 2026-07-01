package com.game.chat.Manager;

import com.data.CfgManager;
import com.data.bean.Cfg_Share_Bean;
import com.game.chat.script.IShareScript;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 分享功能管理类
 *
 * @author luosv
 * Created on 2018/6/7 0007.
 */
public class ShareManager {

    private static final Logger LOGGER = LogManager.getLogger(ShareManager.class);

    public static ShareManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 检查能否分享
     *
     * @param player  玩家
     * @param shareId 分享ID
     * @return bool
     */
    public boolean canShare(Player player, int shareId) {
        if (player == null || shareId < 1) {
            return false;
        }
        Cfg_Share_Bean bean = CfgManager.getCfg_Share_Container().getValueByKey(shareId);
        if (bean == null) {
            return false;
        }
        if (player.getShareMap().containsKey(shareId)) {
            if (bean.getCount() > 0) {
                return player.getShareMap().get(shareId) < bean.getCount();
            }
        }
        return true;
    }

    /**
     * 记录玩家分享
     *
     * @param player  玩家
     * @param shareId 分享ID
     */
    public void addShare(Player player, int shareId) {
        if (player == null || shareId < 1) {
            return;
        }
        Cfg_Share_Bean bean = CfgManager.getCfg_Share_Container().getValueByKey(shareId);
        if (bean == null) {
            return;
        }
        if (bean.getCount() < 0) {
            return;
        }
        if (!player.getShareMap().containsKey(shareId)) {
            player.getShareMap().put(shareId, 0);
        }
        player.getShareMap().put(shareId, player.getShareMap().get(shareId) + 1);
    }

    public IShareScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ShareBaseScript);
        if (is instanceof IShareScript) {
            return (IShareScript) is;
        } else {
            LOGGER.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        /**
         * 一个枚举元素就是一个实例
         */
        INSTANCE;

        ShareManager processor;

        Singleton() {
            this.processor = new ShareManager();
        }

        ShareManager getProcessor() {
            return processor;
        }
    }

}
