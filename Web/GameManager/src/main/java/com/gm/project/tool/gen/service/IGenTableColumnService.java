package com.gm.project.tool.gen.service;

import java.util.List;
import com.gm.project.tool.gen.domain.GenTableColumn;

/**
 * 业务字段 服务层
 * 
 * @author ruoyi
 */
public interface IGenTableColumnService
{
    /**
     * 查询业务字段列表
     * 
     * @param genTableColumn 业务字段Информация
     * @return 业务字段集合
     */
    public List<GenTableColumn> selectGenTableColumnListByTableId(GenTableColumn genTableColumn);

    /**
     * Добавить业务字段
     * 
     * @param genTableColumn 业务字段Информация
     * @return Результат
     */
    public int insertGenTableColumn(GenTableColumn genTableColumn);

    /**
     * Изменить业务字段
     * 
     * @param genTableColumn 业务字段Информация
     * @return Результат
     */
    public int updateGenTableColumn(GenTableColumn genTableColumn);

    /**
     * Удалить业务字段Информация
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteGenTableColumnByIds(String ids);
}
