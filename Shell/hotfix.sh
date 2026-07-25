#!/bin/sh

# write packet info
SVNROMOTEPATH=http://192.168.1.8:8080/svn/qinji2/trunk/server/Q2

#更新文件列表九零 一起玩 www.9 01 75.com
GameServerHotFixList="Enter_Map.java"
WorldServerHotFixList=""

BeginTime=`date +'%Y-%m-%d %H:%M:%S'`

#cur dir
ROOTPATH=`pwd`
#check dir
SVNWORKPATH=$ROOTPATH/trunk
TARGETHEAD=lsm-hotfix-`date +%Y%m%d`

#一天不可能打100个包吧 = =！
getVersion(){
	for((i=1;i<=100;i++));do  
		if [ ! -f "$TARGETHEAD-v$i.tar.gz" ]; then
			echo $i
			break
		fi
	done
}
#hotfix file
TARGETPACKET=$TARGETHEAD-v`getVersion`

#begin check data
echo begin checkout code!!!
svn co $SVNROMOTEPATH $SVNWORKPATH

echo checkout end
if [ ! -x "$SVNWORKPATH" ]; then
	echo 更新目录不存在，失败了
	exit 1
fi

sleep 3
echo ========================================
echo 开始编译GameCore

cd $SVNWORKPATH/GameCore
rm -rf dist build
ant -f build-ant.xml
if [ ! -f "$SVNWORKPATH/GameCore/dist/GameCore.jar" ]; then
	echo GameCore编译失败了
	exit 2
fi

echo 完成编译GameCore
sleep 2
echo ========================================
echo 开始编译Message

cd $SVNWORKPATH/Message
rm -rf dist build
ant -f build-ant.xml
if [ ! -f "$SVNWORKPATH/Message/dist/Message.jar" ]; then
        echo Message编译失败了
        exit 3
fi
echo 完成编译Message
sleep 2
echo ========================================
echo 开始编译GameServer

cd $SVNWORKPATH/GameServer
rm -rf gameserver build
ant -f build-ant.xml
if [ ! -f "$SVNWORKPATH/GameServer/gameserver/GameServer.jar" ]; then
        echo GameServer编译失败了
        exit 4
fi
echo 完成编译GameServer
sleep 1
echo ========================================
echo 开始编译WorldServer

cd $SVNWORKPATH/WorldServer
rm -rf worldserver build
ant -f build-ant.xml
if [ ! -f "$SVNWORKPATH/WorldServer/worldserver/WorldServer.jar" ]; then
        echo WorldServer编译失败了
        exit 5
fi
echo 完成编译WorldServer
sleep 1

cd $ROOTPATH

echo =======================================

echo =======================================
echo 开始打包
cd $SVNWORKPATH/GameServer/
tar -czvf gameserver.tar.gz gameserver
mv gameserver.tar.gz ../../
echo make gameserver.tar.gz over !
cd $SVNWORKPATH/WorldServer/
tar -czvf worldserver.tar.gz worldserver
mv worldserver.tar.gz ../../
echo make worldserver.tar.gz over !

cd ../../
tar -czvf all.tar.gz worldserver.tar.gz gameserver.tar.gz readme.txt

echo 开始生成热更新包

cd $SVNWORKPATH
rm hotfix -rf
mkdir -p hotfix/$TARGETPACKET

cd $SVNWORKPATH/GameServer/
rm hotfix -rf
for file in $GameServerHotFixList
do
        path=`find gameserver/ -name $file`
        echo $path
        newPath=hotfix/${path%/*}
        echo $newPath
        mkdir -p $newPath
        cp $path $newPath
done
if [ -x "$SVNWORKPATH/GameServer/hotfix" ]; then
	cd hotfix
	mv gameserver $SVNWORKPATH/hotfix/$TARGETPACKET
fi


cd $SVNWORKPATH/WorldServer/
rm hotfix -rf

for file in $WorldServerHotFixList
do
        path=`find worldserver/ -name $file`
        echo $path
        newPath=hotfix/${path%/*}
        echo $newPath
        mkdir -p $newPath
        cp $path $newPath
done
if [ -x "$SVNWORKPATH/WorldServer/hotfix" ]; then
	cd hotfix
	mv worldserver $SVNWORKPATH/hotfix/$TARGETPACKET
fi

cd $SVNWORKPATH/hotfix
tar -czvf $TARGETPACKET.tar.gz $TARGETPACKET
mv $TARGETPACKET.tar.gz ../../
cd ../../

sz $TARGETPACKET.tar.gz

md5sum $TARGETPACKET.tar.gz
