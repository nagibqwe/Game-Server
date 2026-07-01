cd ../Tools

copy /y ..\GameCfg\dist\GameCfg.jar dist\lib 
java -Xms1024m -Xmx1024m -Xmn512m -classpath dist/Tools.jar excelExport.ExcelToCode

