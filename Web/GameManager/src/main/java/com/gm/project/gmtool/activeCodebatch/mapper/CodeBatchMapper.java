package com.gm.project.gmtool.activeCodebatch.mapper;

import java.util.List;
import com.gm.project.gmtool.activeCodebatch.domain.CodeBatch;

/**
 * Пакет кодов активацииMapper接口
 * 
 * @author gm
 * @date 2021-09-22
 */
public interface CodeBatchMapper 
{
    /**
     * 查询Пакет кодов активации
     * 
     * @param id Пакет кодов активацииID
     * @return Пакет кодов активации
     */
    public CodeBatch selectCodeBatchById(Long id);

    /**
     * 查询Пакет кодов активации列表
     * 
     * @param codeBatch Пакет кодов активации
     * @return Пакет кодов активации集合
     */
    public List<CodeBatch> selectCodeBatchList(CodeBatch codeBatch);

    /**
     * ДобавитьПакет кодов активации
     * 
     * @param codeBatch Пакет кодов активации
     * @return Результат
     */
    public int insertCodeBatch(CodeBatch codeBatch);

    /**
     * ИзменитьПакет кодов активации
     * 
     * @param codeBatch Пакет кодов активации
     * @return Результат
     */
    public int updateCodeBatch(CodeBatch codeBatch);

    /**
     * УдалитьПакет кодов активации
     * 
     * @param id Пакет кодов активацииID
     * @return Результат
     */
    public int deleteCodeBatchById(Long id);

    /**
     * 批量УдалитьПакет кодов активации
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteCodeBatchByIds(String[] ids);

    /**
     * 获取最大的id
     * @return
     */
    public int selectMaxId();

    public int selectBatchId(int batchId);
}
