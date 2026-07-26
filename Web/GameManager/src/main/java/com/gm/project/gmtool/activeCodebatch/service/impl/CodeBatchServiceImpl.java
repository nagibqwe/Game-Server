package com.gm.project.gmtool.activeCodebatch.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.activeCodebatch.mapper.CodeBatchMapper;
import com.gm.project.gmtool.activeCodebatch.domain.CodeBatch;
import com.gm.project.gmtool.activeCodebatch.service.ICodeBatchService;
import com.gm.common.utils.text.Convert;

/**
 * Пакет кодов активацииService业务层处理
 * 
 * @author gm
 * @date 2021-09-22
 */
@Service
public class CodeBatchServiceImpl implements ICodeBatchService 
{
    @Autowired
    private CodeBatchMapper codeBatchMapper;

    /**
     * 查询Пакет кодов активации
     * 
     * @param id Пакет кодов активацииID
     * @return Пакет кодов активации
     */
    @Override
    public CodeBatch selectCodeBatchById(Long id)
    {
        return codeBatchMapper.selectCodeBatchById(id);
    }

    /**
     * 查询Пакет кодов активации列表
     * 
     * @param codeBatch Пакет кодов активации
     * @return Пакет кодов активации
     */
    @Override
    public List<CodeBatch> selectCodeBatchList(CodeBatch codeBatch)
    {
        return codeBatchMapper.selectCodeBatchList(codeBatch);
    }

    /**
     * ДобавитьПакет кодов активации
     * 
     * @param codeBatch Пакет кодов активации
     * @return Результат
     */
    @Override
    public int insertCodeBatch(CodeBatch codeBatch)
    {
        return codeBatchMapper.insertCodeBatch(codeBatch);
    }

    /**
     * ИзменитьПакет кодов активации
     * 
     * @param codeBatch Пакет кодов активации
     * @return Результат
     */
    @Override
    public int updateCodeBatch(CodeBatch codeBatch)
    {
        return codeBatchMapper.updateCodeBatch(codeBatch);
    }

    /**
     * УдалитьПакет кодов активации对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteCodeBatchByIds(String ids)
    {
        return codeBatchMapper.deleteCodeBatchByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьПакет кодов активацииИнформация
     * 
     * @param id Пакет кодов активацииID
     * @return Результат
     */
    @Override
    public int deleteCodeBatchById(Long id)
    {
        return codeBatchMapper.deleteCodeBatchById(id);
    }

    /**
     * 获取最大的id
     * @return
     */
    @Override
    public int selectMaxId(){
        return codeBatchMapper.selectMaxId();
    }

    @Override
    public int selectBatchId(int batchId) {
        return codeBatchMapper.selectBatchId(batchId);
    }
}
