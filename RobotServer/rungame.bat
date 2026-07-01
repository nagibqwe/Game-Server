::call build.bat
cd ./robotserver/
java -DideDebug=true -XX:+AggressiveOpts -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=9851 -classpath RobotServer.jar Main
pause