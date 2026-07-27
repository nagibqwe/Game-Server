package com.gm.project.gmtool.mail.service.impl;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.mail.mapper.MailDataMapper;
import com.gm.project.gmtool.mail.domain.MailData;
import com.gm.project.gmtool.mail.service.IMailDataService;
import com.gm.common.utils.text.Convert;

/**
 * 邮件列表Service业务层处理
 * 
 * @author gm
 * @date 2021-08-30
 */
@Service
public class MailDataServiceImpl implements IMailDataService 
{
    @Autowired
    private MailDataMapper mailDataMapper;

    /**
     * 查询邮件列表
     * 
     * @param id 邮件列表ID
     * @return 邮件列表
     */
    @Override
    public MailData selectMailDataById(Long id)
    {
        return mailDataMapper.selectMailDataById(id);
    }

    /**
     * 查询邮件列表列表
     * 
     * @param mailData 邮件列表
     * @return 邮件列表
     */
    @Override
    public List<MailData> selectMailDataList(MailData mailData)
    {
        return mailDataMapper.selectMailDataList(mailData);
    }

    /**
     * 新增邮件列表
     * 
     * @param mailData 邮件列表
     * @return 结果
     */
    @Override
    public int insertMailData(MailData mailData)
    {
        return mailDataMapper.insertMailData(mailData);
    }

    /**
     * 修改邮件列表
     * 
     * @param mailData 邮件列表
     * @return 结果
     */
    @Override
    public int updateMailData(MailData mailData)
    {
        return mailDataMapper.updateMailData(mailData);
    }

    /**
     * 删除邮件列表对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteMailDataByIds(String ids)
    {
        return mailDataMapper.deleteMailDataByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除邮件列表信息
     * 
     * @param id 邮件列表ID
     * @return 结果
     */
    @Override
    public int deleteMailDataById(Long id)
    {
        return mailDataMapper.deleteMailDataById(id);
    }

    @Override
    public List<MailData> selectWaitDealMail() {
        return mailDataMapper.selectWaitDealMail();
    }

    @Override
    public List<MailData> selectMineMail(String createUser, String createDate) {
        HashMap map = new HashMap();
        map.put("createUser",createUser);
        map.put("createDate",createDate);
        return mailDataMapper.selectMineMail(map);
    }

    @Override
    public List<MailData> selectHistoryMail() {
        return mailDataMapper.selectHistoryMail();
    }

    @Override
    public List<MailData> selectMailByState() {
        return mailDataMapper.selectMailByState();
    }
}
