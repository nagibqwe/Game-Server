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
 * Список писемService业务层处理
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
     * 查询Список писем
     * 
     * @param id Список писемID
     * @return Список писем
     */
    @Override
    public MailData selectMailDataById(Long id)
    {
        return mailDataMapper.selectMailDataById(id);
    }

    /**
     * 查询Список писем列表
     * 
     * @param mailData Список писем
     * @return Список писем
     */
    @Override
    public List<MailData> selectMailDataList(MailData mailData)
    {
        return mailDataMapper.selectMailDataList(mailData);
    }

    /**
     * ДобавитьСписок писем
     * 
     * @param mailData Список писем
     * @return Результат
     */
    @Override
    public int insertMailData(MailData mailData)
    {
        return mailDataMapper.insertMailData(mailData);
    }

    /**
     * ИзменитьСписок писем
     * 
     * @param mailData Список писем
     * @return Результат
     */
    @Override
    public int updateMailData(MailData mailData)
    {
        return mailDataMapper.updateMailData(mailData);
    }

    /**
     * УдалитьСписок писем对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteMailDataByIds(String ids)
    {
        return mailDataMapper.deleteMailDataByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьСписок писемИнформация
     * 
     * @param id Список писемID
     * @return Результат
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
