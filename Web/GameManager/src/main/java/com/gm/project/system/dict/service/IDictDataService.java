package com.gm.project.system.dict.service;

import java.util.List;
import com.gm.project.system.dict.domain.DictData;

/**
 * 字典 业务层
 * 
 * @author ruoyi
 */
public interface IDictDataService
{
    /**
     * 根据条件分页查询Данные справочника
     * 
     * @param dictData Данные справочникаИнформация
     * @return Данные справочника集合Информация
     */
    public List<DictData> selectDictDataList(DictData dictData);

    /**
     * 根据Тип справочника和字典键值查询Данные справочникаИнформация
     * 
     * @param dictType Тип справочника
     * @param dictValue 字典键值
     * @return 字典标签
     */
    public String selectDictLabel(String dictType, String dictValue);

    /**
     * 根据Данные справочникаID查询Информация
     * 
     * @param dictCode Данные справочникаID
     * @return Данные справочника
     */
    public DictData selectDictDataById(Long dictCode);

    /**
     * 批量УдалитьДанные справочника
     * 
     * @param ids 需要Удалить的Данные
     * @return Результат
     */
    public int deleteDictDataByIds(String ids);

    /**
     * ДобавитьСохранитьДанные справочникаИнформация
     * 
     * @param dictData Данные справочникаИнформация
     * @return Результат
     */
    public int insertDictData(DictData dictData);

    /**
     * ИзменитьСохранитьДанные справочникаИнформация
     * 
     * @param dictData Данные справочникаИнформация
     * @return Результат
     */
    public int updateDictData(DictData dictData);
}
