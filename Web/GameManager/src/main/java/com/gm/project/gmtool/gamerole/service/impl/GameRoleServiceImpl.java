package com.gm.project.gmtool.gamerole.service.impl;

import com.gm.common.utils.StringUtils;
import com.gm.common.utils.sql.SqlUtil;
import com.gm.framework.web.page.PageDomain;
import com.gm.framework.web.page.TableSupport;
import com.gm.project.common.utils.GameLogUtil;
import com.gm.project.gamelog.rolestate.domain.RoleState;
import com.gm.project.gmtool.gamerole.service.IGameRoleService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏角色信息日志Service业务层处理
 *
 * @author gm
 * @date 2021-09-07
 */
@Service
public class GameRoleServiceImpl implements IGameRoleService
{
//    @Autowired
//    private RoleStateMapper roleStateMapper;

    @Override
    public List<RoleState> queryByRoleName(Integer selectServerId, String roleName) {
        StringBuilder wheresql = new StringBuilder(" where 1 = 1");
        if(StringUtils.isEmpty(roleName)){
            return null;
        }
        wheresql.append(" and roleName like '%" + roleName+"%'");

        Map<String,Object> param = new HashMap<>();
        param.put("tableName","rolestate");
        param.put("where",wheresql);
        param.put("serverId",selectServerId);

        return GameLogUtil.getHeFuLogDataList(RoleState.class,param);
    }

    @Override
    public List<RoleState> queryByPlatFormAccount(Integer selectServerId, String pAccount) {
        StringBuilder wheresql = new StringBuilder(" where 1 = 1");
        if(StringUtils.isEmpty(pAccount)){
            return null;
        }
        wheresql.append(" and platUserId ='" + pAccount+"'");

        Map<String,Object> param = new HashMap<>();
        param.put("tableName","rolestate");
        param.put("where",wheresql);
        param.put("serverId",selectServerId);

        return GameLogUtil.getHeFuLogDataList(RoleState.class,param);
    }

    @Override
    public List<RoleState> queryByPlatFormUid(Integer selectServerId, String pUid) {
        StringBuilder wheresql = new StringBuilder(" where 1 = 1");
        if(StringUtils.isEmpty(pUid)){
            return null;
        }
        wheresql.append(" and funcellUUid ='" + pUid+"'");

        Map<String,Object> param = new HashMap<>();
        param.put("tableName","rolestate");
        param.put("where",wheresql);
        param.put("serverId",selectServerId);

        return GameLogUtil.getHeFuLogDataList(RoleState.class,param);
    }

    @Override
    public List<RoleState> queryByUserId(Integer selectServerId, String userId) {
        StringBuilder wheresql = new StringBuilder(" where 1 = 1");
        if(StringUtils.isEmpty(userId)){
            return null;
        }
        wheresql.append(" and userId =" + userId);

        Map<String,Object> param = new HashMap<>();
        param.put("tableName","rolestate");
        param.put("where",wheresql);
        param.put("serverId",selectServerId);

        return GameLogUtil.getHeFuLogDataList(RoleState.class,param);
    }

    @Override
    public List<RoleState> queryByRoleId(Integer selectServerId, String roleId, Integer roleIdType) {
        StringBuilder wheresql = new StringBuilder(" where 1 = 1");
        if(StringUtils.isEmpty(roleId)){
            return null;
        }
        long finalRoleId;
        try {
            finalRoleId = roleIdType == 1 ? toBase10(roleId) : Long.valueOf(roleId);
        } catch (Exception e) {
//            LogUtils.logError("输入查询参数错误", e);
            return null;
        }
        wheresql.append(" and roleId =" + finalRoleId);

        Map<String,Object> param = new HashMap<>();
        param.put("tableName","rolestate");
        param.put("where",wheresql);
        param.put("serverId",selectServerId);

        return GameLogUtil.getHeFuLogDataList(RoleState.class,param);
    }

    /**
     * 从36进制的字符串转换为10进制的long型
     */
    private long toBase10(String str) {
        return Long.parseLong(str, 36);
    }
}
