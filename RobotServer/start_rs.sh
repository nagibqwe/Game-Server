#!/bin/sh

ps -ef | grep /data/robot/dist/RobotServer | grep -v grep | cut -c 9-15 | xargs kill -s 9
echo "kill old process success"

rm -rf dist
echo "remove old dist success"
tar -zxvf dist.tar.gz
echo "tar dist.tar.gz success"

rm -rf dist/config/server-config.xml
cp server-config.xml dist/config/server-config.xml

cd dist
screen java -server -verbose -Djava.nio.channels.spi.SelectorProvider=sun.nio.ch.EPollSelectorProvider -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseG1GC -XX:+UseAdaptiveSizePolicy -XX:+UseTLAB  -XX:+ResizeTLAB  -Xcomp -XX:+PrintHeapAtGC -Xloggc:gc.log  -Xms2g -Xmx2g -Xmn1g -jar /data/robot/dist/RobotServer.jar

echo "start success"

