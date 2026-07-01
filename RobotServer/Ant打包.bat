rmdir robotserver /S /Q

call ant -f build-ant.xml
if not exist robotserver goto:end
::del *.tar /s
del *.tar 
::del *.gz /s
del *.gz 

7z a -aoa -ttar robotserver.tar robotserver 

7z a -aoa -tgzip robotserver.tar.gz robotserver.tar

del robotserver.tar
echo 成功
goto:quit

:end
echo 编译失败
:quit

pause
