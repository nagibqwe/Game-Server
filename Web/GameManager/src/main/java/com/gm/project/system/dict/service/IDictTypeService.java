package com.gm.project.system.dict.service;

import java.util.List;
import com.gm.framework.web.domain.Ztree;
import com.gm.project.system.dict.domain.DictData;
import com.gm.project.system.dict.domain.DictType;

/**
 * 字典 业务层
 * 
 * @author ruoyi
 */
public interface IDictTypeService
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
     * 根据Тип справочника查询Данные справочника
     * 
     * @param dictType Тип справочника
     * @return Данные справочника集合Информация
     */
    public List<DictData> selectDictDataByType(String dictType);

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
     * 批量УдалитьТип справочника
     * 
     * @param ids 需要Удалить的Данные
     * @return Результат
     * @throws Exception 异常
     */
    public int deleteDictTypeByIds(String ids);

    /**
     * 清空缓存Данные
     */
    public void clearCache();

    /**
     * ДобавитьСохранитьТип справочникаИнформация
     * 
     * @param dictType Тип справочникаИнформация
     * @return Результат
     */
    public int insertDictType(DictType dictType);

    /**
     * ИзменитьСохранитьТип справочникаИнформация
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
    public String checkDictTypeUnique(DictType dictType);

    /**
     * 查询Тип справочника树
     * 
     * @param dictType Тип справочника
     * @return 所有Тип справочника
     */
    public List<Ztree> selectDictTree(DictType dictType);
}
