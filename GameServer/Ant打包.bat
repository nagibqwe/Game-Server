rmdir gameserver /S /Q

call ant -f build-ant.xml
if not exist gameserver goto:end
::del *.tar /s
del *.tar 
::del *.gz /s
del *.gz 

7z a -aoa -ttar gameserver.tar gameserver

7z a -aoa -tgzip gameserver.tar.gz gameserver.tar
echo 成功
goto:quit

:end
echo 编译失败
:quit

pause
