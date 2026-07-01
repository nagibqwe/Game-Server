#!/bin/sh

#JAVA 虚拟机启动参数
GAME_OPTS=' -server -verbose -Djava.nio.channels.spi.SelectorProvider=sun.nio.ch.EPollSelectorProvider -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseG1GC -XX:+UseAdaptiveSizePolicy -XX:+UseTLAB  -XX:+ResizeTLAB  -Xcomp -XX:+PrintHeapAtGC -Xloggc:gc.log  -Xms4g -Xmx4g -Xmn1g -Dio.netty.leakDetectionLevel=advanced '

WORLD_OPTS=' -server -verbose -Djava.nio.channels.spi.SelectorProvider=sun.nio.ch.EPollSelectorProvider -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseG1GC -XX:+UseAdaptiveSizePolicy -XX:+UseTLAB  -XX:+ResizeTLAB  -Xcomp -XX:+PrintHeapAtGC -Xloggc:gc.log  -Xms2g -Xmx2g -Xmn512m -Dio.netty.leakDetectionLevel=advanced'

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
		cd $BASEPATH
		cp server-config.xml.game $GAMEPATH/config/server-config.xml
		cp client-server-config.xml.game $GAMEPATH/config/client-server-config.xml
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

#关闭game server
stopGame() {
	STOPPid=`getGamePid`
	if [ -z $STOPPid ];then
		echo "服务器game未启动"
	else
		kill $STOPPid
		echo 服务器game关闭成功 kill: $STOPPid
	fi
}

#启动world server九 零一起 玩 www .9017 5. com
startWorld() {
	STARTPid=`getWorldPid`
	echo $STARTPid
	if [ -z $STARTPid ];then
		echo "开始启动world服务器"
		cd $BASEPATH
		cp server-config.xml.world $WORLDPATH/config/server-config.xml
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


startWorld
startGame

echo "the end!!!!!"




