package com.game.recharge.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.recharge.structs.RechargeItemInfo;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 服务器充值下单
 * Created by cxl on 2021/4/27.
 */
public class RechargePlaceOrderHandler implements ICommand {
    private final Logger log = LogManager.getLogger("RechargeManager");

    Player player;
    RechargeItemInfo rechargeItemInfo;
    String moneyType;
    public RechargePlaceOrderHandler(Player player,RechargeItemInfo rechargeItemInfo,String moneyType){
        this.player = player;
        this.rechargeItemInfo = rechargeItemInfo;
        this.moneyType = moneyType;
    }


    @Override
    public void action() {
        try {
            Manager.rechargeManager.deal().onResRechargePlaceOrder(player,rechargeItemInfo,moneyType);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

}
