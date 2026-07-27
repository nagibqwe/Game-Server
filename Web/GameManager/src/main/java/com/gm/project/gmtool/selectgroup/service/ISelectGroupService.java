package com.gm.project.gmtool.selectgroup.service;

import com.gm.project.gmtool.server.domain.TServer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 服务器列Service接口
 */
public interface ISelectGroupService
{
    public List<String> selectServerGroup();

    public List<TServer> selectServerList(String groupName, int ignoreMerge, String serverTypeList);

}
