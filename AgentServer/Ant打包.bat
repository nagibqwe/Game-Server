rmdir agentserver /S /Q

call ant -f build-ant.xml
if not exist agentserver goto:end
::del *.tar /s
del *.tar 
::del *.gz /s
del *.gz 

7z a -aoa -ttar agentserver.tar agentserver

7z a -aoa -tgzip agentserver.tar.gz agentserver.tar

del agentserver.tar
echo 成功
goto:quit

:end
echo 编译失败
:quit

pause
