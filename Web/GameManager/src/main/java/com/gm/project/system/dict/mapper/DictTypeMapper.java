package com.gm.project.system.dict.mapper;

import java.util.List;
import com.gm.project.system.dict.domain.DictType;

/**
 * 字典表 Данные层
 * 
 * @author ruoyi
 */
public interface DictTypeMapper
{
    /**
     * 根据条件分页查询Тип справочника
     * 
     * @param dictType Тип справочникаИнформация
     * @return Тип справочника集合Информация
     */
    public List<DictType> selectDictTypeList(DictType dictType);

    /**
     * 根据所有Тип справочника
     * 
     * @return Тип справочника集合Информация
     */
    public List<DictType> selectDictTypeAll();

    /**
     * 根据Тип справочникаID查询Информация
     * 
     * @param dictId Тип справочникаID
     * @return Тип справочника
     */
    public DictType selectDictTypeById(Long dictId);

    /**
     * 根据Тип справочника查询Информация
     * 
     * @param dictType Тип справочника
     * @return Тип справочника
     */
    public DictType selectDictTypeByType(String dictType);

    /**
     * 通过字典IDУдалить字典Информация
     * 
     * @param dictId 字典ID
     * @return Результат
     */
    public int deleteDictTypeById(Long dictId);

    /**
     * 批量УдалитьТип справочника
     * 
     * @param ids 需要Удалить的Данные
     * @return Результат
     */
    public int deleteDictTypeByIds(Long[] ids);

    /**
     * ДобавитьТип справочникаИнформация
     * 
     * @param dictType Тип справочникаИнформация
     * @return Результат
     */
    public int insertDictType(DictType dictType);

    /**
     * ИзменитьТип справочникаИнформация
     * 
     * @param dictType Тип справочникаИнформация
     * @return Результат
     */
    public int updateDictType(DictType dictType);

    /**
     * 校验Тип справочника称ДаНет唯一
     * 
     * @param dictType Тип справочника
     * @return Результат
     */
    public DictType checkDictTypeUnique(String dictType);
}
