cd ../../Tool/_Gadgets/

call ExportExcleData.bat

lua ../../Client/Assets/StreamingAssets/LuaRoot/DatabaseExporter.lua "../../Client"

cd ../../javaServer/Tools

java -classpath dist/Tools.jar excelExport.ExcelToCode

cd ../GameCfg
call AntBuild.bat

cd ../GameCfg_cht
call AntBuild.bat

cd ../GameCfg_eng
call AntBuild.bat

cd ../GameCfg_enu
call AntBuild.bat

cd ../GameCfg_jpn
call AntBuild.bat

cd ../GameCfg_kor
call AntBuild.bat

cd ../GameCfg_rus
call AntBuild.bat

cd ../GameCfg_tha
call AntBuild.bat

cd ../GameCfg_vietnam
call AntBuild.bat