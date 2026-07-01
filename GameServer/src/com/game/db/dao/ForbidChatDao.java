/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.ForbidChatBean;
import java.util.List;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author hewei
 */
public class ForbidChatDao {

    public List<ForbidChatBean> selectAll() {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            List<ForbidChatBean> list = session.selectList("forbidchat.selectAll");
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

}
