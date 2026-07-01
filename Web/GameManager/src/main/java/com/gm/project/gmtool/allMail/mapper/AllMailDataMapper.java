package com.gm.project.gmtool.allMail.mapper;

import java.util.List;
import java.util.Map;

import com.gm.project.gmtool.allMail.domain.AllMailData;

/**
 * 全服邮件Mapper接口
 * 
 * @author gm
 * @date 2021-08-30
 */
public interface AllMailDataMapper 
{
    /**
     * 查询全服邮件
     * 
     * @param id 全服邮件ID
     * @return 全服邮件
     */
    public AllMailData selectAllMailDataById(Long id);

    /**
     * 查询全服邮件列表
     * 
     * @param allMailData 全服邮件
     * @return 全服邮件集合
     */
    public List<AllMailData> selectAllMailDataList(AllMailData allMailData);

    /**
     * 新增全服邮件
     * 
     * @param allMailData 全服邮件
     * @return 结果
     */
    public int insertAllMailData(AllMailData allMailData);

    /**
     * 修改全服邮件
     * 
     * @param allMailData 全服邮件
     * @return 结果
     */
    public int updateAllMailData(AllMailData allMailData);

    /**
     * 删除全服邮件
     * 
     * @param id 全服邮件ID
     * @return 结果
     */
    public int deleteAllMailDataById(Long id);

    /**
     * 批量删除全服邮件
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAllMailDataByIds(String[] ids);


    /**
     * 待处理邮件列表
     * @return
     */
    public List<AllMailData> selectWaitDealMail();

    /**
     * 我的邮件列表
     * @param map
     * @return
     */
    public List<AllMailData> selectMineMail(Map map);

    /**
     * 历史邮件列表
     * @return
     */
    public List<AllMailData> selectHistoryMail();

    public List<AllMailData> selectMailByState();
}
