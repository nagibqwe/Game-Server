package com.gm.project.system.dict.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.common.utils.security.ShiroUtils;
import com.gm.common.utils.text.Convert;
import com.gm.project.system.dict.domain.DictData;
import com.gm.project.system.dict.mapper.DictDataMapper;
import com.gm.project.system.dict.utils.DictUtils;

/**
 * 字典 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class DictDataServiceImpl implements IDictDataService
{
    @Autowired
    private DictDataMapper dictDataMapper;

    /**
     * 根据条件分页查询Данные справочника
     * 
     * @param dictData Данные справочникаИнформация
     * @return Данные справочника集合Информация
     */
    @Override
    public List<DictData> selectDictDataList(DictData dictData)
    {
        return dictDataMapper.selectDictDataList(dictData);
    }

    /**
     * 根据Тип справочника和字典键值查询Данные справочникаИнформация
     * 
     * @param dictType Тип справочника
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue)
    {
        return dictDataMapper.selectDictLabel(dictType, dictValue);
    }

    /**
     * 根据Данные справочникаID查询Информация
     * 
     * @param dictCode Данные справочникаID
     * @return Данные справочника
     */
    @Override
    public DictData selectDictDataById(Long dictCode)
    {
        return dictDataMapper.selectDictDataById(dictCode);
    }

    /**
     * 批量УдалитьДанные справочника
     * 
     * @param ids 需要Удалить的Данные
     * @return Результат
     */
    @Override
    public int deleteDictDataByIds(String ids)
    {
        int row = dictDataMapper.deleteDictDataByIds(Convert.toStrArray(ids));
        if (row > 0)
        {
            DictUtils.clearDictCache();
        }
        return row;
    }

    /**
     * ДобавитьСохранитьДанные справочникаИнформация
     * 
     * @param dictData Данные справочникаИнформация
     * @return Результат
     */
    @Override
    public int insertDictData(DictData dictData)
    {
        dictData.setCreateBy(ShiroUtils.getLoginName());
        int row = dictDataMapper.insertDictData(dictData);
        if (row > 0)
        {
            DictUtils.clearDictCache();
        }
        return row;
    }

    /**
     * ИзменитьСохранитьДанные справочникаИнформация
     * 
     * @param dictData Данные справочникаИнформация
     * @return Результат
     */
    @Override
    public int updateDictData(DictData dictData)
    {
        dictData.setUpdateBy(ShiroUtils.getLoginName());
        int row = dictDataMapper.updateDictData(dictData);
        if (row > 0)
        {
            DictUtils.clearDictCache();
        }
        return row;
    }
}
