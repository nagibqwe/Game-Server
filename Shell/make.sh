#!/bin/sh

# write packet info
SVN_PJ_PATH=http://10.0.1.151/svn/MMORPG/TianZhiJin/trunk

ROOTPATH=`pwd`
SVNWORKPATH=$ROOTPATH/trunk

getVersion(){
if [ ! -f "version.txt" ]; then
        echo 0
else
        cat version.txt | awk 'END {print}'
fi
}

vs=`getVersion`
echo $vs
vs=`expr $vs + 1`
echo $vs
echo $vs >> version.txt

#target packet name
TARGET_PACKET=Server-`date +%Y%m%d`-$vs

#begin check data
echo begin checkout code!!!
svn co $SVN_PJ_PATH/Server $SVNWORKPATH/Server
svn co $SVN_PJ_PATH/Gamedata $SVNWORKPATH/Gamedata

echo checkout end

if [ ! -x "$SVNWORKPATH/Server" ]; then
	echo 更新目录不存在，失败了
	exit 1
fi
if [ ! -x "$SVNWORKPATH/Gamedata" ]; then
	echo 更新目录不存在，失败了
	exit 2
fi

echo ========================================
echo 开始编译GameCore

cd $SVNWORKPATH/Server/GameCore
rm -rf dist build
ant -f build-ant.xml
if [ ! -f "$SVNWORKPATH/Server/GameCore/dist/GameCore.jar" ]; then
	echo GameCore编译失败了
	exit 3
fi
echo 完成编译GameCore
sleep 1

echo ========================================
echo 开始编译GameCfg

cd $SVNWORKPATH/Server/GameCfg
rm -rf dist build
ant -f build-ant.xml
if [ ! -f "$SVNWORKPATH/Server/GameCfg/dist/GameCfg.jar" ]; then
	echo GameCfg编译失败了
	exit 4
fi
echo 完成编译GameCfg
sleep 1

echo ========================================
echo 开始编译Message

cd $SVNWORKPATH/Server/Message
rm -rf dist build
ant -f build-ant.xml
if [ ! -f "$SVNWORKPATH/Server/Message/dist/Message.jar" ]; then
    echo Message编译失败了
    exit 5
fi
echo 完成编译Message
sleep 1

echo ========================================
echo 开始编译GameServer

cd $SVNWORKPATH/Server/GameServer
rm -rf gameserver build
ant -f build-ant.xml
if [ ! -f "$SVNWORKPATH/Server/GameServer/gameserver/GameServer.jar" ]; then
    echo GameServer编译失败了
    exit 6
fi
echo 完成编译GameServer
sleep 1

echo ========================================
echo 开始编译PublicServer

cd $SVNWORKPATH/Server/PublicServer
rm -rf publicserver build
ant -f build-ant.xml
if [ ! -f "$SVNWORKPATH/Server/PublicServer/publicserver/PublicServer.jar" ]; then
    echo PublicServer编译失败了
    exit 7
fi
echo 完成编译PublicServer
sleep 1

echo ========================================
echo 开始编译SocialServer

cd $SVNWORKPATH/Server/SocialServer
rm -rf socialserver build
ant -f build-ant.xml
if [ ! -f "$SVNWORKPATH/Server/SocialServer/socialserver/SocialServer.jar" ]; then
    echo SocialServer编译失败了
    exit 8
fi
echo 完成编译SocialServer
sleep 1

echo =======================================
echo 开始打包
cd $SVNWORKPATH/Server/GameServer/
tar -czvf gameserver.tar.gz gameserver
mv -f gameserver.tar.gz $ROOTPATH
echo make gameserver.tar.gz over !

cd $SVNWORKPATH/Server/PublicServer/
tar -czvf publicserver.tar.gz publicserver
mv -f publicserver.tar.gz $ROOTPATH
echo make publicserver.tar.gz over !

cd $SVNWORKPATH/Server/SocialServer/
tar -czvf socialserver.tar.gz socialserver
mv -f socialserver.tar.gz $ROOTPATH
echo make socialserver.tar.gz over !

cd $ROOTPATH
tar -czvf  $TARGET_PACKET.tar.gz gameserver.tar.gz publicserver.tar.gz socialserver.tar.gz
