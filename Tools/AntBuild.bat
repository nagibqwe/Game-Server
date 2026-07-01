cd ../Tools

rmdir dist /S /Q

call ant -f build-ant.xml
if not exist dist goto:end
::del *.tar /s
::del *.tar 
::del *.gz /s
::del *.gz 

echo 成功
goto:quit

:end
echo 编译失败
:quit

