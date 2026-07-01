/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.GameMaster;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Administrator
 */
public class GameMasterDao {

    public GameMaster selectByUserId(long userId) {
        GameMaster gm = new GameMaster();
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            gm = (GameMaster) session.selectOne("GameMaster.selectByUserId", userId);
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return gm;
    }
}
