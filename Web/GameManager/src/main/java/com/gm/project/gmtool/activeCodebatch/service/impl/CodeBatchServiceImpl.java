package com.gm.project.gmtool.activeCodebatch.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.activeCodebatch.mapper.CodeBatchMapper;
import com.gm.project.gmtool.activeCodebatch.domain.CodeBatch;
import com.gm.project.gmtool.activeCodebatch.service.ICodeBatchService;
import com.gm.common.utils.text.Convert;

/**
 * 激活码批次号Service业务层处理
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
     * 查询激活码批次号
     * 
     * @param id 激活码批次号ID
     * @return 激活码批次号
     */
    @Override
    public CodeBatch selectCodeBatchById(Long id)
    {
        return codeBatchMapper.selectCodeBatchById(id);
    }

    /**
     * 查询激活码批次号列表
     * 
     * @param codeBatch 激活码批次号
     * @return 激活码批次号
     */
    @Override
    public List<CodeBatch> selectCodeBatchList(CodeBatch codeBatch)
    {
        return codeBatchMapper.selectCodeBatchList(codeBatch);
    }

    /**
     * 新增激活码批次号
     * 
     * @param codeBatch 激活码批次号
     * @return 结果
     */
    @Override
    public int insertCodeBatch(CodeBatch codeBatch)
    {
        return codeBatchMapper.insertCodeBatch(codeBatch);
    }

    /**
     * 修改激活码批次号
     * 
     * @param codeBatch 激活码批次号
     * @return 结果
     */
    @Override
    public int updateCodeBatch(CodeBatch codeBatch)
    {
        return codeBatchMapper.updateCodeBatch(codeBatch);
    }

    /**
     * 删除激活码批次号对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCodeBatchByIds(String ids)
    {
        return codeBatchMapper.deleteCodeBatchByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除激活码批次号信息
     * 
     * @param id 激活码批次号ID
     * @return 结果
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
