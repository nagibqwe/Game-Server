package com.gm.project.gmtool.roleTransfer.service;

import java.util.List;
import com.gm.project.gmtool.roleTransfer.domain.RoleTransfer;

/**
 * Перенос персонажаService接口
 * 
 * @author gm
 * @date 2021-11-03
 */
public interface IRoleTransferService 
{
    /**
     * 查询Перенос персонажа
     * 
     * @param roleId Перенос персонажаID
     * @return Перенос персонажа
     */
    public RoleTransfer selectRoleTransferById(String roleId);

    /**
     * 查询Перенос персонажа列表
     * 
     * @param roleTransfer Перенос персонажа
     * @return Перенос персонажа集合
     */
    public List<RoleTransfer> selectRoleTransferList(RoleTransfer roleTransfer);

    /**
     * ДобавитьПеренос персонажа
     * 
     * @param roleTransfer Перенос персонажа
     * @return Результат
     */
    public int insertRoleTransfer(RoleTransfer roleTransfer);

    /**
     * ИзменитьПеренос персонажа
     * 
     * @param roleTransfer Перенос персонажа
     * @return Результат
     */
    public int updateRoleTransfer(RoleTransfer roleTransfer);

    /**
     * 批量УдалитьПеренос персонажа
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteRoleTransferByIds(String ids);

    /**
     * УдалитьПеренос персонажаИнформация
     * 
     * @param roleId Перенос персонажаID
     * @return Результат
     */
    public int deleteRoleTransferById(String roleId);
}
