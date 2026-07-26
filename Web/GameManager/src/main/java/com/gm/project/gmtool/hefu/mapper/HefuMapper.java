package com.gm.project.gmtool.hefu.mapper;

import java.util.List;
import com.gm.project.gmtool.hefu.domain.Hefu;

/**
 * Объединение серверовMapper接口
 * 
 * @author gm
 * @date 2021-09-08
 */
public interface HefuMapper 
{
    /**
     * 查询Объединение серверов
     * 
     * @param id Объединение серверовID
     * @return Объединение серверов
     */
    public Hefu selectHefuById(Long id);

    /**
     * 查询Объединение серверов详细Информация
     *
     * @param id Объединение серверовID
     * @return Объединение серверов
     */
    public Hefu selectHefuRecord(Long id);

    /**
     * 查询Список объединения
     * 
     * @param hefu Объединение серверов
     * @return Объединение серверов集合
     */
    public List<Hefu> selectHefuList(Hefu hefu);

    /**
     * ДобавитьОбъединение серверов
     * 
     * @param hefu Объединение серверов
     * @return Результат
     */
    public int insertHefu(Hefu hefu);

    /**
     * ИзменитьОбъединение серверов
     * 
     * @param hefu Объединение серверов
     * @return Результат
     */
    public int updateHefu(Hefu hefu);

    /**
     * УдалитьОбъединение серверов
     * 
     * @param id Объединение серверовID
     * @return Результат
     */
    public int deleteHefuById(Long id);

    /**
     * 批量УдалитьОбъединение серверов
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteHefuByIds(String[] ids);
}
