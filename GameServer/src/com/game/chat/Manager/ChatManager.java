package com.game.chat.Manager;

import com.game.chat.script.IChatScript;
import com.game.chat.structs.*;
import com.game.db.bean.ChatBlackListBean;
import com.game.db.bean.ChatWordBean;
import com.game.db.bean.ForbidChatBean;
import com.game.db.bean.ForbidWordBean;
import com.game.db.dao.ChatBlackListDao;
import com.game.db.dao.ChatWordDao;
import com.game.db.dao.ForbidChatDao;
import com.game.db.dao.ForbidWordDao;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatManager {


    public static final byte CHATTYPE_WORD = 0;//文字
    public static final byte CHATTYPE_VOICE = 1;//语音

    public static final int CHATWORD_MAX = 1024;
    public static final int CHATVOICE_MAX = 100 * 1024;

    public static final byte ROLE_NAME = 100;                    //创建角色/改名，用于4399监控频道定义
    public static final byte GUILD_NAME = 101;                   //公会创建、改名，用于4399监控频道定义
    public static final byte GUILD_NOTICE = 102;                 //公会公告，用于4399监控频道定义
    public static final String[] CHATCHANNEL_DESC = {"世界", "帮会", "队伍", "私聊",
            "系统", "小聊天框", "王府", "综合", "当前", "组队", "直播", "好友", "历练", "跨服"};


    public static final String[] OTHERCHANNEL_DESC = {"角色创建/改名", "帮会创建/改名", "帮会公告"};

    public static final int CHATTYPE_SYSTEM_GuildSendId = 1;    //系统公会发送id

    private final ForbidChatDao chatDao = new ForbidChatDao();
    private final ForbidWordDao wordDao = new ForbidWordDao();
    private final ChatBlackListDao chatBlackListDao = new ChatBlackListDao();
    private final ChatWordDao chatWordDao = new ChatWordDao();

    //前一次发送的信息内容
    private final ConcurrentHashMap<Long, String> lastMsgContentMap = new ConcurrentHashMap<>();
    //相同信息累计次数
    private final ConcurrentHashMap<Long, Integer> sameMsgContentAddUpMap = new ConcurrentHashMap<>();
    //世界/跨服频道聊天消息缓存
    private final ConcurrentHashMap<ChatChannel, List<LeaveMsg>> worldHistoryMsgCacheList = new ConcurrentHashMap<>();
    //仙盟频道聊天消息
    private final ConcurrentHashMap<Long, List<LeaveMsg>> guildHistoryMsgCacheList = new ConcurrentHashMap<>();
    //留言消息
    private final ConcurrentHashMap<Long, HashMap<Long, List<LeaveMsg>>> leaveMsgList = new ConcurrentHashMap<>();
    //多媒体数据 语音、图片等
    private final ConcurrentHashMap<Long, MultiMedia> multiMediaList = new ConcurrentHashMap<>();
    //禁言数据
    private final ConcurrentHashMap<Long, ForbidChatBean> forbids = new ConcurrentHashMap<>();
    //禁言关键字
    private final ConcurrentHashMap<Integer, ForbidWordBean> forbidWords = new ConcurrentHashMap<>();
    //禁言黑名单
    private final ConcurrentHashMap<Long, ChatBlackListBean> chatBlackList = new ConcurrentHashMap<>();
    //禁言替换关键字
    private final ConcurrentHashMap<Integer, ChatWordBean> chatWords = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Long, MediaChatData> mediaChatList = new ConcurrentHashMap<>();

    public ForbidChatDao getChatDao() {
        return chatDao;
    }

    public ForbidWordDao getWordDao() {
        return wordDao;
    }

    public ChatBlackListDao getChatBlackListDao() {
        return chatBlackListDao;
    }

    public ChatWordDao getChatWordDao() {
        return chatWordDao;
    }

    public ConcurrentHashMap<Long, ForbidChatBean> getForbids() {
        return forbids;
    }

    public ConcurrentHashMap<ChatChannel, List<LeaveMsg>> getWorldHistoryMsgCacheList() {
        return worldHistoryMsgCacheList;
    }

    public ConcurrentHashMap<Long, List<LeaveMsg>> getGuildHistoryMsgCacheList() {
        return guildHistoryMsgCacheList;
    }

    public ConcurrentHashMap<Long, String> getLastMsgContentMap() {
        return lastMsgContentMap;
    }

    public ConcurrentHashMap<Long, MultiMedia> getMultiMediaList() {
        return multiMediaList;
    }

    public ConcurrentHashMap<Long, Integer> getSameMsgContentAddUpMap() {
        return sameMsgContentAddUpMap;
    }

    public ConcurrentHashMap<Long, HashMap<Long, List<LeaveMsg>>> getLeaveMsgList() {
        return leaveMsgList;
    }

    public ConcurrentHashMap<Integer, ForbidWordBean> getForbidWords() {
        return forbidWords;
    }

    public ConcurrentHashMap<Long, ChatBlackListBean> getChatBlackList() {
        return chatBlackList;
    }

    public ConcurrentHashMap<Integer, ChatWordBean> getChatWords() {
        return chatWords;
    }

    public ConcurrentHashMap<Long, MediaChatData> getMediaChatList() {
        return mediaChatList;
    }

    public final static Object mediaObj = new Object();

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        ChatManager manager;

        Singleton() {
            this.manager = new ChatManager();
        }

        ChatManager getProcessor() {
            return manager;
        }
    }

    public static ChatManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public void loadForbidChatAll() {
        loadForbidChat();
        loadForbidWord();
        loadChatBlackList();
        loadChatWord();
    }

    /**
     * 加载禁言数据
     */
    public void loadForbidChat() {
        forbids.clear();
        List<ForbidChatBean> chatList = chatDao.selectAll();
        if (chatList != null) {
            for (ForbidChatBean bean : chatList) {
                forbids.put(bean.getUserId(), bean);
            }
        }
    }

    public void loadForbidWord() {
        forbidWords.clear();
        List<ForbidWordBean> wordList = wordDao.selectAll();
        if (wordList != null) {
            for (ForbidWordBean bean : wordList) {
                forbidWords.put(bean.getId(), bean);
            }
        }
    }

    public void loadChatBlackList() {
        this.chatBlackList.clear();
        List<ChatBlackListBean> chatBlackList = chatBlackListDao.selectAllByServerIds(ServerConfig.getServerIdList());
        if (chatBlackList != null) {
            for (ChatBlackListBean bean : chatBlackList) {
                this.chatBlackList.put(bean.getUserId(), bean);
            }
        }
    }

    public void loadChatWord() {
        this.chatWords.clear();
        List<ChatWordBean> chatWords = chatWordDao.selectAllByServerIds(ServerConfig.getServerIdList());
        if (chatWords != null) {
            for (ChatWordBean bean : chatWords) {
                this.chatWords.put(bean.getId(), bean);
            }
        }
    }

    public IChatScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ChatBaseScript);
        if (is instanceof IChatScript) {
            return (IChatScript) is;
        }
        return null;
    }
}
