package com.gm.project.stat.common.dao;

import com.gm.common.dbclient.DBClient;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IStatRoleStateDao {
    /**
     * 得到注册用户列表
     * @param channelNames
     * @param serverId
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Map<String, Object>> getUserRegisterDataList(String channelNames, String serverId, String startDate, String endDate);
    public Set<String> getUserIdRegAddSet(DBClient dbClientGM, String caclStartDay, String serverList);

    /**
     * 新增设备
     * @param channelNames
     * @param selectServerIds
     * @param blackUserStr
     * @param startDate
     * @param endDate
     * @param isBlack
     * @return
     */
    public List<Map<String, Object>> getNewDeviceDataList( String channelNames, String selectServerIds, String blackUserStr, String startDate, String endDate,boolean isBlack);

}
