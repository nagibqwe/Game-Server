/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.structs;

/**
 * Определение типов игровых серверов
 * @author soko <xuchangming@haowan123.com>
 */
public class ServerType {
    public final static int GAMESERVER_TEST = 0;   // Тестовый игровой сервер
    public final static int GAMESERVER = 1;        // Игровой сервер
    public final static int LOGINSERVER = 2;       // Сервер входа
    public final static int PUBLICSERVER = 3;      // Публичный управляющий сервер
    public final static int FIGHTSERVER = 4;       // Боевой сервер
    public final static int SocialServer = 5;      // Социальный сервер
}