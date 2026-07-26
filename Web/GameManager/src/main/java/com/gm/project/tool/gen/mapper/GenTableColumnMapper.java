package com.gm.project.tool.gen.mapper;

import java.util.List;
import com.gm.project.tool.gen.domain.GenTableColumn;

/**
 * 业务字段 Данные层
 * 
 * @author ruoyi
 */
public interface GenTableColumnMapper
{
    /**
     * 根据Название таблицы查询列Информация
     * 
     * @param tableName Название таблицы
     * @return 列Информация
     */
    public List<GenTableColumn> selectDbTableColumnsByName(String tableName);

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
     * Удалить业务字段
     * 
     * @param genTableColumns 列Данные
     * @return Результат
     */
    public int deleteGenTableColumns(List<GenTableColumn> genTableColumns);

    /**
     * 批量Удалить业务字段
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteGenTableColumnByIds(Long[] ids);
}
