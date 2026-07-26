package com.gm.project.gmtool.allMail.service;

import java.util.List;
import com.gm.project.gmtool.allMail.domain.AllMailData;

/**
 * Письмо всем серверамService接口
 * 
 * @author gm
 * @date 2021-08-30
 */
public interface IAllMailDataService 
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
     * 批量УдалитьПисьмо всем серверам
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteAllMailDataByIds(String ids);

    /**
     * УдалитьПисьмо всем серверамИнформация
     * 
     * @param id Письмо всем серверамID
     * @return Результат
     */
    public int deleteAllMailDataById(Long id);

    /**
     * 待处理Список писем
     * @return
     */
    public List<AllMailData> selectWaitDealMail();

    /**
     * 我的Список писем
     * @param createUser
     * @param createDate
     * @return
     */
    public List<AllMailData> selectMineMail(String createUser,String createDate);

    /**
     * 历史Список писем
     * @return
     */
    public List<AllMailData> selectHistoryMail();

    public List<AllMailData> selectMailByState();
}
