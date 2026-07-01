#!/bin/sh

# write packet info

ROOTPATH=`pwd`

SVNROMATEPATH=http://10.0.1.151/svn/MMORPG/TianZhiJin/trunk/Server
SVNWORKPATH=$ROOTPATH/trunk

#SVNROMATEPATH_FENZHI=http://10.0.1.151/svn/MMORPG/TianZhiJin/branches/1.2.0.0_0312_2019.03.10_Main/Server
#SVNROMATEPATH_FENZHI=http://10.0.1.151/svn/MMORPG/TianZhiJin/branches/1.2.0.0_0416_2020.04.10_Main/trunk/Server
SVNROMATEPATH_FENZHI=http://10.0.1.151/svn/MMORPG/TianZhiJin/branches/1.2.1.0_0701_2020.06.19_Main/trunk/Server
SVNWORKPATH_FENZHI=$ROOTPATH/branches

ARG=$1

svnCode(){
	echo ========================================
	svn co --username luowei --password yZ1r67T7 $1 $2
	cd $2
	svn up --username luowei --password yZ1r67T7
	echo checkout end
	echo ========================================
}

makeMessage(){
	echo ========================================
	echo 开始编译Message
	cd $1/Message
	ant -f build-ant.xml
	echo 完成编译Message
	echo ========================================
}

makeCfg(){
	echo 开始编译GameCfg
	cd $1/GameCfg
	ant -f build-ant.xml
	echo 完成编译GameCfg
	echo ========================================
}

makeGame(){
	makeMessage $1
	makeCfg $1
	sleep 3
	echo ========================================
	echo 开始编译GameServer
	cd $1/GameServer
	ant -f build-ant.xml
	echo 完成编译GameServer
	echo ========================================
}

makePublic(){
	makeMessage $1
	makeCfg $1
	sleep 3
	echo ========================================
	echo 开始编译publicserver
	cd $1/PublicServer
	ant -f build-ant.xml
	echo 完成编译publicserver
	echo ========================================
}


#打包压缩上传ftp
game(){

	cp -r $2/GameServer/gameserver $1
	cp -v $ROOTPATH/manager.sh $1/gameserver/manager.sh
	cp -v $1/server-config.xml $1/gameserver/config/server-config.xml
	
	cd $1/gameserver
	sh manager.sh stop

	rm -rf $1/logs/*.*

	echo 启动时间:`date +%Y.%m.%d_%H:%M:%S` >> version

        sleep 10
	sh manager.sh start

	echo $1" start success"
}

public(){
	cp -r $2/PublicServer/publicserver $1
	cp -v $ROOTPATH/manager_public.sh $1/publicserver/manager.sh
	cp -v $1/server-config.xml $1/publicserver/config/server-config.xml
	
	cd $1/publicserver
	sh manager.sh stop
        sleep 10
	echo 启动时间:`date +%Y.%m.%d_%H:%M:%S` >> version

	sh manager.sh start

	echo "public start success"
}

tips(){
	echo "请输入控制命令："
	echo "0：所有服务器"
	echo "1：分支服"
	echo "2：分支跨服"
	echo "3：分支公共服"
}

start0(){
	svnCode $SVNROMATEPATH_FENZHI $SVNWORKPATH_FENZHI
	makeGame $SVNWORKPATH_FENZHI
	makePublic $SVNWORKPATH_FENZHI	
	
	sleep 5
	public $ROOTPATH/server/fenzhi_public $SVNWORKPATH_FENZHI
	sleep 5
	game $ROOTPATH/server/fenzhi_fight $SVNWORKPATH_FENZHI
	sleep 5
	game $ROOTPATH/server/fenzhi $SVNWORKPATH_FENZHI
}

start1(){
	svnCode $SVNROMATEPATH_FENZHI $SVNWORKPATH_FENZHI
	makeGame $SVNWORKPATH_FENZHI
	game $ROOTPATH/server/fenzhi $SVNWORKPATH_FENZHI
}

start2(){
	svnCode $SVNROMATEPATH_FENZHI $SVNWORKPATH_FENZHI
	makeGame $SVNWORKPATH_FENZHI
	game $ROOTPATH/server/fenzhi_fight $SVNWORKPATH_FENZHI
}

start3(){
	svnCode $SVNROMATEPATH_FENZHI $SVNWORKPATH_FENZHI
	makePublic $SVNWORKPATH_FENZHI
	public $ROOTPATH/server/fenzhi_public $SVNWORKPATH_FENZHI
}

#控制中心九  零一 起玩 www.90  175.c  om
start_game(){

	echo 输入参数：$1
	if [ "$1" = "0" ] ;then  
		start0
	elif [ "$1" = "1" ];then  
		start1
	elif [ "$1" = "2" ];then  
		start2
	elif [ "$1" = "3" ];then  
		start3
	else    
		echo "错误的命令输入！！！请重新输入"
		exit 1
	fi  

}


if [ -z $ARG ];then
	echo "===============welcome monitor=========================="
        rm -rf /data/branches
	tips
	read input
	start_game $input 
else
	start_game $ARG
fi


