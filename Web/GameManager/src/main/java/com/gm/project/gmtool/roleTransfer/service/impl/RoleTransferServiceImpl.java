package com.gm.project.gmtool.roleTransfer.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.roleTransfer.mapper.RoleTransferMapper;
import com.gm.project.gmtool.roleTransfer.domain.RoleTransfer;
import com.gm.project.gmtool.roleTransfer.service.IRoleTransferService;
import com.gm.common.utils.text.Convert;

/**
 * Перенос персонажаService业务层处理
 * 
 * @author gm
 * @date 2021-11-03
 */
@Service
public class RoleTransferServiceImpl implements IRoleTransferService 
{
    @Autowired
    private RoleTransferMapper roleTransferMapper;

    /**
     * 查询Перенос персонажа
     * 
     * @param roleId Перенос персонажаID
     * @return Перенос персонажа
     */
    @Override
    public RoleTransfer selectRoleTransferById(String roleId)
    {
        return roleTransferMapper.selectRoleTransferById(roleId);
    }

    /**
     * 查询Перенос персонажа列表
     * 
     * @param roleTransfer Перенос персонажа
     * @return Перенос персонажа
     */
    @Override
    public List<RoleTransfer> selectRoleTransferList(RoleTransfer roleTransfer)
    {
        return roleTransferMapper.selectRoleTransferList(roleTransfer);
    }

    /**
     * ДобавитьПеренос персонажа
     * 
     * @param roleTransfer Перенос персонажа
     * @return Результат
     */
    @Override
    public int insertRoleTransfer(RoleTransfer roleTransfer)
    {
        return roleTransferMapper.insertRoleTransfer(roleTransfer);
    }

    /**
     * ИзменитьПеренос персонажа
     * 
     * @param roleTransfer Перенос персонажа
     * @return Результат
     */
    @Override
    public int updateRoleTransfer(RoleTransfer roleTransfer)
    {
        return roleTransferMapper.updateRoleTransfer(roleTransfer);
    }

    /**
     * УдалитьПеренос персонажа对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteRoleTransferByIds(String ids)
    {
        return roleTransferMapper.deleteRoleTransferByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьПеренос персонажаИнформация
     * 
     * @param roleId Перенос персонажаID
     * @return Результат
     */
    @Override
    public int deleteRoleTransferById(String roleId)
    {
        return roleTransferMapper.deleteRoleTransferById(roleId);
    }
}
