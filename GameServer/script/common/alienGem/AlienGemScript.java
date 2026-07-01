package common.alienGem;

import com.data.bean.Cfg_Cross_Alien_Gem_Copy_Bean;
import com.data.container.Cfg_Cross_Alien_Gem_Copy_Container;
import com.data.struct.ReadArray;
import com.game.alienGem.script.IAlienGemScript;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.map.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/11/15 17:13
 */
public class AlienGemScript implements IAlienGemScript {

    private Logger log = LogManager.getLogger(AlienGemScript.class);

    @Override
    public int getId() {
        return ScriptEnum.AlienGemScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void createMap(int id) {
        //得到类型对应的配置
        Cfg_Cross_Alien_Gem_Copy_Bean bean = null;
        for(Cfg_Cross_Alien_Gem_Copy_Bean b : Cfg_Cross_Alien_Gem_Copy_Container.GetInstance().getValuees()){
            if(b.getId() == id){
                bean = b;
            }
        }
        if(bean == null){
            log.info("须弥宝库配置不存在 id:" + id);
            return;
        }
        MapObject map = Manager.mapManager.createCopyMap(bean.getCloneMap(), 0, 0, bean.getId());
        Manager.alienGemManager.getMaps().put(id, map.getId());
    }

    @Override
    public void enterMap(Player player, int type) {
        Long mapId = Manager.alienGemManager.getMaps().get(type);
        if(mapId != null){
            MapObject map = Manager.mapManager.getMap(mapId);
            if(map != null){
                ReadArray<Integer> pos =  map.getSetting().getBornPosition().get(0);
                Manager.mapManager.changeMap(player, mapId, new Position(pos.get(0), pos.get(1)), false);
            }
        }
    }

    @Override
    public void close() {
        Manager.alienGemManager.getBossMap().clear();
        for(Long mapId : Manager.alienGemManager.getMaps().values()){
            MapObject map = Manager.mapManager.getMap(mapId);
            for(Player player : map.getPlayers().values()){
                Manager.copyMapManager.manager().onReqCopyMapOut(player);
            }
            map.setStop(true);
            map.setAutoRemove(true);

        }
        Manager.alienGemManager.getMaps().clear();
    }
}
