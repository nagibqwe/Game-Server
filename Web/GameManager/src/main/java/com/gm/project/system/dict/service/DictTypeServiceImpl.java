package com.gm.project.system.dict.service;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gm.common.constant.UserConstants;
import com.gm.common.exception.BusinessException;
import com.gm.common.utils.StringUtils;
import com.gm.common.utils.security.ShiroUtils;
import com.gm.common.utils.text.Convert;
import com.gm.framework.web.domain.Ztree;
import com.gm.project.system.dict.domain.DictData;
import com.gm.project.system.dict.domain.DictType;
import com.gm.project.system.dict.mapper.DictDataMapper;
import com.gm.project.system.dict.mapper.DictTypeMapper;
import com.gm.project.system.dict.utils.DictUtils;

/**
 * 字典 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class DictTypeServiceImpl implements IDictTypeService
{
    @Autowired
    private DictTypeMapper dictTypeMapper;

    @Autowired
    private DictDataMapper dictDataMapper;

    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init()
    {
        List<DictType> dictTypeList = dictTypeMapper.selectDictTypeAll();
        for (DictType dictType : dictTypeList)
        {
            List<DictData> dictDatas = dictDataMapper.selectDictDataByType(dictType.getDictType());
            DictUtils.setDictCache(dictType.getDictType(), dictDatas);
        }
    }

    /**
     * 根据条件分页查询Тип справочника
     * 
     * @param dictType Тип справочникаИнформация
     * @return Тип справочника集合Информация
     */
    @Override
    public List<DictType> selectDictTypeList(DictType dictType)
    {
        return dictTypeMapper.selectDictTypeList(dictType);
    }

    /**
     * 根据所有Тип справочника
     * 
     * @return Тип справочника集合Информация
     */
    @Override
    public List<DictType> selectDictTypeAll()
    {
        return dictTypeMapper.selectDictTypeAll();
    }

    /**
     * 根据Тип справочника查询Данные справочника
     * 
     * @param dictType Тип справочника
     * @return Данные справочника集合Информация
     */
    @Override
    public List<DictData> selectDictDataByType(String dictType)
    {
        List<DictData> dictDatas = DictUtils.getDictCache(dictType);
        if (StringUtils.isNotEmpty(dictDatas))
        {
            return dictDatas;
        }
        dictDatas = dictDataMapper.selectDictDataByType(dictType);
        if (StringUtils.isNotEmpty(dictDatas))
        {
            DictUtils.setDictCache(dictType, dictDatas);
            return dictDatas;
        }
        return null;
    }

    /**
     * 根据Тип справочникаID查询Информация
     * 
     * @param dictId Тип справочникаID
     * @return Тип справочника
     */
    @Override
    public DictType selectDictTypeById(Long dictId)
    {
        return dictTypeMapper.selectDictTypeById(dictId);
    }

    /**
     * 根据Тип справочника查询Информация
     * 
     * @param dictType Тип справочника
     * @return Тип справочника
     */
    @Override
    public DictType selectDictTypeByType(String dictType)
    {
        return dictTypeMapper.selectDictTypeByType(dictType);
    }

    /**
     * 批量УдалитьТип справочника
     * 
     * @param ids 需要Удалить的Данные
     * @return Результат
     */
    @Override
    public int deleteDictTypeByIds(String ids)
    {
        Long[] dictIds = Convert.toLongArray(ids);
        for (Long dictId : dictIds)
        {
            DictType dictType = selectDictTypeById(dictId);
            if (dictDataMapper.countDictDataByType(dictType.getDictType()) > 0)
            {
                throw new BusinessException(String.format("%1$s已分配,不能Удалить", dictType.getDictName()));
            }
        }
        int count = dictTypeMapper.deleteDictTypeByIds(dictIds);
        if (count > 0)
        {
            DictUtils.clearDictCache();
        }
        return count;
    }

    /**
     * 清空缓存Данные
     */
    @Override
    public void clearCache()
    {
        DictUtils.clearDictCache();
    }

    /**
     * ДобавитьСохранитьТип справочникаИнформация
     * 
     * @param dictType Тип справочникаИнформация
     * @return Результат
     */
    @Override
    public int insertDictType(DictType dictType)
    {
        dictType.setCreateBy(ShiroUtils.getLoginName());
        int row = dictTypeMapper.insertDictType(dictType);
        if (row > 0)
        {
            DictUtils.clearDictCache();
        }
        return row;
    }

    /**
     * ИзменитьСохранитьТип справочникаИнформация
     * 
     * @param dictType Тип справочникаИнформация
     * @return Результат
     */
    @Override
    @Transactional
    public int updateDictType(DictType dictType)
    {
        dictType.setUpdateBy(ShiroUtils.getLoginName());
        DictType oldDict = dictTypeMapper.selectDictTypeById(dictType.getDictId());
        dictDataMapper.updateDictDataType(oldDict.getDictType(), dictType.getDictType());
        int row = dictTypeMapper.updateDictType(dictType);
        if (row > 0)
        {
            DictUtils.clearDictCache();
        }
        return row;
    }

    /**
     * 校验Тип справочника称ДаНет唯一
     * 
     * @param dict Тип справочника
     * @return Результат
     */
    @Override
    public String checkDictTypeUnique(DictType dict)
    {
        Long dictId = StringUtils.isNull(dict.getDictId()) ? -1L : dict.getDictId();
        DictType dictType = dictTypeMapper.checkDictTypeUnique(dict.getDictType());
        if (StringUtils.isNotNull(dictType) && dictType.getDictId().longValue() != dictId.longValue())
        {
            return UserConstants.DICT_TYPE_NOT_UNIQUE;
        }
        return UserConstants.DICT_TYPE_UNIQUE;
    }

    /**
     * 查询Тип справочника树
     * 
     * @param dictType Тип справочника
     * @return 所有Тип справочника
     */
    @Override
    public List<Ztree> selectDictTree(DictType dictType)
    {
        List<Ztree> ztrees = new ArrayList<Ztree>();
        List<DictType> dictList = dictTypeMapper.selectDictTypeList(dictType);
        for (DictType dict : dictList)
        {
            if (UserConstants.DICT_NORMAL.equals(dict.getStatus()))
            {
                Ztree ztree = new Ztree();
                ztree.setId(dict.getDictId());
                ztree.setName(transDictName(dict));
                ztree.setTitle(dict.getDictType());
                ztrees.add(ztree);
            }
        }
        return ztrees;
    }

    public String transDictName(DictType dictType)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("(" + dictType.getDictName() + ")");
        sb.append("&nbsp;&nbsp;&nbsp;" + dictType.getDictType());
        return sb.toString();
    }
}
