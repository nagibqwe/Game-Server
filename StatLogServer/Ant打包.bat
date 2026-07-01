set projectname=statlogserver
rmdir %projectname% /S /Q

call ant -f build-ant.xml
if not exist %projectname% goto:end
::del *.tar /s
del *.tar 
::del *.gz /s
del *.gz 

7z a -aoa -ttar %projectname%.tar %projectname%

7z a -aoa -tgzip %projectname%.tar.gz %projectname%.tar
echo 成功
goto:quit

:end
echo 编译失败
:quit

pause
