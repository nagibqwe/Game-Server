@echo off
title GameServer
color 9f


xcopy server-config.xml gameserver\config /y
cd gameserver
java -server -jar -DideDebug=true GameServer.jar
pause
