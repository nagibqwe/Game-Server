package com.gm.project.gmtool.allMail.mapper;

import java.util.List;
import java.util.Map;

import com.gm.project.gmtool.allMail.domain.AllMailData;

/**
 * Письмо всем серверамMapper接口
 * 
 * @author gm
 * @date 2021-08-30
 */
public interface AllMailDataMapper 
{
    /**
     * 查询Письмо всем серверам
     * 
     * @param id Письмо всем серверамID
     * @return Письмо всем серверам
     */
    public AllMailData selectAllMailDataById(Long id);

    /**
     * 查询全服Список писем
     * 
     * @param allMailData Письмо всем серверам
     * @return Письмо всем серверам集合
     */
    public List<AllMailData> selectAllMailDataList(AllMailData allMailData);

    /**
     * ДобавитьПисьмо всем серверам
     * 
     * @param allMailData Письмо всем серверам
     * @return Результат
     */
    public int insertAllMailData(AllMailData allMailData);

    /**
     * ИзменитьПисьмо всем серверам
     * 
     * @param allMailData Письмо всем серверам
     * @return Результат
     */
    public int updateAllMailData(AllMailData allMailData);

    /**
     * УдалитьПисьмо всем серверам
     * 
     * @param id Письмо всем серверамID
     * @return Результат
     */
    public int deleteAllMailDataById(Long id);

    /**
     * 批量УдалитьПисьмо всем серверам
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteAllMailDataByIds(String[] ids);


    /**
     * 待处理Список писем
     * @return
     */
    public List<AllMailData> selectWaitDealMail();

    /**
     * 我的Список писем
     * @param map
     * @return
     */
    public List<AllMailData> selectMineMail(Map map);

    /**
     * 历史Список писем
     * @return
     */
    public List<AllMailData> selectHistoryMail();

    public List<AllMailData> selectMailByState();
}
