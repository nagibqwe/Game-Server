package com.game.ranklist.handler;

import com.game.manager.Manager;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 删除角色的排行榜数据更新
 */
public class DeleteRoleForRankListHandler implements ICommand {

    private static final Logger log = LogManager.getLogger(DeleteRoleForRankListHandler.class);

    private final long roleId;

    public DeleteRoleForRankListHandler(long roleId) {
        this.roleId = roleId;
    }

    @Override
    public void action() {
        try {
            Manager.rankListManager.deal().deleteRankRole(roleId);
        } catch (Exception ex) {
            log.error(ex, ex);
        }
    }

}
