package game.core.message;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 *
 * <b>Dom4j读取XML的工具类.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.3
 */
public class Dom4jReader
{

    private Document document;

    public Dom4jReader(String filePath) throws DocumentException
    {
        document = new SAXReader().read(new File(filePath));
    }
    
    public Dom4jReader(File file) throws DocumentException
    {
        document = new SAXReader().read(file);
    }

    public Dom4jReader(InputStream input) throws DocumentException
    {
        document = new SAXReader().read(input);
    }

    public Dom4jReader(URL url) throws DocumentException
    {
        document = new SAXReader().read(url);
    }

    /**
     * 获取某节点的子节点的值, 如果有多个父节点, 则只取最先遍历到的那个节点的子节点.
     * <p>
     * 如: 语法为"/movie-config/movie-info"时, 父节点为"movie-info",
     * 如果"movie-config"下有多个"movie-info"存在, 那么只获取最先遍历到的那个"movie-info"的子节点.
     * <p>
     * 另: 本方法只适用于子节点的名称定义无重复的情况.
     *
     * @param nodePath 节点路径(请使用XPath语法)
     * @return 节点集合, 采用键值对保存节点的名称和值(key = 节点名; value = 节点值),
     * 按遍历顺序将所有匹配的节点依次放入数组.<br>
     * 如果节点不存在, 或者该节点下没有子节点, 则返回null; 如果某子节点的值为空, 该子节点的值将被设置为空字符串: "".
     */
    public Map<String, String> getChildNodeText(String nodePath)
    {
        Map<String, String> results = null;
        Node node = document.selectSingleNode(nodePath);
        if (null != node)
        {
            results = new HashMap<>();
            for (Iterator it = ((Element) node).elementIterator(); it.hasNext();)
            {
                Element childNode = (Element) it.next();
                String key = childNode.getName();
                String value = childNode.getText();
                results.put(key, null == value ? "" : value);
            }
            if (results.isEmpty())
                results = null;
        }

        return results;
    }

    /**
     * 获取某节点的子节点的值, 如果有相同条件的节点, 则只取最先遍历到的那个节点的子节点.
     * <p>
     * 注意: 查询条件是匹配该节点的属性值, 如有下列用于记录电影基本信息的文档:<br>
     * .
     * .....<br>
     * &lt;movie-info id="1001"&gt;<br>
     * &lt;name&gt;金刚&lt;/name&gt;<br>
     * .
     * .....<br>
     * &lt;price&gt;0.5&lt;/price&gt;<br>
     * &lt;/movie-info&gt;<br>
     * &lt;movie-info id="1002"&gt;<br>
     * &lt;name&gt;生化危机III&lt;/name&gt;<br>
     * .
     * .....<br>
     * &lt;price&gt;0.8&lt;/price&gt;<br>
     * &lt;/movie-info&gt;<br>
     * .
     * .....<br>
     * 如: 调用getChildNodeTextByAttribute("//movie-info", "id", "1002"),
     * 将返回电影-"生化危机III"的基本信息.
     * <p>
     * 另: 本方法只适用于子节点的名称定义无重复的情况.
     *
     * @param nodePath 节点路径(请使用XPath语法)
     * @param attributeName 属性名称
     * @param attributeValue 属性的值
     * @return 节点集合, 采用键值对保存节点的名称和值(key = 节点名; value = 节点值).<br>
     * 如果节点不存在, 或者该节点下没有子节点, 则返回null; 如果某子节点的值为空, 该子节点的值将被设置为空字符串: "".
     */
    public Map<String, String> getChildNodeTextByAttribute(String nodePath,
            String attributeName, String attributeValue)
    {
        List nodes = document.selectNodes(nodePath);
        if (null == nodes || nodes.isEmpty())
            return null;

        Map<String, String> results = null;
        for (Iterator it = nodes.iterator(); it.hasNext();)
        {
            Node node = (Node) it.next();
            Attribute attribute = ((Element) node).attribute(attributeName);
            if (null != attribute)
            {
                String av = attribute.getValue();
                if (null != av && av.equals(attributeValue))
                {
                    results = new HashMap<>();
                    for (Iterator childIt = ((Element) node).elementIterator(); childIt
                            .hasNext();)
                    {
                        Element childNode = (Element) childIt.next();
                        String key = childNode.getName();
                        String value = childNode.getText();
                        results.put(key, null == value ? "" : value);
                    }
                    if (results.isEmpty())
                        results = null;
                    break;
                }
            }
        }

        return results;
    }

    /**
     * 获取节点名称相同的子节点的值列表, 每一个父节点下的子节点集合都作为一个Map放入数组.
     * <p>
     * 如: 语法为"/movie-config/movie-info"时, 父节点为"movie-info",
     * 如果"movie-config"下有多个"movie-info"存在, 那么,
     * 每个"movie-info"下的子节点的值列表都作为一个Map放入数组.
     * <p>
     * 另: 本方法只适用于子节点的名称定义无重复的情况.
     *
     * @param nodePath 节点路径(请使用XPath语法)
     * @return 节点集合, 采用键值对保存节点的名称和值(key = 节点名; value = 节点值),
     * 按遍历顺序将所有匹配的节点按一组子节点集合为单位依次放入数组.<br>
     * 如果一个节点都不存在, 或者所有节点下都没有子节点, 则返回null; 如果某子节点的值为空, 该子节点的值将被设置为空字符串: "".
     */
    @SuppressWarnings("unchecked")
    public Map<String, String>[] getChildNodeTextList(String nodePath)
    {
        List nodes = document.selectNodes(nodePath);
        if (null == nodes || nodes.isEmpty())
            return null;

        Map<String, String>[] results = new HashMap[nodes.size()];
        int index = 0;
        boolean isEmpty = true;
        for (Iterator it = nodes.iterator(); it.hasNext();)
        {
            Node node = (Node) it.next();
            results[index] = new HashMap();
            for (Iterator it2 = ((Element) node).elementIterator(); it2
                    .hasNext();)
            {
                Element childNode = (Element) it2.next();
                String key = childNode.getName();
                String value = childNode.getText();
                results[index].put(key, null == value ? "" : value);
                isEmpty = false;
            }
            index++;
        }

        if (isEmpty)
            results = null;

        return results;
    }

    /**
     * 获取某节点的属性集合.
     * <p>
     * 注意: 如果一个节点里定义了多个相同的属性名, 则只取最先遍历到的那个. 因为一个节点里具有相同的属性名, 通常无甚意义,
     * 所以在这里不考虑属性名相同的情况.
     *
     * @param nodePath 节点路径(请使用XPath语法)
     * @return 属性集合, 采用键值对保存属性的名称和值(key = 属性名; value = 属性值).<br>
     * 如果节点不存在, 或者该节点没有定义属性, 则返回null; 如果某一属性的值为空, 该属性的值将被设置为空字符串: "".
     */
    public Map<String, String> getNodeAttribute(String nodePath)
    {
        Map<String, String> results = null;
        Node node = document.selectSingleNode(nodePath);
        if (null != node)
        {
            List attributes = ((Element) node).attributes();
            if (attributes == null || attributes.isEmpty())
                return null;

            results = new HashMap<>();
            for (Iterator it = attributes.iterator(); it.hasNext();)
            {
                Attribute attribute = (Attribute) it.next();
                String key = attribute.getName();
                if (!results.containsKey(key))
                {
                    String value = attribute.getValue();
                    results.put(key, null == value ? "" : value);
                }
            }

            if (results.isEmpty())
                results = null;
        }

        return results;
    }

    /**
     * 获取某节点中某属性的值.
     * <p>
     * 注意: 如果一个节点里定义了多个相同的属性名, 则只取最先遍历到的那个. 因为一个节点里具有相同的属性名, 通常无甚意义,
     * 所以在这里不考虑属性名相同的情况.
     *
     * @param nodePath 节点路径(请使用XPath语法)
     * @param attributeName 属性名称
     * @return 属性的值, 如果节点不存在, 或者该节点没有定义此属性, 则返回null; 如果属性的值为空, 则返回空字符串: "".
     */
    public String getNodeAttribute(String nodePath, String attributeName)
    {
        Node node = document.selectSingleNode(nodePath);
        if (null != node)
        {
            Attribute attribute = ((Element) node).attribute(attributeName);
            if (null != attribute)
            {
                String value = attribute.getValue();
                return null == value ? "" : value;
            }
        }

        return null;
    }

    /**
     * 获取节点名称相同的属性集合列表
     * <p>
     * 注意: 如果一个节点里定义了多个相同的属性名, 则只取最先遍历到的那个. 因为一个节点里具有相同的属性名, 通常无甚意义,
     * 所以在这里不考虑属性名相同的情况.
     *
     * @param nodePath 节点路径(请使用XPath语法)
     * @return 属性集合, 采用键值对保存属性的名称和值(key = 属性名; value = 属性值),
     * 按遍历顺序将所有匹配的节点的属性集合依次放入数组.<br>
     * 如果一个节点都不存在, 或者所有的节点都没有定义属性, 则返回null; 如果某一属性的值为空, 该属性的值将被设置为空字符串: "".
     */
    @SuppressWarnings("unchecked")
    public Map<String, String>[] getNodeAttributeList(String nodePath)
    {
        List nodes = document.selectNodes(nodePath);
        if (null == nodes || nodes.isEmpty())
            return null;

        Map<String, String>[] results = new HashMap[nodes.size()];
        int index = 0;
        boolean isEmpty = true;
        for (Iterator it = nodes.iterator(); it.hasNext();)
        {
            Node node = (Node) it.next();
            results[index] = new HashMap();
            List attributes = ((Element) node).attributes();
            if (null != attributes && attributes.size() > 0)
            {
                for (Iterator it2 = attributes.iterator(); it2.hasNext();)
                {
                    Attribute attribute = (Attribute) it2.next();
                    String key = attribute.getName();
                    if (!results[index].containsKey(key))
                    {
                        String value = attribute.getValue();
                        results[index].put(key, null == value ? "" : value);
                        isEmpty = false;
                    }
                }
            }
            index++;
        }

        if (isEmpty)
            results = null;

        return results;
    }

    /**
     * 获取节点名称相同的某属性的值列表.
     * <p>
     * 注意: 如果一个节点里定义了多个相同的属性名, 则只取最先遍历到的那个. 因为一个节点里具有相同的属性名, 通常无甚意义,
     * 所以在这里不考虑属性名相同的情况.
     *
     * @param nodePath 节点路径(请使用XPath语法)
     * @param attributeName 属性名称
     * @return 属性的值, 按遍历顺序将所有匹配的节点的属性值依次放入数组.<br>
     * 如果一个节点都不存在, 或者所有的节点都没有定义该属性, 则返回null; 如果某一属性的值为空, 该属性的值将被设置为空字符串: "".
     */
    public String[] getNodeAttributeList(String nodePath, String attributeName)
    {
        List nodes = document.selectNodes(nodePath);
        if (null == nodes || nodes.isEmpty())
        {
            return null;
        }

        String[] results = new String[nodes.size()];
        int index = 0;
        boolean isEmpty = true;
        for (Iterator it = nodes.iterator(); it.hasNext();)
        {
            Node node = (Node) it.next();
            Attribute attribute = ((Element) node).attribute(attributeName);
            if (null != attribute)
            {
                String value = attribute.getValue();
                results[index] = null == value ? "" : value;
                isEmpty = false;
            }
            index++;
        }

        if (isEmpty)
            results = null;

        return results;
    }

    /**
     * 获取某节点的值, 如果有多个相同的节点, 则只取最先遍历到的那个.
     *
     * @param nodePath 节点路径(请使用XPath语法)
     * @return 节点的值, 如果节点不存在, 则返回null; 如果节点的值为空, 则返回空字符串: "".
     */
    public String getNodeText(String nodePath)
    {
        Node node = document.selectSingleNode(nodePath);
        if (null != node)
        {
            String value = node.getText();
            return null == value ? "" : value;
        }

        return null;
    }

    /**
     * 获取节点名称相同的值列表
     *
     * @param nodePath 节点路径(请使用XPath语法)
     * @return 节点的值, 按遍历顺序将所有匹配的节点的值依次放入数组.<br>
     * 如果一个节点都不存在, 则返回null; 如果某一节点的值为空, 该节点的值将被设置为空字符串: "".
     */
    public String[] getNodeTextList(String nodePath)
    {
        List nodes = document.selectNodes(nodePath);
        if (nodes == null || nodes.isEmpty())
            return null;

        String[] results = new String[nodes.size()];
        int index = 0;
        boolean isEmpty = true;
        for (Iterator it = nodes.iterator(); it.hasNext();)
        {
            Node node = (Node) it.next();
            String value = node.getText();
            results[index] = (null == value ? "" : value);
            isEmpty = false;
            index++;
        }

        if (isEmpty)
            results = null;

        return results;
    }

    /**
     * 将文档中某些Elements的Attribate自动赋值到对象属性.
     * <p>
     * 如某文档中有此一段:<br>
     * .
     * .....<br>
     * &lt;constant name="ip" value="127.0.0.1" type="String" ... /&gt;<br>
     * &lt;constant name="port" value="8080" type="int" ... /&gt;<br>
     * .
     * .....<br>
     * 要将所有constant节点的值都读取并赋值到Constants类中, 先假设Constants类中已包含诸如ip, port等属性, 然后调用:
     * <br>
     * toObjectProperties(Constants.class, "constant", "name", "type", "value");
     *
     * @param clazz 目标对象
     * @param elementName 被拷贝的一系列Elements名称
     * @param propertyName 一个Attribate名称, 表示对象的属性名称, 其值必须与目标对象的属性名称对应.
     * @param propertyType 一个Attribate名称, 表示对象的属性类型, 其值必须与目标对象的属性类型匹配.
     * @param propertyValue 一个Attribate名称, 表示对象的属性值, 其值必须与目标对象的属性类型匹配.
     */
    public void toObjectProperties(Class clazz, String elementName,
            String propertyName, String propertyType, String propertyValue)
    {
        try
        {
            Element root = document.getRootElement();
            Field[] fields = clazz.getDeclaredFields();
            boolean flag;

            for (Iterator i = root.elementIterator(elementName); i.hasNext();)
            {
                Element node = (Element) i.next();
                flag = false;
                for (Field field : fields)
                {
                    if (field.getName().equals(node.attributeValue(propertyName)))
                    {
                        field.setAccessible(true);
                        if (boolean.class.getName().equals(
                                node.attributeValue(propertyType)))
                        {
                            field.setBoolean(field, Boolean.parseBoolean(node.attributeValue(propertyValue)));
                        }
                        else if (byte.class.getName().equals(
                                node.attributeValue(propertyType)))
                        {
                            field.setByte(field, Byte.parseByte(node.attributeValue(propertyValue)));
                        }
                        else if (double.class.getName().equals(
                                node.attributeValue(propertyType)))
                        {
                            field.setDouble(field, Double.parseDouble(node.attributeValue(propertyValue)));
                        }
                        else if (float.class.getName().equals(
                                node.attributeValue(propertyType)))
                        {
                            field.setFloat(field, Float.parseFloat(node.attributeValue(propertyValue)));
                        }
                        else if (int.class.getName().equals(
                                node.attributeValue(propertyType)))
                        {
                            field.setInt(field, Integer.parseInt(node.attributeValue(propertyValue)));
                        }
                        else if (long.class.getName().equals(
                                node.attributeValue(propertyType)))
                        {
                            field.setLong(field, Long.parseLong(node.attributeValue(propertyValue)));
                        }
                        else if (short.class.getName().equals(
                                node.attributeValue(propertyType)))
                        {
                            field.setShort(field, Short.parseShort(node.attributeValue(propertyValue)));
                        }
                        else
                        {
                            field.set(field, node.attributeValue(propertyValue));
                        }

                        flag = true;
                        break;
                    }
                }

                if (!flag)
                {
                    throw new RuntimeException(document.getPath()
                            + "文件中定义的属性名没有在类" + clazz.getName() + "中定义! "
                            + propertyName + ": "
                            + node.attributeValue(propertyName));
                }
            }
        }
        catch (IllegalAccessException | RuntimeException e)
        {
        }
    }

    /**
     * 将文档解析为文本格式返回.
     *
     * @return 文本内容
     */
    public String parseText()
    {
        return document.asXML();
    }

}
