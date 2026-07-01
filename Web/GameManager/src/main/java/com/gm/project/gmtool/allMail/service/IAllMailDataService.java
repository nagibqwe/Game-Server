package com.gm.project.gmtool.allMail.service;

import java.util.List;
import com.gm.project.gmtool.allMail.domain.AllMailData;

/**
 * 全服邮件Service接口
 * 
 * @author gm
 * @date 2021-08-30
 */
public interface IAllMailDataService 
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
     * 批量删除全服邮件
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAllMailDataByIds(String ids);

    /**
     * 删除全服邮件信息
     * 
     * @param id 全服邮件ID
     * @return 结果
     */
    public int deleteAllMailDataById(Long id);

    /**
     * 待处理邮件列表
     * @return
     */
    public List<AllMailData> selectWaitDealMail();

    /**
     * 我的邮件列表
     * @param createUser
     * @param createDate
     * @return
     */
    public List<AllMailData> selectMineMail(String createUser,String createDate);

    /**
     * 历史邮件列表
     * @return
     */
    public List<AllMailData> selectHistoryMail();

    public List<AllMailData> selectMailByState();
}
