package com.gm.project.gmtool.mail.mapper;

import java.util.List;
import java.util.Map;

import com.gm.project.gmtool.mail.domain.MailData;

/**
 * Список писемMapper接口
 * 
 * @author gm
 * @date 2021-08-30
 */
public interface MailDataMapper 
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
     * УдалитьСписок писем
     * 
     * @param id Список писемID
     * @return Результат
     */
    public int deleteMailDataById(Long id);

    /**
     * 批量УдалитьСписок писем
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteMailDataByIds(String[] ids);

    /**
     * 待处理Список писем
     * @return
     */
    public List<MailData> selectWaitDealMail();

    /**
     * 我的Список писем
     * @param map
     * @return
     */
    public List<MailData> selectMineMail(Map map);

    /**
     * 历史Список писем
     * @return
     */
    public List<MailData> selectHistoryMail();

    public List<MailData> selectMailByState();
}
