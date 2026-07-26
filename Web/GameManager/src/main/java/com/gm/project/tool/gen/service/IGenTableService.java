package com.gm.project.tool.gen.service;

import java.util.List;
import java.util.Map;
import com.gm.project.tool.gen.domain.GenTable;

/**
 * 业务 服务层
 * 
 * @author ruoyi
 */
public interface IGenTableService
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
     * 查询业务Информация
     * 
     * @param id 业务ID
     * @return 业务Информация
     */
    public GenTable selectGenTableById(Long id);

    /**
     * Изменить业务
     * 
     * @param genTable 业务Информация
     * @return Результат
     */
    public void updateGenTable(GenTable genTable);

    /**
     * Удалить业务Информация
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public void deleteGenTableByIds(String ids);

    /**
     * Импорт表结构
     * 
     * @param tableList Импорт表列表
     */
    public void importGenTable(List<GenTable> tableList);

    /**
     * 预览代码
     * 
     * @param tableId 表Номер
     * @return 预览Данные列表
     */
    public Map<String, String> previewCode(Long tableId);

    /**
     * 生成代码（Скачать方式）
     * 
     * @param tableName Название таблицы
     * @return Данные
     */
    public byte[] downloadCode(String tableName);

    /**
     * 生成代码（自定义路径）
     * 
     * @param tableName Название таблицы
     */
    public void generatorCode(String tableName);
    
    /**
     * 同步Данные库
     * 
     * @param tableName Название таблицы
     */
    public void synchDb(String tableName);

    /**
     * 批量生成代码（Скачать方式）
     * 
     * @param tableNames 表数组
     * @return Данные
     */
    public byte[] downloadCode(String[] tableNames);

    /**
     * ИзменитьСохранить参数校验
     * 
     * @param genTable 业务Информация
     */
    public void validateEdit(GenTable genTable);
}
