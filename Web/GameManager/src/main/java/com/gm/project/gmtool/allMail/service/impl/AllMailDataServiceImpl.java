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
 * 全服邮件Service业务层处理
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
     * 查询全服邮件
     * 
     * @param id 全服邮件ID
     * @return 全服邮件
     */
    @Override
    public AllMailData selectAllMailDataById(Long id)
    {
        return allMailDataMapper.selectAllMailDataById(id);
    }

    /**
     * 查询全服邮件列表
     * 
     * @param allMailData 全服邮件
     * @return 全服邮件
     */
    @Override
    public List<AllMailData> selectAllMailDataList(AllMailData allMailData)
    {
        return allMailDataMapper.selectAllMailDataList(allMailData);
    }

    /**
     * 新增全服邮件
     * 
     * @param allMailData 全服邮件
     * @return 结果
     */
    @Override
    public int insertAllMailData(AllMailData allMailData)
    {
        return allMailDataMapper.insertAllMailData(allMailData);
    }

    /**
     * 修改全服邮件
     * 
     * @param allMailData 全服邮件
     * @return 结果
     */
    @Override
    public int updateAllMailData(AllMailData allMailData)
    {
        return allMailDataMapper.updateAllMailData(allMailData);
    }

    /**
     * 删除全服邮件对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAllMailDataByIds(String ids)
    {
        return allMailDataMapper.deleteAllMailDataByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除全服邮件信息
     * 
     * @param id 全服邮件ID
     * @return 结果
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
