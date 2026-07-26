package com.gm.project.gmtool.allMail.service.impl;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.allMail.mapper.AllMailDataMapper;
import com.gm.project.gmtool.allMail.domain.AllMailData;
import com.gm.project.gmtool.allMail.service.IAllMailDataService;
import com.gm.common.utils.text.Convert;

/**
 * Письмо всем серверамService业务层处理
 * 
 * @author gm
 * @date 2021-08-30
 */
@Service
public class AllMailDataServiceImpl implements IAllMailDataService 
{
    @Autowired
    private AllMailDataMapper allMailDataMapper;

    /**
     * 查询Письмо всем серверам
     * 
     * @param id Письмо всем серверамID
     * @return Письмо всем серверам
     */
    @Override
    public AllMailData selectAllMailDataById(Long id)
    {
        return allMailDataMapper.selectAllMailDataById(id);
    }

    /**
     * 查询全服Список писем
     * 
     * @param allMailData Письмо всем серверам
     * @return Письмо всем серверам
     */
    @Override
    public List<AllMailData> selectAllMailDataList(AllMailData allMailData)
    {
        return allMailDataMapper.selectAllMailDataList(allMailData);
    }

    /**
     * ДобавитьПисьмо всем серверам
     * 
     * @param allMailData Письмо всем серверам
     * @return Результат
     */
    @Override
    public int insertAllMailData(AllMailData allMailData)
    {
        return allMailDataMapper.insertAllMailData(allMailData);
    }

    /**
     * ИзменитьПисьмо всем серверам
     * 
     * @param allMailData Письмо всем серверам
     * @return Результат
     */
    @Override
    public int updateAllMailData(AllMailData allMailData)
    {
        return allMailDataMapper.updateAllMailData(allMailData);
    }

    /**
     * УдалитьПисьмо всем серверам对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteAllMailDataByIds(String ids)
    {
        return allMailDataMapper.deleteAllMailDataByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьПисьмо всем серверамИнформация
     * 
     * @param id Письмо всем серверамID
     * @return Результат
     */
    @Override
    public int deleteAllMailDataById(Long id)
    {
        return allMailDataMapper.deleteAllMailDataById(id);
    }

    @Override
    public List<AllMailData> selectWaitDealMail() {
        return allMailDataMapper.selectWaitDealMail();
    }

    @Override
    public List<AllMailData> selectMineMail(String createUser, String createDate) {
        HashMap map = new HashMap();
        map.put("createUser",createUser);
        map.put("createDate",createDate);
        return allMailDataMapper.selectMineMail(map);
    }

    @Override
    public List<AllMailData> selectHistoryMail() {
        return allMailDataMapper.selectHistoryMail();
    }

    @Override
    public List<AllMailData> selectMailByState() {
        return allMailDataMapper.selectMailByState();
    }
}
