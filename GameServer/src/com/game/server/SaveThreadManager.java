package com.game.server;

import com.game.chum.struct.ChumBeanExt;
import com.game.db.bean.*;
import com.game.db.dao.*;
import com.game.marriage.struct.MarryDeclaration;
import com.game.player.structs.PlayerWorldInfo;
import com.game.server.thread.SaveGoldThread;
import com.game.server.thread.SavePlayerThread;
import com.game.server.thread.SaveServer;

/**
 * @author lw
 * 管理所有保存线程
 */
public class SaveThreadManager {

    //玩家数据保存线程
    private SavePlayerThread savePlayerThread;

    //玩家元宝数据
    private SaveGoldThread wSaveGoldThread;

    private final SaveServer otherServerSave = new SaveServer(new ThreadGroup("otherServerSave"), "other savethread");

    public SaveGoldThread getSaveGoldThread() {
        return wSaveGoldThread;
    }

    public SavePlayerThread getSavePlayerThread() {
        return savePlayerThread;
    }

    public SaveServer getOtherServerSave() {
        return otherServerSave;
    }

    private enum Singleton {

        INSTANCE;                   //一个枚举的元素，它就代表了Singleton的一个实例
        SaveThreadManager manager;

        Singleton() {
            this.manager = new SaveThreadManager();
        }

        SaveThreadManager getProcessor() {
            return manager;
        }
    }

    //FriendManager
    public static SaveThreadManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    //初始化
    public void init() {
        savePlayerThread = new SavePlayerThread("Save-Player-Thread");
        wSaveGoldThread = new SaveGoldThread("Save-Gold-Thread");
        //以下是注册到同一线程里
        otherServerSave.register(PeakBean.class, new PeakDao());
        otherServerSave.register(Mail.class, new MailDao());
        otherServerSave.register(MarryBean.class, new MarryDao());
        otherServerSave.register(MarryDeclaration.class, new MarryDao());
        otherServerSave.register(ServerParamBean.class, new serverParamDao());
        otherServerSave.register(ActivityConfigBean.class, new ActivityConfigDao());
        otherServerSave.register(ActivityDataBean.class, new ActivityDataDao());
        otherServerSave.register(RoleActivityDataBean.class, new RoleActivityDataDao());
        otherServerSave.register(UserActivityBean.class, new UserActivityDao());
        otherServerSave.register(RankPlayer.class, new RankPlayerDao());
        otherServerSave.register(GuildBean.class, new GuildDao());
        otherServerSave.register(FriendBean.class, new FriendDao());
        otherServerSave.register(PlayerWorldInfo.class, new PlayerWorldInfoDao());
        otherServerSave.register(RoleLoginInfoBean.class, new RoleLoginInfoDao());
        otherServerSave.register(NewServerActivityBean.class, new NewServerDao());
        otherServerSave.register(redpacketBean.class, new redpacketDao());
        otherServerSave.register(DailyAccRechargeBean.class, new DailyAccRechargeDao());
        otherServerSave.register(BossDieRecordBean.class, new BossDieRecordDao());
        otherServerSave.register(AuctionBean.class, new AuctionDao());
        otherServerSave.register(ChumBeanExt.class, new ChumDao());
        otherServerSave.register(GuildMemberBean.class, new GuildMemberDao());
    }

    //开始
    public void start() {
        savePlayerThread.start();
        wSaveGoldThread.start();
        otherServerSave.start();
    }

    //结束
    public void stop() {
        otherServerSave.stop(true);
        savePlayerThread.stop(true);
        wSaveGoldThread.stop(true);
    }
}
