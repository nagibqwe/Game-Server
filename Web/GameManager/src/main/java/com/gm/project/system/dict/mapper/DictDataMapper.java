package com.gm.project.system.dict.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.gm.project.system.dict.domain.DictData;

/**
 * 字典表 Данные层
 * 
 * @author ruoyi
 */
public interface DictDataMapper
{
    /**
     * 根据条件分页查询Данные справочника
     * 
     * @param dictData Данные справочникаИнформация
     * @return Данные справочника集合Информация
     */
    public List<DictData> selectDictDataList(DictData dictData);

    /**
     * 根据Тип справочника查询Данные справочника
     * 
     * @param dictType Тип справочника
     * @return Данные справочника集合Информация
     */
    public List<DictData> selectDictDataByType(String dictType);

    /**
     * 根据Тип справочника和字典键值查询Данные справочникаИнформация
     * 
     * @param dictType Тип справочника
     * @param dictValue 字典键值
     * @return 字典标签
     */
    public String selectDictLabel(@Param("dictType") String dictType, @Param("dictValue") String dictValue);

    /**
     * 根据Данные справочникаID查询Информация
     * 
     * @param dictCode Данные справочникаID
     * @return Данные справочника
     */
    public DictData selectDictDataById(Long dictCode);

    /**
     * 查询Данные справочника
     * 
     * @param dictType Тип справочника
     * @return Данные справочника
     */
    public int countDictDataByType(String dictType);

    /**
     * 通过字典IDУдалитьДанные справочникаИнформация
     * 
     * @param dictCode Данные справочникаID
     * @return Результат
     */
    public int deleteDictDataById(Long dictCode);

    /**
     * 批量УдалитьДанные справочника
     * 
     * @param ids 需要Удалить的Данные
     * @return Результат
     */
    public int deleteDictDataByIds(String[] ids);

    /**
     * ДобавитьДанные справочникаИнформация
     * 
     * @param dictData Данные справочникаИнформация
     * @return Результат
     */
    public int insertDictData(DictData dictData);

    /**
     * ИзменитьДанные справочникаИнформация
     * 
     * @param dictData Данные справочникаИнформация
     * @return Результат
     */
    public int updateDictData(DictData dictData);

    /**
     * 同步ИзменитьТип справочника
     * 
     * @param oldDictType 旧Тип справочника
     * @param newDictType 新旧Тип справочника
     * @return Результат
     */
    public int updateDictDataType(@Param("oldDictType") String oldDictType, @Param("newDictType") String newDictType);
}
