package com.gm.project.gmtool.mail.service;

import java.util.List;
import com.gm.project.gmtool.mail.domain.MailData;

/**
 * Список писемService接口
 * 
 * @author gm
 * @date 2021-08-30
 */
public interface IMailDataService 
{
    /**
     * 查询Список писем
     * 
     * @param id Список писемID
     * @return Список писем
     */
    public MailData selectMailDataById(Long id);

    /**
     * 查询Список писем列表
     * 
     * @param mailData Список писем
     * @return Список писем集合
     */
    public List<MailData> selectMailDataList(MailData mailData);

    /**
     * ДобавитьСписок писем
     * 
     * @param mailData Список писем
     * @return Результат
     */
    public int insertMailData(MailData mailData);

    /**
     * ИзменитьСписок писем
     * 
     * @param mailData Список писем
     * @return Результат
     */
    public int updateMailData(MailData mailData);

    /**
     * 批量УдалитьСписок писем
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteMailDataByIds(String ids);

    /**
     * УдалитьСписок писемИнформация
     * 
     * @param id Список писемID
     * @return Результат
     */
    public int deleteMailDataById(Long id);

    /**
     * 待处理Список писем
     * @return
     */
    public List<MailData> selectWaitDealMail();

    /**
     * 我的Список писем
     * @param createUser
     * @param createDate
     * @return
     */
    public List<MailData> selectMineMail(String createUser,String createDate);

    /**
     * 历史Список писем
     * @return
     */
    public List<MailData> selectHistoryMail();

    public List<MailData> selectMailByState();
}
