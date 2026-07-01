package com.gm.project.gamelog.rolestate.service.impl;

import java.util.List;
import java.util.Map;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.dbclient.TableType;
import com.gm.common.utils.DateUtils;
import com.gm.project.common.utils.GameLogUtil;
import com.gm.project.gamelog.itemchangelog.domain.Itemchangelog;
import org.apache.commons.dbutils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gamelog.rolestate.mapper.RoleStateMapper;
import com.gm.project.gamelog.rolestate.domain.RoleState;
import com.gm.project.gamelog.rolestate.service.IRoleStateService;
import com.gm.common.utils.text.Convert;

/**
 * 角色快照日志Service业务层处理
 * 
 * @author gm
 * @date 2021-09-07
 */
@Service
public class RoleStateServiceImpl implements IRoleStateService 
{
//    @Autowired
//    private RoleStateMapper roleStateMapper;

//    /**
//     * 查询角色快照日志
//     *
//     * @param roleId 角色快照日志ID
//     * @return 角色快照日志
//     */
//    @Override
//    public RoleState selectRoleStateById(Long roleId)
//    {
//        return roleStateMapper.selectRoleStateById(roleId);
//    }

    /**
     * 查询角色快照日志列表
     * 
     * @param roleState 角色快照日志
     * @return 角色快照日志
     */
    @Override
    public List<RoleState> selectRoleStateList(RoleState roleState, Map<String,Object> param)
    {
        StringBuilder wheresql = new StringBuilder(" where 1 = 1");
        //自定义查询条件
        if(roleState.getUserId() != null){
            wheresql.append(" and userId = " + roleState.getUserId());
        }
        if(roleState.getRoleId() != null){
            wheresql.append(" and roleId = " + roleState.getRoleId());
        }
        if(roleState.getRoleName() != null && !roleState.getRoleName().equals("")){
            wheresql.append(" and roleName like '%" + roleState.getRoleName()+"%'");
        }
        param.put("tableName","rolestate");
        param.put("where",wheresql);
        return GameLogUtil.getHeFuLogDataList( RoleState.class,param);
    }

    /**
     * 查询角色快照日志列表
     *
     * @param roleIds 角色ID集
     * @return 角色快照日志
     */
    @Override
    public List<RoleState> selectRoleStateList(Integer serverId, String roleIds)
    {
        String sqlStr = "SELECT * FROM rolestate rs WHERE rs.roleId in (" + roleIds+ ") AND rs.createsid = " + serverId;
        DBClient dbClient = DBServerMgr.getInstance().getLogDBClient(serverId);
        if (dbClient == null) {
            return null;
        }
        return dbClient.selectList(sqlStr, RoleState.class);
    }

//    /**
//     * 新增角色快照日志
//     *
//     * @param roleState 角色快照日志
//     * @return 结果
//     */
//    @Override
//    public int insertRoleState(RoleState roleState)
//    {
//        roleState.setCreateTime(DateUtils.getNowDate());
//        return roleStateMapper.insertRoleState(roleState);
//    }
//
//    /**
//     * 修改角色快照日志
//     *
//     * @param roleState 角色快照日志
//     * @return 结果
//     */
//    @Override
//    public int updateRoleState(RoleState roleState)
//    {
//        return roleStateMapper.updateRoleState(roleState);
//    }
//
//    /**
//     * 删除角色快照日志对象
//     *
//     * @param ids 需要删除的数据ID
//     * @return 结果
//     */
//    @Override
//    public int deleteRoleStateByIds(String ids)
//    {
//        return roleStateMapper.deleteRoleStateByIds(Convert.toStrArray(ids));
//    }
//
//    /**
//     * 删除角色快照日志信息
//     *
//     * @param roleId 角色快照日志ID
//     * @return 结果
//     */
//    @Override
//    public int deleteRoleStateById(Long roleId)
//    {
//        return roleStateMapper.deleteRoleStateById(roleId);
//    }
}
