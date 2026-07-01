#!/bin/sh


#版本数据库
MYSQL_HOST=127.0.0.1
MYSQL_USER=game_root
MYSQL_PASSW=game123456
DATABASE_GAME=lsm_game
DATABASE_DATA=lsm_game_data



echo =======================================
echo 导出策划配置表
mysqldump -u$MYSQL_USER -p$MYSQL_PASSW -h$MYSQL_HOST $DATABASE_DATA > $DATABASE_DATA.sql
echo 导出游戏数据表
mysqldump -u$MYSQL_USER -p$MYSQL_PASSW -h$MYSQL_HOST --opt -d $DATABASE_GAME  > $DATABASE_GAME.sql

echo 导表完成



