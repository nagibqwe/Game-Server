package com.game.utils;

import com.game.count.structs.Count;
import com.game.db.bean.ServerParamBean;
import com.game.db.dao.ServerParamDao;
import com.game.eightdiagrams.structs.EightDiageramSeverInfo;
import com.game.eightdiagrams.structs.EightDiagramCity;
import com.game.manager.Manager;
import com.game.server.MainServer;
import com.game.server.thread.SaveServerParamThread;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.servermatch.structs.GameServerInfo;
import com.game.servermatch.structs.TimeAxis;
import com.game.universe.struts.GuildBattleInfo;
import game.core.json.TypeReference;
import game.core.net.Config.ServerConfig;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import game.core.util.JsonUtils;
import game.core.util.VersionUpdateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 服务器系统参数缓存保存系统
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class ServerParamUtil {

    //运营的分组
    public static final String serverGroupStr = "SERVERGROUP";

    //public static final String timeAxisStr = "timeAxisStr";//时间保存key

    public static final String operatingGroup = "operatingGroup";//运营分组保存key

    public static final String saveServer = "GameServerInfo";//保存服务器key

    public static final String BRAVE_PEAK_MYSQL_SAVE_KEY = "BRAVE_PEAK_DATA";//勇者巅峰存放在ServerParm表中的key

    public static final String eightDiagramsCityInfo = "EightDiagramsCityInfo";//八级阵图分组信息

    public static final String eightDiagramGroupInfo = "EightDiagramGroupInfo";//八级阵图分组信息

    public static final String guildBattleInfo = "GuildBattleInfo";//公会战信息

    public static final String CountKey = "CountKey";   //计数器

    public static final String peakSeasonKey = "peakSeasonKey"; //巅峰竞技场赛季Key


    private static final Logger logger = LogManager.getLogger(ServerParamUtil.class);
    private static final ServerParamDao serverParamDao = new ServerParamDao();
    private static final ConcurrentHashMap<String, String> serverParamMap = new ConcurrentHashMap<>();
    //服务器分组列表服务器KEY_groupId组号
    private static final ConcurrentHashMap<String, Integer> serverGroup = new ConcurrentHashMap<>();
    //组ID 对应 这个组的 13个争夺城市
    public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, EightDiagramCity>> groupCityInfos = new ConcurrentHashMap<>();

    //组ID 对应 服务器
    public static ConcurrentHashMap<Integer, List<EightDiageramSeverInfo>> groupWithServer = new ConcurrentHashMap<>();

    //公会战信息 区服ID》排名，战绩
    public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, GuildBattleInfo>> guildBattleInfoMap = new ConcurrentHashMap<>();

    //巅峰竞技赛季
    public static int peakSeason;
    //计数器
    public static ConcurrentHashMap<String, Count> counts = new ConcurrentHashMap<>();

    public static ServerParamDao getServerParamDao() {
        return serverParamDao;
    }

    public static ConcurrentHashMap<String, String> getServerParamMap() {
        return serverParamMap;
    }

    public static String getValue(String key) {
        if (serverParamMap.containsKey(key)) {
            return serverParamMap.get(key);
        }
        return null;
    }

    /**
     * 线程队列存储
     *
     * @param paramKey
     * @param paramValue
     */
    public static void threadSave(String paramKey, String paramValue) {
        ServerParamBean serverParam = new ServerParamBean();
        serverParam.setParamkey(paramKey);
        serverParam.setServerid(ServerConfig.getServerId());
        serverParam.setParamvalue(paramValue);
        if (serverGroupStr.equals(serverParam.getParamkey())) {
            parasServerGroup(serverParam.getParamvalue());
        }
        if (serverParamMap.containsKey(paramKey)) {
            serverParamMap.put(paramKey, paramValue);
            MainServer.getInstance().getwSaveServerParamThread().dealServerParam(serverParam, SaveServerParamThread.ServerParam_UPDATE);
        } else {
            serverParamMap.put(paramKey, paramValue);
            MainServer.getInstance().getwSaveServerParamThread().dealServerParam(serverParam, SaveServerParamThread.ServerParam_INSERT);
        }
    }

    /**
     * 数据操作立即存储
     *
     * @param paramKey
     * @param paramValue
     * @return
     */
    public static int immediateSave(String paramKey, String paramValue) {
        ServerParamBean serverParam = new ServerParamBean();
        serverParam.setParamkey(paramKey);
        serverParam.setServerid(ServerConfig.getServerId());
        serverParam.setParamvalue(paramValue);
        if (serverGroupStr.equals(serverParam.getParamkey())) {
            parasServerGroup(serverParam.getParamvalue());
        }
        if (serverParamMap.containsKey(paramKey)) {
            serverParamMap.put(paramKey, paramValue);
            if (getServerParamDao().update(serverParam) == 0) {
                logger.info(String.format("serverParamMap保存update错误！paramKey = %s", paramKey));
                return 0;
            }
        } else {
            serverParamMap.put(paramKey, paramValue);
            if (getServerParamDao().insert(serverParam) == 0) {
                logger.info(String.format("serverParamMap保存insert错误！paramKey = %s", paramKey));
                return 0;
            }
        }

        return 1;
    }

    /**
     * 服务器开启时立即读取
     *
     * @return
     */
    public static boolean loadServerParam() {
        List<ServerParamBean> serverParams = getServerParamDao().selectAll();
        for (ServerParamBean serverParam : serverParams) {
            if (serverParam != null) {
                //如果数据库中因合服存了非本服的数据， 则先抛弃
                if (serverParam.getServerid() != ServerConfig.getServerId()) {
                    continue;
                }

                serverParamMap.put(serverParam.getParamkey(), serverParam.getParamvalue());
                if (serverGroupStr.equals(serverParam.getParamkey())) {
                    parasServerGroup(serverParam.getParamvalue());
                } else if (eightDiagramsCityInfo.equals(serverParam.getParamkey())) {
                    try {
                        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, EightDiagramCity>> groupCityInfos =
                                JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, EightDiagramCity>>>() {
                                });
                        ServerParamUtil.groupCityInfos.putAll(groupCityInfos);
                    } catch (Exception e) {
                        logger.error("八星阵图 城市信息 错误", e);
                    }
                } else if (eightDiagramGroupInfo.equals(serverParam.getParamkey())) {
                    try {
                        ConcurrentHashMap<Integer, List<EightDiageramSeverInfo>> groupServerInfos =
                                JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ConcurrentHashMap<Integer, List<EightDiageramSeverInfo>>>() {
                                });
                        ServerParamUtil.groupWithServer.putAll(groupServerInfos);
                    } catch (Exception e) {
                        logger.error("八星阵图 服务器组分配信息 错误", e);
                    }
                }
                else if (saveServer.equals(serverParam.getParamkey())) {
                    try {
                        HashMap<String, GameServerInfo> serverInfo =
                                JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<HashMap<String, GameServerInfo>>() {
                                });
                        ServerMatchManager.infos.putAll(serverInfo);
                    } catch (Exception e) {
                        logger.error("服务器列表", e);
                    }
                } else if (guildBattleInfo.equals(serverParam.getParamkey())) {
                    try {
                        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, GuildBattleInfo>> guildInfo =
                                JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()),
                                        new TypeReference<ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, GuildBattleInfo>>>() {
                                        });
                        ServerParamUtil.guildBattleInfoMap.putAll(guildInfo);
                    } catch (Exception e) {
                        logger.error("公会战信息 错误", e);
                    }
                } else if (peakSeasonKey.equals(serverParam.getParamkey())) {
                    peakSeason = Integer.parseInt(serverParam.getParamvalue());
                } else if (CountKey.equals(serverParam.getParamkey())) {
                    counts = JsonUtils.parseObject(serverParam.getParamvalue(), new TypeReference<ConcurrentHashMap<String, Count>>() {
                    });
                } else  if (operatingGroup.equals(serverParam.getParamkey())){
                    try {
                        HashMap<Integer,HashMap<Integer,List<String>>> gm_OperatingGroup =
                                JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<HashMap<Integer,HashMap<Integer,List<String>>>>() {
                                });
                        ServerMatchManager.gm_OperatingGroup.putAll(gm_OperatingGroup);
                    } catch (Exception e) {
                        logger.error("gm_setServerMatchinfos 错误", e);
                    }
                }
            }
        }
        logger.info("服务器参数加载成功！");
        return true;

    }

    public static void parasServerGroup(String paramvalue) {
        String[] str = paramvalue.split(";");
        for (String s : str) {
            String[] group = s.split("=");
            serverGroup.put(group[0], Integer.parseInt(group[1]));
        }
    }

    public static void saveEightCityInfo() {
        threadSave(eightDiagramsCityInfo, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(groupCityInfos)));
    }

    public static void savaEightGroupInfo(){
        threadSave(eightDiagramGroupInfo, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(groupWithServer)));
    }

    public static void savaGameServerInfo() {
        threadSave(saveServer, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(ServerMatchManager.infos)));
    }

    public static void saveOperatingGroup(){
        threadSave(operatingGroup, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(ServerMatchManager.gm_OperatingGroup)));
    }

    public static void saveGuildBattleInfo() {
        threadSave(guildBattleInfo, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(guildBattleInfoMap)));
    }

    public static void savePeakSeason() {
        threadSave(peakSeasonKey, String.valueOf(peakSeason));
    }

    public static void saveCounts() {
        threadSave(CountKey, JsonUtils.toJSONString(counts));
    }

}
