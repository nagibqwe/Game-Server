package com.gm.project.gmtool.updateNotice.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.updateNotice.mapper.UpdateNoticeMapper;
import com.gm.project.gmtool.updateNotice.domain.UpdateNotice;
import com.gm.project.gmtool.updateNotice.service.IUpdateNoticeService;
import com.gm.common.utils.text.Convert;

/**
 * Объявление об обновленииService业务层处理
 * 
 * @author gm
 * @date 2021-10-30
 */
@Service
public class UpdateNoticeServiceImpl implements IUpdateNoticeService 
{
    @Autowired
    private UpdateNoticeMapper updateNoticeMapper;

    /**
     * 查询Объявление об обновлении
     * 
     * @param id Объявление об обновленииID
     * @return Объявление об обновлении
     */
    @Override
    public UpdateNotice selectUpdateNoticeById(Integer id)
    {
        return updateNoticeMapper.selectUpdateNoticeById(id);
    }

    /**
     * 查询Объявление об обновлении列表
     * 
     * @param updateNotice Объявление об обновлении
     * @return Объявление об обновлении
     */
    @Override
    public List<UpdateNotice> selectUpdateNoticeList(UpdateNotice updateNotice)
    {
        return updateNoticeMapper.selectUpdateNoticeList(updateNotice);
    }

    /**
     * ДобавитьОбъявление об обновлении
     * 
     * @param updateNotice Объявление об обновлении
     * @return Результат
     */
    @Override
    public int insertUpdateNotice(UpdateNotice updateNotice)
    {
        return updateNoticeMapper.insertUpdateNotice(updateNotice);
    }

    /**
     * ИзменитьОбъявление об обновлении
     * 
     * @param updateNotice Объявление об обновлении
     * @return Результат
     */
    @Override
    public int updateUpdateNotice(UpdateNotice updateNotice)
    {
        return updateNoticeMapper.updateUpdateNotice(updateNotice);
    }

    /**
     * УдалитьОбъявление об обновлении对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteUpdateNoticeByIds(String ids)
    {
        return updateNoticeMapper.deleteUpdateNoticeByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьОбъявление об обновленииИнформация
     * 
     * @param id Объявление об обновленииID
     * @return Результат
     */
    @Override
    public int deleteUpdateNoticeById(Integer id)
    {
        return updateNoticeMapper.deleteUpdateNoticeById(id);
    }
}
