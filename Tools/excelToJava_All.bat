java -Xms512m -Xmx512m -Xmn128m -classpath dist/Tools.jar excelExport.ExcelToCode

cd ../GameCfg
call AntBuild.bat

cd ../GameCfg_Japan
call AntBuild.bat

cd ../GameCfg_Korea
call AntBuild.bat

cd ../GameCfg_Russian
call AntBuild.bat

cd ../GameCfg_SG_MY_TH
call AntBuild.bat

cd ../GameCfg_Taiwan
call AntBuild.bat

cd ../GameCfg_Thailand
call AntBuild.bat

cd ../GameCfg_USA
call AntBuild.bat

cd ../GameCfg_Vietnam
call AntBuild.bat