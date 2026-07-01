#!/bin/sh

#JAVA 虚拟机启动参数
GAME_OPTS='-server -verbose -Xmn1g -Djava.nio.channels.spi.SelectorProvider=sun.nio.ch.EPollSelectorProvider -XX:PermSize=512m -XX:MaxPermSize=512m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseG1GC -Xloggc:gc.log  -Xms2048m -Xmx2048m'

WORLD_OPTS='-server  -Xms2048m -Xmx2048m'

BASEPATH=`pwd`
GAMEPATH=$BASEPATH/gameserver
WORLDPATH=$BASEPATH/worldserver


#获取game进程
getGamePid() {
	SIGN=$GAMEPATH/GameServer.jar
	ps aux | grep java | grep $SIGN | awk '{print $2}'
}
#获取world进程
getWorldPid() {
	SIGN=$WORLDPATH/WorldServer.jar
	ps aux | grep java | grep $SIGN | awk '{print $2}'
}

#启动game server
startGame() {
	STARTPid=`getGamePid`
	echo $STARTPid
	if [ -z $STARTPid ];then
		echo "开始启动game服务器"
		cd $GAMEPATH
		nohup java $GAME_OPTS -jar $GAMEPATH/GameServer.jar &
		STARTPid=`getGamePid`
		if [ -z $STARTPid ];then
			echo "启动game服务器失败！！！"
		else
			echo 服务器game启动成功 pid: $STARTPid
		fi
	else
		echo 启动失败，game服正在运行  pid: $STARTPid
	fi
}

#关闭game server九 零一 起玩 www.90 175.com
stopGame() {
	STOPPid=`getGamePid`
	if [ -z $STOPPid ];then
		echo "服务器game未启动"
	else
		kill $STOPPid
		echo 服务器game关闭成功 kill: $STOPPid
	fi
}

#启动world server
startWorld() {
	STARTPid=`getWorldPid`
	echo $STARTPid
	if [ -z $STARTPid ];then
		echo "开始启动world服务器"
		cd $WORLDPATH
		nohup java $WORLD_OPTS -jar $WORLDPATH/WorldServer.jar &
		STARTPid=`getWorldPid`
		if [ -z $STARTPid ];then
			echo "启动world服务器失败！！！"
		else
			echo 服务器world启动成功 pid: $STARTPid
		fi
	else
		echo 启动失败，world服正在运行  pid: $STARTPid
	fi
}

#关闭world server
stopWorld() {
	STOPPid=`getWorldPid`
	if [ -z $STOPPid ];then
		echo "服务器world未启动"
	else
		kill $STOPPid
		echo 服务器world关闭成功 kill: $STOPPid
	fi
}

stopGame
stopWorld

echo "the end!!!!!"




