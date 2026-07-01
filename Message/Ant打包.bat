::rmdir dist /S /Q

call ant -f build-ant.xml
if not exist dist goto:end

echo 成功
goto:quit

:end
echo 编译失败
:quit

pause
