@echo off

SET START_CD=%CD%
SET TRUNK_ROOT=%CD%\..
SET TOOL_ROOT=%CD%

CD %TRUNK_ROOT%
::echo CD TRUNK_ROOT
::echo TRUNK_ROOT : %TRUNK_ROOT%
echo ==============================Make Scripts=================================
CALL %TOOL_ROOT%\Python\Script\MakeScripts.bat
::echo CALL TOOL_ROOT\Python\Script\MakeScripts.bat
::echo TOOL_ROOT : %TOOL_ROOT%
echo =========================Make Scripts Finished=====================
CD %START_CD%
::echo CD START_CD
::echo START_CD : %START_CD%
echo on
xcopy config\* ..\config\YedAi\ /e /y
pause
