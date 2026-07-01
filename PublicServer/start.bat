@echo off
title PublicServer
color 6f


xcopy server-config.xml gameserver\config /y
cd publicserver
java -server -jar -DideDebug=true PublicServer.jar
pause
