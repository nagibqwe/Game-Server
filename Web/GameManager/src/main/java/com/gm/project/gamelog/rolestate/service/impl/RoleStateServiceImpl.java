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
 * Журнал снимков персонажейService业务层处理
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
//     * 查询Журнал снимков персонажей
//     *
//     * @param roleId Журнал снимков персонажейID
//     * @return Журнал снимков персонажей
//     */
//    @Override
//    public RoleState selectRoleStateById(Long roleId)
//    {
//        return roleStateMapper.selectRoleStateById(roleId);
//    }

    /**
     * 查询Журнал снимков персонажей列表
     * 
     * @param roleState Журнал снимков персонажей
     * @return Журнал снимков персонажей
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
     * 查询Журнал снимков персонажей列表
     *
     * @param roleIds ID персонажа集
     * @return Журнал снимков персонажей
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
//     * ДобавитьЖурнал снимков персонажей
//     *
//     * @param roleState Журнал снимков персонажей
//     * @return Результат
//     */
//    @Override
//    public int insertRoleState(RoleState roleState)
//    {
//        roleState.setCreateTime(DateUtils.getNowDate());
//        return roleStateMapper.insertRoleState(roleState);
//    }
//
//    /**
//     * ИзменитьЖурнал снимков персонажей
//     *
//     * @param roleState Журнал снимков персонажей
//     * @return Результат
//     */
//    @Override
//    public int updateRoleState(RoleState roleState)
//    {
//        return roleStateMapper.updateRoleState(roleState);
//    }
//
//    /**
//     * УдалитьЖурнал снимков персонажей对象
//     *
//     * @param ids 需要Удалить的ДанныеID
//     * @return Результат
//     */
//    @Override
//    public int deleteRoleStateByIds(String ids)
//    {
//        return roleStateMapper.deleteRoleStateByIds(Convert.toStrArray(ids));
//    }
//
//    /**
//     * УдалитьЖурнал снимков персонажейИнформация
//     *
//     * @param roleId Журнал снимков персонажейID
//     * @return Результат
//     */
//    @Override
//    public int deleteRoleStateById(Long roleId)
//    {
//        return roleStateMapper.deleteRoleStateById(roleId);
//    }
}
