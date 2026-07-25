#!/bin/sh

# write packet info
SVNROMOTEPATH=http://192.168.1.8:8080/svn/qinji2/trunk/server/Q2
MYSQL_HOST=127.0.0.1
MYSQL_USER=game_root
MYSQL_PASSW=game123456
ROOTPATH=`pwd`
SVNWORKPATH=$ROOTPATH/trunk
TARGETPACKET=lsm-trunk-`date +%Y%m%d`-v1

#begin check data
echo begin checkout code!!!
svn co $SVNROMOTEPATH $SVNWORKPATH

echo checkout end
sleep 3
echo ========================================
echo 开始编译GameCore

cd $SVNWORKPATH/GameCore
ant -f build-ant.xml

echo 完成编译GameCore
sleep 3
echo ========================================
echo 开始编译Message

cd $SVNWORKPATH/Message
ant -f build-ant.xml

echo 完成编译Message
sleep 5
echo ========================================
echo 开始编译GameServer

cd $SVNWORKPATH/GameServer
ant -f build-ant.xml

echo 完成编译GameServer
sleep 3
echo ========================================
echo 开始编译WorldServer

cd $SVNWORKPATH/WorldServer
ant -f build-ant.xml

echo 完成编译WorldServer
sleep 3
echo =======================================
echo 导出策划配置表
mysqldump -u$MYSQL_USER -p$MYSQL_PASSW -h$MYSQL_HOST qin2_game_data > qin2_game_data.sql
echo 导出游戏数据表
mysqldump -u$MYSQL_USER -p$MYSQL_PASSW -h$MYSQL_HOST --opt -d qin2_game  > qin2_game.sql

echo 导表完成
echo =======================================
echo 开始打包

cd $ROOTPATH
mkdir $TARGETPACKET

cp -r $SVNWORKPATH/GameServer/gameserver $TARGETPACKET
cp -r $SVNWORKPATH/WorldServer/worldserver $TARGETPACKET
cp -v $SVNWORKPATH/shell/manager.sh $TARGETPACKET
cp -v $SVNWORKPATH/shell/readme.txt $TARGETPACKET
cp -v $SVNWORKPATH/GameServer/config/server-config.xml $TARGETPACKET/server-config.xml.game
cp -v $SVNWORKPATH/GameServer/config/client-server-config.xml $TARGETPACKET/client-server-config.xml.game
cp -v $SVNWORKPATH/WorldServer/config/server-config.xml $TARGETPACKET/server-config.xml.world


tar -czvf $TARGETPACKET.tar.gz $TARGETPACKET
sz $TARGETPACKET.tar.gz

echo 打包 $TARGETPACKET.tar.gz 成功!














echo " make success"


