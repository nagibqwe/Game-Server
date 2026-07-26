package com.gm.project.system.config.service;

import com.gm.project.system.config.domain.Config;
import java.util.List;

/**
 * 参数配置 服务层
 * 
 * @author ruoyi
 */
public interface IConfigService
{
    /**
     * 查询参数配置Информация
     * 
     * @param configId 参数配置ID
     * @return 参数配置Информация
     */
    public Config selectConfigById(Long configId);

    /**
     * 根据键名查询参数配置Информация
     * 
     * @param configKey 参数键名
     * @return 参数键值
     */
    public String selectConfigByKey(String configKey);

    /**
     * 查询参数配置列表
     * 
     * @param config 参数配置Информация
     * @return 参数配置集合
     */
    public List<Config> selectConfigList(Config config);

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
     * 批量Удалить参数配置Информация
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteConfigByIds(String ids);

    /**
     * 清空缓存Данные
     */
    public void clearCache();

    /**
     * 校验参数键名ДаНет唯一
     * 
     * @param config 参数Информация
     * @return Результат
     */
    public String checkConfigKeyUnique(Config config);
}
