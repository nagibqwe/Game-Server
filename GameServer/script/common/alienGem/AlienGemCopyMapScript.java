package common.alienGem;

import com.data.CfgManager;
import com.data.ItemChangeReason;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Cross_Alien_Gem_Boss_Bean;
import com.data.container.Cfg_Cross_Alien_Gem_Boss_Container;
import com.data.struct.ReadArray;
import com.game.alienGem.structs.PlayerDamage;
import com.game.backpack.structs.Item;
import com.game.boss.struct.Boss;
import com.game.boss.struct.BossTypeConst;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.monster.manager.MonsterManager;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.structs.GlobalType;
import com.game.utils.RandomUtils;
import game.core.map.Position;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 虚弥宝库
 * @Auther: gouzhongliang
 * @Date: 2021/11/15 15:16
 */
public class AlienGemCopyMapScript implements IMapBaseScript {

    final Logger logger = LogManager.getLogger(AlienGemCopyMapScript.class);

    @Override
    public int getId() {
        return ScriptEnum.AlienGemCopyMapScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(mapObject.getZoneModelId());
        if (bean == null) {
            logger.info("副本不存在:" + mapObject.getZoneModelId());
            return ;
        }
        int id = (int)objects[0];
        initAllMonster(mapObject, id);
    }

    private void initAllMonster(MapObject mapObject, int copyType){
        //开服时间
        int day = TimeUtils.getOpenServerDay();
        //世界等级
        int level = GlobalType.getWorldLevel();

        ConcurrentHashMap<Long, Boss> bossConcurrentHashMap = new ConcurrentHashMap<>();
        Manager.alienGemManager.getBossMap().put(mapObject.getId(), bossConcurrentHashMap);
        for(Cfg_Cross_Alien_Gem_Boss_Bean bean : Cfg_Cross_Alien_Gem_Boss_Container.GetInstance().getValuees()){
            if(bean.getConnectType() == copyType && bean.getDay().size() == 2 && bean.getDay().get(0) <= day && bean.getDay().get(1) >= day){
                if(bean.getLevel().size() == 2 && bean.getLevel().get(0) <= level && bean.getLevel().get(1) >= level){
                    Monster monster = MonsterManager.getInstance().createMonster(bean.getMonsterId());
                    if (monster != null) {
                        Position pos =  new Position(bean.getPos().get(0), bean.getPos().get(1));
                        monster.changeLine(mapObject.getLineId());
                        monster.changeMapId(mapObject.getId());
                        monster.changeMapModelId(mapObject.getMapModelId());
                        monster.setInitPos(pos);
                        monster.setCamp(mapObject.getMapModelId(),true);
                        Manager.mapManager.manager().onEnterMap(monster);

                        Boss boss =  bossConcurrentHashMap.get(bean.getMonsterId());
                        if (boss == null){
                            boss = new Boss();
                            bossConcurrentHashMap.put(monster.getId(),boss);
                        }
                        boss.setConfigId(bean.getId());
                        boss.setModelId(bean.getMonsterId());
                        boss.setMapID(mapObject.getZoneModelId());
                        boss.setId(mapObject.getId());
                        boss.setNextTime(0l);
                        boss.setPos(pos);

                        logger.info(" 须弥宝库 createMonster sucss  Camp {}  ",mapObject.getZoneModelId());
                    } else {
                        logger.error("须弥宝库Boss刷新怪物生成失败：monsterId=" + mapObject.getZoneModelId());
                    }
                }
            }
        }
    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return true;
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {
        Manager.bossManager.manager().sendBossInfo(player, Manager.alienGemManager.getBossMap().get(map.getId()).values(), map.getZoneModelId(), BossTypeConst.ALIENGEM_BOSS);
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {

    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {
        if (attacker instanceof Player) {

            Player player = (Player) attacker;
            Manager.worldHelpManager.getScript().sendResSynHarmRank(BossTypeConst.ALIENGEM_BOSS, player, monster);

            Manager.bossManager.manager().syncBossDamageRank(monster);
        }
    }

    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {
    }

    @Override
    public void onMonsterAfterDie(MapObject map, Monster monster, Fighter attacker) {

        ConcurrentHashMap<Long, Boss> bosses = Manager.alienGemManager.getBossMap().get(map.getId());
        Boss boss = bosses.get(monster.getId());
        if(boss == null){
            return;
        }

        //修改刷新时间
        boss.setNextTime(TimeUtils.Time() + 3600000);
        //击杀掉落
        sendAward(map, monster, boss);
        //boss信息
        Manager.bossManager.manager().sendBossInfo((Player)attacker, bosses.values(), map.getZoneModelId(), BossTypeConst.ALIENGEM_BOSS);

    }

    /**
     * 发奖
     * @param map
     * @param monster
     * @param boss
     */
    private void sendAward(MapObject map, Monster monster, Boss boss){
        Cfg_Cross_Alien_Gem_Boss_Bean bean = Cfg_Cross_Alien_Gem_Boss_Container.GetInstance().getValueByKey(boss.getConfigId());
        if(bean == null){
            logger.error("Cfg_Cross_Alien_Gem_Boss_Bean配置不存在" + boss.getConfigId());
            return;
        }
        //计算共享掉落包
        List<List<Integer>> drops = new ArrayList<>();
        for (int dropId : bean.getDrop().getValue()) {
            List<List<Integer>> temp = Manager.dropManager.deal().dropExecute(dropId);
            drops.addAll(temp);

        }
        List<Item> shareDrops = Item.createItems(drops, 1);

        //所有玩家
        List<Player> players = new ArrayList<>();
        for(Long rid : monster.getDamages().keySet()){
            Player player = map.getPlayer(rid);
            if(player != null){
                players.add(player);
            }
        }
        //掉落信息
        HashMap<Long, List<Item>> shareHash = new HashMap<>();

        //分配共享掉落
        //参与的玩家
        List<Player> shares = new ArrayList<>(players);

        for (Item item : shareDrops) {
            if (shares.isEmpty()) {
                shares.addAll(players);
            }
            int random = RandomUtils.random(0, shares.size() - 1);
            Player share = shares.remove(random);
            List<Item> list = shareHash.getOrDefault(share.getId(), new ArrayList<>());
            list.add(item);
            shareHash.put(share.getId(), list);
        }

        //排名奖励--排序
        List<PlayerDamage> sorters = new ArrayList<>();
        for(Player p : players){
            Long score = monster.getDamages().get(p.getId());
            sorters.add(new PlayerDamage(p, score == null ? 0 : score));
        }
        Collections.sort(sorters, new Comparator<PlayerDamage>() {
            @Override
            public int compare(PlayerDamage o1, PlayerDamage o2) {
                if(o1.getDamage() > o2.getDamage()){
                    return 1;
                }else if(o1.getDamage() < o2.getDamage()){
                    return -1;
                }
                return 0;
            }
        });

        //分配特殊掉落
        for(ReadArray<Integer> arr : bean.getSpecialDrop().getValuees()){
            if(arr.size() < 3){
                continue;
            }
            int start = arr.get(0);
            int end = arr.get(1);
            int dropId = arr.get(2);
            for(; start <= end; start++){
                if(sorters.size() >= start){
                    Player p = sorters.get(start - 1).getPlayer();
                    List<List<Integer>> temp = Manager.dropManager.deal().dropExecute(dropId);
                    List<Item> dropItems = Item.createItems(temp, 1);
                    List<Item> its = shareHash.getOrDefault((p.getId()), new ArrayList<>());
                    its.addAll(dropItems);
                }
            }
        }

        //发送奖励
        for(Player p : players){
            List<Item> dropItems = shareHash.get(p.getId());
            Manager.backpackManager.manager().addOrSendItems(p, dropItems, ItemChangeReason.AlienGemGet, IDConfigUtil.getLogId());
        }
    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    @Override
    public void onPlayerDie(MapObject map, Fighter attacker, Player player) {

    }

    @Override
    public void action(MapObject map, String method, Object[] params) {

    }

    @Override
    public void removeMap(MapObject map) {
        Manager.alienGemManager.getBossMap().remove(map.getId());
    }
}
