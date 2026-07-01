package com.game.store.script;

import com.game.player.structs.Player;
import game.message.storeMessage.ReqBagToStore;
import game.message.storeMessage.ReqStoreClearUp;
import game.message.storeMessage.ReqStoreMoveItem;
import game.message.storeMessage.ReqStoreToBag;

/**
 *
 * @author Administrator
 */
public interface IStoreScript {

    //请求入库
    public void OnReqBagToStore(Player player, ReqBagToStore messInfo);

    //仓库整理
    public void OnReqStoreClearUp(Player player, ReqStoreClearUp messInfo);

    //仓库移动
    public void OnReqStoreMoveItem(Player player, ReqStoreMoveItem messInfo);

    //请求出库
    public void OnReqStoreToBag(Player player, ReqStoreToBag messInfo);
}
