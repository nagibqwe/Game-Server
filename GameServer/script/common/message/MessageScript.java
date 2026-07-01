package common.message;

import com.data.CfgManager;
import com.data.bean.Cfg_Mapsetting_Bean;
import com.game.map.structs.Area;
import com.game.map.structs.MapObject;
import com.game.fightserver.manager.FightClientManager;
import com.game.server.GameServer;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.map.IMapObject;
import com.game.utils.MessageInterFace;
import com.game.utils.MessageUtils;
import game.core.message.SMessage;
import game.core.script.IScript;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author admin
 */
public class MessageScript implements IScript, MessageInterFace {

    private static final Logger log = LogManager.getLogger(MessageScript.class);

    @Override
    public int getId() {
        return ScriptEnum.MessageBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    //获取分级关系
    @Override
    public void OnCalcFilterR(Player player) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        List<Player> players = getRoundPlayer(map, player);
        player.getUnfilters().clear();
        filterAdd(map, player, players);

    }

    //周围全体广播
    @Override
    public void OnBroadcastRound2(IMapObject point, SMessage message) {

        if (point instanceof Player) {
            MessageUtils.send_to_player((Player) point, message);
        }

        MapObject map = Manager.mapManager.getMap(point.gainMapId());
        if (map == null) {
//            log.error("发广播的时候， 物件已经不存在了！id" + point.getId() + "point:" + point.toString());//调整日志输出
            return;
        }
        //如果是跨服休息室，不需要广播
        if (map.getMapModelId() == MapManager.CrossWaitMapId) {
            return;
        }

        List<Player> players = getRoundPlayer(map, point);
        //发给玩家自己的不受广播影响
        if (point instanceof Player) {
            players.remove( point);
        }
        //TODO如果是过滤型地图
        if (OnNeedFilter(point, map, message)) {
            OnFilterBroadcastRound(map, point, message, players);
        } else {
            OnBroadcastRound(message, players, map.getId());
        }
    }

    //是否带上我的全体广播
    @Override
    public void OnBroadcastRound3(IMapObject point, SMessage message, boolean includeMe) {

        MapObject map = Manager.mapManager.getMap(point.gainMapId());
        if (map == null) {
            return;
        }

        //如果是跨服休息室，不需要广播
        if (map.getMapModelId() == MapManager.CrossWaitMapId) {
            if (includeMe) {
                if (point instanceof Player) {
                    MessageUtils.send_to_player((Player) point, message);
                }
            }
            return;
        }

        List<Player> players = getRoundPlayer(map, point);
        if (point instanceof Player) {
            if (includeMe) {
                if (!players.contains((Player) point)) {
                    players.add((Player) point);
                }
            } else {
                players.remove((Player) point);
            }
        }

        if (players.isEmpty()) {
            return;
        }

        //TODO如果是过滤型地图
        if (OnNeedFilter(point, map, message)) {
//            long start = TimeUtils.Time();
            //LOGGER.error(point.getId() + "分级发送start：" + start);
            OnFilterBroadcastRound(map, point, message, players);
//            LOGGER.error(point.getId() + "分级发送useTime：" + (TimeUtils.Time() - start));

        } else {
            //其它物品的出现
            if (!(point instanceof Player)) {
                Iterator<Player> iter = players.iterator();
                while (iter.hasNext()) {
                    Player pp = (Player) iter.next();
                    if (!point.canSee(pp)) {
                        iter.remove();
                    }
                }
            }
            OnBroadcastRound(message, players, map.getId());
        }
    }

    //广播
    private void OnBroadcastRound(SMessage message, List<Player> players, long fromid) {

        //跨服
        if (GameServer.getInstance().IsFightServer()) {
            FightClientManager.GetInstance().send_to_players(players, message.getId(), message.getData(), fromid);
            return;
        }

        for (Player player : players) {
            MessageUtils.send_to_player(player, message);
        }

    }
//
//    //广播个数暂定
//    public int FilterBroadcastCountMax = 5;

    //过滤型广播
    public void OnFilterBroadcastRound(MapObject map, IMapObject sender, SMessage message, List<Player> players) {
        if (!(sender instanceof Player)) {
            //需要检查是否是可见的问题
            Iterator<Player> iter = players.iterator();
            while (iter.hasNext()) {
                Player pp = (Player) iter.next();
                if (!sender.canSee(pp)) {
                    iter.remove();
                }
            }
            OnBroadcastRound(message, players, map.getId());
        } else {
            Player player = (Player) sender;

            filterDec(map, player, players);

            if (player.getUnfilters().size() < map.getFilterNum()) {
                filterAdd(map, player, players);
            }

            //跨服
            if (GameServer.getInstance().IsFightServer()) {
                for (Iterator<Player> it = players.iterator(); it.hasNext();) {
                    Player target = it.next();
                    if (target.getId() == player.getId()) {
                        continue;
                    }

                    if (!(target.getUnfilters().contains(player.getId()))) {
                        it.remove();
                    }
                }
                FightClientManager.GetInstance().send_to_players(players, message.getId(), message.getData(), map.getId());
                return;
            }

            int i = 0;
            for (Player target : players) {

                if (target.getId() == player.getId()) {
                    MessageUtils.send_to_player(target, message);
                    continue;
                }

                if (target.getUnfilters().contains(player.getId())) {
                    i++;
                    MessageUtils.send_to_player(target, message);
                }
            }
//            if (player.getId() == 1151329080189598993L) {
//                LOGGER.error(player.getName() + player.getUnfilters().size() + "发送个数：" + i);
//            }

        }
    }

    //检测脱离关系
    private void filterDec(MapObject map, Player sender, List<Player> rounds) {
        Iterator<Long> iter = sender.getUnfilters().iterator();
        while (iter.hasNext()) {
            Long roleId = iter.next();
            Player target = map.getPlayer(roleId);
            if (target == null) {
                iter.remove();
                continue;
            }
            //解除关系
            if (!rounds.contains(target)) {
                iter.remove();
                target.getUnfilters().remove(sender.getId());
//                if (sender.getId() == 1151329080189598993L) {
//                    LOGGER.error("解除关系 red:" + sender.getName() + sender.getUnfilters().size() + " blue:" + target.getName() + sender.getUnfilters().size());
//                }
            }

        }
    }

    //筛选规则
    private void filterAdd(MapObject map, Player sender, List<Player> rounds) {
        //发送优先级 队伍1 > 仇恨关系2 > 其他阵营3 > 同一阵营4
        List<Player> teams = new ArrayList<>();
        List<Player> hatreds = new ArrayList<>();
        List<Player> otherCamps = new ArrayList<>();
        List<Player> camps = new ArrayList<>();

        for (Player member : rounds) {
            switch (getRelation(sender, member)) {
                case 1:
                    teams.add(member);
                    break;
                case 2:
                    hatreds.add(member);
                    break;
                case 3:
                    otherCamps.add(member);
                    break;
                case 4:
                    camps.add(member);
                    break;
                default:
                    break;
            }
        }

        for (Player member : teams) {
            r2r(sender, member);
        }
        for (Player member : hatreds) {
            r2r(sender, member);
        }
        for (Player member : otherCamps) {
            if (sender.getUnfilters().size() >= map.getFilterNum()) {
                return;
            }
            if (member.getUnfilters().size() >= map.getFilterNum() * 3) {
                continue;
            }
            r2r(sender, member);
        }
        for (Player member : camps) {
            if (sender.getUnfilters().size() >= map.getFilterNum()) {
                return;
            }
            if (member.getUnfilters().size() >= map.getFilterNum() * 3) {
                continue;
            }
            r2r(sender, member);
        }

    }

    //发送优先级 队伍1 > 仇恨关系2 > 其他阵营3 > 其他
    private int getRelation(Player red, Player blue) {
        if (red.getTeamId() != 0 && blue.getTeamId() != 0 && red.getTeamId() == blue.getTeamId()) {
            return 1;
        }
        if (red.getEnemys().containsKey(blue.getId())) {
            return 2;
        }
        if (red.getCamp() == blue.getCamp()) {
            return 4;
        } else {
            return 3;
        }
    }

    //建立关系
    private void r2r(Player red, Player blue) {
        if (!red.getUnfilters().contains(blue.getId())) {
            red.getUnfilters().add(blue.getId());
        }
        if (!blue.getUnfilters().contains(red.getId())) {
            blue.getUnfilters().add(red.getId());
        }
    }

    //是否需要过滤
    private boolean OnNeedFilter(IMapObject point, MapObject map, SMessage message) {

        if (!(point instanceof Player)) {
            return false;
        }
        //是否是分级发送地图
        Cfg_Mapsetting_Bean config = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
        if (config == null || config.getFilter() != 1) {
            return false;
        }
        //是否是要分级发送的消息
//        LOGGER.info("hello world id:" + message.getId());
        switch (message.getId()) {

            case 102103: //把周围对象广播给主角
                return true;
            case 102104: //把玩家广播给周围玩家
                return true;
            case 102106: //停止移动
                return true;
            case 102110: //移动
                return true;
            case 102111: //跳跃
                return true;
            case 102116: //方向移动
                return true;
            case 103102: //广播使用技能
                return true;
            case 103103: //广播攻击结果
                return true;
            case 103104: //广播死亡消息
                return true;
            case 103106: //广播吟唱技能
                return true;
            case 103110: //血量改变
                return true;
            case 103111: //战斗状态
                return true;
            default:
                return false;
        }
    }

    //获取周围所有玩家
    private List<Player> getRoundPlayer(MapObject map, IMapObject point) {

        List<Area> areas = Manager.mapManager.getRounds(map, point.gainCurPos());
        List<Player> players = new ArrayList<>();
        for (Area area : areas) {
            players.addAll(area.getPlayers());
        }
        return players;
    }

}
