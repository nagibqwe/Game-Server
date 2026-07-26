package com.gm.project.gmtool.blackuser.service;

import java.util.List;
import com.gm.project.gmtool.blackuser.domain.Blackuser;

/**
 * Чёрный списокService接口
 * 
 * @author gm
 * @date 2021-11-04
 */
public interface IBlackuserService 
{
    /**
     * 查询Чёрный список
     * 
     * @param id Чёрный списокID
     * @return Чёрный список
     */
    public Blackuser selectBlackuserById(Integer id);

    /**
     * 查询Чёрный список列表
     * 
     * @param blackuser Чёрный список
     * @return Чёрный список集合
     */
    public List<Blackuser> selectBlackuserList(Blackuser blackuser);

    /**
     * ДобавитьЧёрный список
     * 
     * @param blackuser Чёрный список
     * @return Результат
     */
    public int insertBlackuser(Blackuser blackuser);

    /**
     * ИзменитьЧёрный список
     * 
     * @param blackuser Чёрный список
     * @return Результат
     */
    public int updateBlackuser(Blackuser blackuser);

    /**
     * 批量УдалитьЧёрный список
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteBlackuserByIds(String ids);

    /**
     * УдалитьЧёрный списокИнформация
     * 
     * @param id Чёрный списокID
     * @return Результат
     */
    public int deleteBlackuserById(Integer id);
}
