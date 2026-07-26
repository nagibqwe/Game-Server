package com.gm.project.tool.gen.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.common.utils.text.Convert;
import com.gm.project.tool.gen.domain.GenTableColumn;
import com.gm.project.tool.gen.mapper.GenTableColumnMapper;

/**
 * 业务字段 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class GenTableColumnServiceImpl implements IGenTableColumnService 
{
	@Autowired
	private GenTableColumnMapper genTableColumnMapper;

	/**
     * 查询业务字段列表
     * 
     * @param genTableColumn 业务字段Информация
     * @return 业务字段集合
     */
	@Override
	public List<GenTableColumn> selectGenTableColumnListByTableId(GenTableColumn genTableColumn)
	{
	    return genTableColumnMapper.selectGenTableColumnListByTableId(genTableColumn);
	}
	
    /**
     * Добавить业务字段
     * 
     * @param genTableColumn 业务字段Информация
     * @return Результат
     */
	@Override
	public int insertGenTableColumn(GenTableColumn genTableColumn)
	{
	    return genTableColumnMapper.insertGenTableColumn(genTableColumn);
	}
	
	/**
     * Изменить业务字段
     * 
     * @param genTableColumn 业务字段Информация
     * @return Результат
     */
	@Override
	public int updateGenTableColumn(GenTableColumn genTableColumn)
	{
	    return genTableColumnMapper.updateGenTableColumn(genTableColumn);
	}

	/**
     * Удалить业务字段对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
	@Override
	public int deleteGenTableColumnByIds(String ids)
	{
		return genTableColumnMapper.deleteGenTableColumnByIds(Convert.toLongArray(ids));
	}
}