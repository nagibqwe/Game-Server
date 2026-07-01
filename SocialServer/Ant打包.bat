rmdir socialserver /S /Q

call ant -f build-ant.xml
if not exist socialserver goto:end
::del *.tar /s
del *.tar 
::del *.gz /s
del *.gz 

7z a -aoa -ttar socialserver.tar socialserver

7z a -aoa -tgzip socialserver.tar.gz socialserver.tar
echo 成功
goto:quit

:end
echo 编译失败
:quit

pause
