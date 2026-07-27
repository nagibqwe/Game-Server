package com.gm.project.gmtool.mail.mapper;

import java.util.List;
import java.util.Map;

import com.gm.project.gmtool.mail.domain.MailData;

/**
 * 邮件列表Mapper接口
 * 
 * @author gm
 * @date 2021-08-30
 */
public interface MailDataMapper 
{
    /**
     * 查询邮件列表
     * 
     * @param id 邮件列表ID
     * @return 邮件列表
     */
    public MailData selectMailDataById(Long id);

    /**
     * 查询邮件列表列表
     * 
     * @param mailData 邮件列表
     * @return 邮件列表集合
     */
    public List<MailData> selectMailDataList(MailData mailData);

    /**
     * 新增邮件列表
     * 
     * @param mailData 邮件列表
     * @return 结果
     */
    public int insertMailData(MailData mailData);

    /**
     * 修改邮件列表
     * 
     * @param mailData 邮件列表
     * @return 结果
     */
    public int updateMailData(MailData mailData);

    /**
     * 删除邮件列表
     * 
     * @param id 邮件列表ID
     * @return 结果
     */
    public int deleteMailDataById(Long id);

    /**
     * 批量删除邮件列表
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteMailDataByIds(String[] ids);

    /**
     * 待处理邮件列表
     * @return
     */
    public List<MailData> selectWaitDealMail();

    /**
     * 我的邮件列表
     * @param map
     * @return
     */
    public List<MailData> selectMineMail(Map map);

    /**
     * 历史邮件列表
     * @return
     */
    public List<MailData> selectHistoryMail();

    public List<MailData> selectMailByState();
}
