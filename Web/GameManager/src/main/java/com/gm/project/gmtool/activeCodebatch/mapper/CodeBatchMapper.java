package com.gm.project.gmtool.activeCodebatch.mapper;

import java.util.List;
import com.gm.project.gmtool.activeCodebatch.domain.CodeBatch;

/**
 * 激活码批次号Mapper接口
 * 
 * @author gm
 * @date 2021-09-22
 */
public interface CodeBatchMapper 
{
    /**
     * 查询激活码批次号
     * 
     * @param id 激活码批次号ID
     * @return 激活码批次号
     */
    public CodeBatch selectCodeBatchById(Long id);

    /**
     * 查询激活码批次号列表
     * 
     * @param codeBatch 激活码批次号
     * @return 激活码批次号集合
     */
    public List<CodeBatch> selectCodeBatchList(CodeBatch codeBatch);

    /**
     * 新增激活码批次号
     * 
     * @param codeBatch 激活码批次号
     * @return 结果
     */
    public int insertCodeBatch(CodeBatch codeBatch);

    /**
     * 修改激活码批次号
     * 
     * @param codeBatch 激活码批次号
     * @return 结果
     */
    public int updateCodeBatch(CodeBatch codeBatch);

    /**
     * 删除激活码批次号
     * 
     * @param id 激活码批次号ID
     * @return 结果
     */
    public int deleteCodeBatchById(Long id);

    /**
     * 批量删除激活码批次号
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCodeBatchByIds(String[] ids);

    /**
     * 获取最大的id
     * @return
     */
    public int selectMaxId();

    public int selectBatchId(int batchId);
}
