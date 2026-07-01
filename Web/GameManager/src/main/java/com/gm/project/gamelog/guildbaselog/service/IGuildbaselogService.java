package com.gm.project.gamelog.guildbaselog.service;

import java.util.List;
import java.util.Map;
import com.gm.project.gamelog.guildbaselog.domain.Guildbaselog;

/**
 * 公会基础信息Service接口
 * 
 * @author gm
 * @date 2021-12-06
 */
public interface IGuildbaselogService 
{

    /**
     * 查询公会基础信息列表
     * 
     * @param guildbaselog 公会基础信息
     * @return 公会基础信息集合
     */
    public List<Guildbaselog> selectGuildbaselogList(Guildbaselog guildbaselog, Map<String, Object> param);

}
