package com.gm.project.tool.gen.mapper;

import java.util.List;
import com.gm.project.tool.gen.domain.GenTable;

/**
 * 业务 Данные层
 * 
 * @author ruoyi
 */
public interface GenTableMapper
{
    /**
     * 查询业务列表
     * 
     * @param genTable 业务Информация
     * @return 业务集合
     */
    public List<GenTable> selectGenTableList(GenTable genTable);

    /**
     * 查询据库列表
     * 
     * @param genTable 业务Информация
     * @return Данные库表集合
     */
    public List<GenTable> selectDbTableList(GenTable genTable);

    /**
     * 查询据库列表
     * 
     * @param tableNames Название таблицы组
     * @return Данные库表集合
     */
    public List<GenTable> selectDbTableListByNames(String[] tableNames);

    /**
     * 查询所有表Информация
     * 
     * @return 表Информация集合
     */
    public List<GenTable> selectGenTableAll();

    /**
     * 查询表ID业务Информация
     * 
     * @param id 业务ID
     * @return 业务Информация
     */
    public GenTable selectGenTableById(Long id);

    /**
     * 查询Название таблицы业务Информация
     * 
     * @param tableName Название таблицы
     * @return 业务Информация
     */
    public GenTable selectGenTableByName(String tableName);

    /**
     * Добавить业务
     * 
     * @param genTable 业务Информация
     * @return Результат
     */
    public int insertGenTable(GenTable genTable);

    /**
     * Изменить业务
     * 
     * @param genTable 业务Информация
     * @return Результат
     */
    public int updateGenTable(GenTable genTable);

    /**
     * 批量Удалить业务
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteGenTableByIds(Long[] ids);
}