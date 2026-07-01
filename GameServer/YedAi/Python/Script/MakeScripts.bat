
call %TOOL_ROOT%\Python\Script\SetEnv.bat
echo =========================make diagram exports==============================
DEL %DEST_ROOT%\*.* /Q
SET OLD_CD = %CD%
CD %SOURCE_ROOT%
FOR /r %%i IN (*.graphml) DO (
	SET REAL_SOURCE=%%~ni.txt
	SET REAL_SOURCE_PATH=%%i
	rem echo %%~nxi
	CALL %TOOLS_ROOT%\Make_One_Diagram.bat
)
rem echo #define script_count		^(%RES_COUNT%^) >> %DEST_ROOT%\DiagramFileMappingTemp.h
rem call %TOOLS_ROOT%\ConvertCase.bat
rem CD %OLD_CD%
echo =============================finished======================================

pause
