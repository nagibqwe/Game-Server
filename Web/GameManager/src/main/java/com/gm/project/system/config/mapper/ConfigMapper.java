package com.gm.project.system.config.mapper;

import com.gm.project.system.config.domain.Config;
import java.util.List;

/**
 * 参数配置 Данные层
 * 
 * @author ruoyi
 */
public interface ConfigMapper
{
    /**
     * 查询参数配置Информация
     * 
     * @param config 参数配置Информация
     * @return 参数配置Информация
     */
    public Config selectConfig(Config config);

    /**
     * 查询参数配置列表
     * 
     * @param config 参数配置Информация
     * @return 参数配置集合
     */
    public List<Config> selectConfigList(Config config);

    /**
     * 根据键名查询参数配置Информация
     * 
     * @param configKey 参数键名
     * @return 参数配置Информация
     */
    public Config checkConfigKeyUnique(String configKey);

    /**
     * Добавить参数配置
     * 
     * @param config 参数配置Информация
     * @return Результат
     */
    public int insertConfig(Config config);

    /**
     * Изменить参数配置
     * 
     * @param config 参数配置Информация
     * @return Результат
     */
    public int updateConfig(Config config);

    /**
     * 批量Удалить参数配置
     * 
     * @param configIds 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteConfigByIds(String[] configIds);
}