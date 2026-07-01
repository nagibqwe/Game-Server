package excelExport;

import org.apache.log4j.Logger;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * Created by 542 on 2019/6/18.
 */
public class InputExcelToCode {

    public final static Logger logger = Logger.getLogger(InputExcelToCode.class);
    public final static Logger error = Logger.getLogger("ERRORLOG");


    public static void loadOne(String Name){

        String ExcelName = Name+".xlsx";


        File f = new File(System.getProperty("user.dir") + File.separator + ConfigExcelReader.excelFilePath + File.separator + ExcelName);
        if (!f.canRead()) {
            ExcelName = Name+".xlsm";
            f = new File(System.getProperty("user.dir") + File.separator + ConfigExcelReader.excelFilePath + File.separator + ExcelName);
            if (!f.canRead()) {
                error.error("未找到配置表： " +ExcelName);
                return;
            }
        }


        List<String> javaCodeBasePathes = new ArrayList<>();
        javaCodeBasePathes.add(ConfigExcelReader.javaCodeBase);
        switch (Name.toLowerCase()){
            case "global":
                GlobalExcelToCode.globalToCode(ConfigExcelReader.excelFilePath, javaCodeBasePathes);
                break;
            case "server_drop_item":
            case "server_drop_package":
                DropExcelToCode.dropToCode(ConfigExcelReader.excelFilePath, javaCodeBasePathes);
                break;
            case  "messagestring":
                MessageStringToCode.messageStringToCode(ConfigExcelReader.excelFilePath, javaCodeBasePathes);
                break;
            case "functionstart":
            case "functionvariable":
                FunctionExcelToCode.functionToCode(ConfigExcelReader.excelFilePath, javaCodeBasePathes);
                load(ExcelName,javaCodeBasePathes);
                break;
            case "itemchangereason":
                ItemChangeReasonExcelToCode.ItemChangeReasonToCode(ConfigExcelReader.excelFilePath, javaCodeBasePathes);
                break;

             default:
                 load(ExcelName,javaCodeBasePathes);
                 break;

        }

    }
    private  static  void  load(String ExcelName, List<String> javaCodeBasePathes ){
        ExcelLoadData codeData = dropSelectToCode(ExcelName,javaCodeBasePathes,"China");
        if (codeData!=null){
            HashMap<String, ExcelLoadData> map = new HashMap<>();
            map= getAllExcelData();
            List<ExcelLoadData> DataList = new ArrayList<>();
            if (!map.containsKey(codeData.getBeanName().toLowerCase())){
                for(String key : map.keySet()) {
                    DataList.add(map.get(key));
                }
                DataList.add(codeData);
                ExcelToCode.toScriptConfigManagerCode(DataList,javaCodeBasePathes);
            }
        }else
            error.error("配置表：" +ExcelName +" 加载失败");
    }
    private static ExcelLoadData  dropSelectToCode(String ExcelName,List<String> javaCodeBasePathes,String languageType){
        ExcelLoadData datas = ExcelToCode.toCode(ConfigExcelReader.excelFilePath, javaCodeBasePathes, ExcelName, languageType);
        if (datas == null) {
            return null;
        }
        return  datas;
    }

    private static  HashMap<String,ExcelLoadData> getAllExcelData(){
        HashMap<String, ExcelLoadData> map = new HashMap<>();
        try {
            Class c = null;
            c = Class.forName("com.data.CfgManager");
            Method[] ms = c.getMethods();
            String name = "";
            String lowerName = "";
            for (int i = 0; i < ms.length; i++) {
                if (ms[i].getName().contains("Container")) {
                    ExcelLoadData data = new ExcelLoadData();
                    name = ms[i].getName().substring(7);
                    name = name.substring(0, name.length() - 10);
                    lowerName = name.toLowerCase();
                    if (lowerName.contains("drop_item")||lowerName.contains("drop_package")||lowerName.contains("global")||lowerName.contains("messagestring"))
                        continue;
                    data.setFilename(name);
                    data.setBeanName(name);
                    map.put(lowerName, data);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return  map;
    }
}
