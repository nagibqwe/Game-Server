package com.gm.project.gmtool.updateNotice.mapper;

import java.util.List;
import com.gm.project.gmtool.updateNotice.domain.UpdateNotice;

/**
 * Объявление об обновленииMapper接口
 * 
 * @author gm
 * @date 2021-10-30
 */
public interface UpdateNoticeMapper 
{
    /**
     * 查询Объявление об обновлении
     * 
     * @param id Объявление об обновленииID
     * @return Объявление об обновлении
     */
    public UpdateNotice selectUpdateNoticeById(Integer id);

    /**
     * 查询Объявление об обновлении列表
     * 
     * @param updateNotice Объявление об обновлении
     * @return Объявление об обновлении集合
     */
    public List<UpdateNotice> selectUpdateNoticeList(UpdateNotice updateNotice);

    /**
     * ДобавитьОбъявление об обновлении
     * 
     * @param updateNotice Объявление об обновлении
     * @return Результат
     */
    public int insertUpdateNotice(UpdateNotice updateNotice);

    /**
     * ИзменитьОбъявление об обновлении
     * 
     * @param updateNotice Объявление об обновлении
     * @return Результат
     */
    public int updateUpdateNotice(UpdateNotice updateNotice);

    /**
     * УдалитьОбъявление об обновлении
     * 
     * @param id Объявление об обновленииID
     * @return Результат
     */
    public int deleteUpdateNoticeById(Integer id);

    /**
     * 批量УдалитьОбъявление об обновлении
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteUpdateNoticeByIds(String[] ids);
}
