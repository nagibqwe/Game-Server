@echo off
title SocialServer
color 5f


xcopy server-config.xml socialserver\config /y
cd socialserver
java -server -jar -DideDebug=true SocialServer.jar
pause
