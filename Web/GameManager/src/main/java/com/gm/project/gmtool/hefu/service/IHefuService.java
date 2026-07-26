package com.gm.project.gmtool.hefu.service;

import java.util.List;
import java.util.Map;

import com.gm.project.gmtool.dbbak.domain.Dbbak;
import com.gm.project.gmtool.hefu.domain.Hefu;

/**
 * Объединение серверовService接口
 * 
 * @author gm
 * @date 2021-09-08
 */
public interface IHefuService 
{
    /**
     * 查询Объединение серверов
     * 
     * @param id Объединение серверовID
     * @return Объединение серверов
     */
    public Hefu selectHefuById(Long id);

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
     * 批量УдалитьОбъединение серверов
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteHefuByIds(String ids);

    /**
     * УдалитьОбъединение серверовИнформация
     * 
     * @param id Объединение серверовID
     * @return Результат
     */
    public int deleteHefuById(Long id);

    /**
     * 开始Объединение серверов
     * @param id
     * @return
     */
    public boolean start(Long id);

    /**
     * 停止Объединение серверов
     * @param id
     * @return
     */
    public boolean stop(Long id);

    /**
     * Объединение серверовЖурнал
     * @param id
     * @param index
     * @return
     */
    Map<String, Object> getLog(Long id, Integer index);

    /**
     * Резервные копии БД
     * @param id
     * @param type
     */
    void dbbak(Long id, Integer serverId, Integer type);

    /**
     * Данные备份列表
     * @param id
     */
    List<Dbbak> bakList(Long id);

    /**
     * Данные库还原
     * @param id
     * @param serverId
     * @param type
     */
    void dbrestore(Long id, Integer serverId, Integer type);

    /**
     * 检测配置
     * @param id
     * @return
     */
    boolean check(Long id) throws Exception;

    /**
     * 检测ДаНет有已合并的Сервер
     * @param id
     * @return
     */
    Map<Integer, Integer> checkIsHefu(Long id);

    /**
     * 查询Журнал记录
     * @param id
     * @return
     */
    List<String> logRecord(Long id);
}
