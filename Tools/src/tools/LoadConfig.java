/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import excelExport.ConfigExcelReader;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author hewei
 */
public class LoadConfig {

    public static final String configxmlFilePath = System.getProperty("user.dir") + File.separator + "config" + File.separator + "config.xml";

    private static boolean isLoad = false;

    /**
     * 从xml中加载配置数据
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static void load() throws ParserConfigurationException, SAXException, IOException, Exception {
        if (isLoad) {
            return;
        }
        isLoad = true;
        doXML();
    }

    /**
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static void doXML(String xmlFilePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File xmlFile = new File(xmlFilePath);
        Document doc = builder.parse(xmlFile);
        Element configure = doc.getDocumentElement();
        NodeList children = configure.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (child instanceof Element) {
                String name = child.getNodeName();
                switch (name) {
                    default:
                        doData((Element) child);
                        break;
                }
            }
        }
    }

    /**
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private static void doXML() throws Exception {
        doXML(configxmlFilePath);
    }

    /**
     * 操作数据
     *
     * @param element
     */
    private static void doData(Element element) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        NamedNodeMap attributes = element.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
                switch (attName) {
                    case "excelFilePath":
                        ConfigExcelReader.excelFilePath = attValue;
                        break;
                    case "javaPackageName":
                        ConfigExcelReader.javaPackageName = attValue;
                        break;
                    case "javaCodeBase":
                        ConfigExcelReader.javaCodeBase = attValue;
                        break;
                    case "javaPath":
                        ConfigExcelReader.javaPath = attValue;
                        break;
                    case "javaScriptPath":
                        ConfigExcelReader.javaScriptPath = attValue;
                        break;
                    case "languageCsvPath":
                        ConfigExcelReader.languageCsvPath = attValue;
                        break;
                    case "scriptConfigManagerPath":
                        ConfigExcelReader.scriptConfigManagerPath = attValue;
                        break;
                    case "excelFilePathOtherBase":
                        ConfigExcelReader.excelFilePathOtherBase = attValue;
                        break;
                    case "excelFilePathOther":
                        ConfigExcelReader.excelFilePathOther = attValue;
                        break;

                    default:
                        break;
                }

        }
    }

}
