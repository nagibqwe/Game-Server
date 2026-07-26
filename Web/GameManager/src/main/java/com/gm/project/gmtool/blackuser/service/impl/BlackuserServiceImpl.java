package com.gm.project.gmtool.blackuser.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.blackuser.mapper.BlackuserMapper;
import com.gm.project.gmtool.blackuser.domain.Blackuser;
import com.gm.project.gmtool.blackuser.service.IBlackuserService;
import com.gm.common.utils.text.Convert;

/**
 * Чёрный списокService业务层处理
 * 
 * @author gm
 * @date 2021-11-04
 */
@Service
public class BlackuserServiceImpl implements IBlackuserService 
{
    @Autowired
    private BlackuserMapper blackuserMapper;

    /**
     * 查询Чёрный список
     * 
     * @param id Чёрный списокID
     * @return Чёрный список
     */
    @Override
    public Blackuser selectBlackuserById(Integer id)
    {
        return blackuserMapper.selectBlackuserById(id);
    }

    /**
     * 查询Чёрный список列表
     * 
     * @param blackuser Чёрный список
     * @return Чёрный список
     */
    @Override
    public List<Blackuser> selectBlackuserList(Blackuser blackuser)
    {
        return blackuserMapper.selectBlackuserList(blackuser);
    }

    /**
     * ДобавитьЧёрный список
     * 
     * @param blackuser Чёрный список
     * @return Результат
     */
    @Override
    public int insertBlackuser(Blackuser blackuser)
    {
        return blackuserMapper.insertBlackuser(blackuser);
    }

    /**
     * ИзменитьЧёрный список
     * 
     * @param blackuser Чёрный список
     * @return Результат
     */
    @Override
    public int updateBlackuser(Blackuser blackuser)
    {
        return blackuserMapper.updateBlackuser(blackuser);
    }

    /**
     * УдалитьЧёрный список对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteBlackuserByIds(String ids)
    {
        return blackuserMapper.deleteBlackuserByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьЧёрный списокИнформация
     * 
     * @param id Чёрный списокID
     * @return Результат
     */
    @Override
    public int deleteBlackuserById(Integer id)
    {
        return blackuserMapper.deleteBlackuserById(id);
    }
}
