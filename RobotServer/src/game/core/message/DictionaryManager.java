package game.core.message;

import com.google.protobuf.GeneratedMessage;
import game.core.command.Handler;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import org.dom4j.DocumentException;

/**
 *
 * <b>消息字典管理器.</b>
 * <p>
 * 提供消息注册、查找等一系列方法.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class DictionaryManager
{

    private static final Logger log = LogManager.getLogger(DictionaryManager.class);

    /**
     * 字典集合，key = 消息ID, value = 消息字典
     */
    private final Map<Integer, Dictionary> map = new ConcurrentHashMap<>();

    /**
     * 获取消息字典.
     *
     * @param msgId 消息ID
     * @return
     */
    public Dictionary get(int msgId)
    {
        return map.get(msgId);
    }

    /**
     * 注册消息类型和对应的处理函数.
     * <p>
     * 注：正式环境推荐使用load方法从配置文件中自动加载.
     *
     * @param msgId 消息ID
     * @param messageClass 消息类型
     * @param handlerClass 处理函数，可以为空
     * @return 注册成功返回true，失败返回false
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public boolean register(int msgId,
            Class<? extends GeneratedMessage> messageClass,
            Class<? extends Handler> handlerClass) throws InstantiationException, IllegalAccessException
    {
        if (messageClass != null)
        {
            map.put(msgId, new Dictionary(messageClass, handlerClass));
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 从配置文件加载消息类型和对应的处理函数.
     * <p>
     * 该方法是线程安全的，可以在运行时动态加载.
     *
     * @param filePath 配置文件的全路径
     * @throws DocumentException
     */
    @SuppressWarnings("unchecked")
    public synchronized void load(String filePath) throws Exception
    {
        Dom4jReader xml = new Dom4jReader(filePath);
        Map<String, String>[] nodeMap = xml.getNodeAttributeList("//msg");
        if (nodeMap != null && nodeMap.length > 0)
        {
            Map<Integer, Dictionary> newMap = new HashMap<>();

            for (Map<String, String> node : nodeMap)
            {
                Integer msgId = null;
                Class messageClass = null;
                Class handlerClass = null;

                for (Entry<String, String> entry : node.entrySet())
                {
                    switch (entry.getKey())
                    {
                        case "id":
                            msgId = Integer.parseInt(entry.getValue());
                            break;
                        case "message":
                            messageClass = Class.forName(entry.getValue());
                            if (messageClass.getSuperclass() != GeneratedMessage.class)
                            {
                                log.error("Class type error! 'message' must be com.google.protobuf.GeneratedMessage: "
                                        + entry.getValue() + ", file: " + filePath);
                            }
                            break;
                        case "handler":
                            handlerClass = Class.forName(entry.getValue());
                            if (handlerClass.getSuperclass() != Handler.class)
                            {
                                log.error("Class type error! 'handler' must be game.core.command.Handler: "
                                        + entry.getValue() + ", file: " + filePath);
                            }
                            break;
                        default:
                            break;
                    }
                }

                if (messageClass == null)
                {
                    log.error("Not found 'message'!  file: " + filePath);
                    return;
                }

                /**
                 * 如果XML中没有为消息配置id，则尝试自动读取消息ID<br>
                 * 但在编写Protobuf文件时，必须按此命名规范为每个消息设置id：enum MsgID { eMsgID =
                 * 10086; };
                 */
                if (msgId == null)
                {
                    Class clazz = Class.forName(messageClass.getName() + "$MsgID");
                    msgId = clazz.getField("eMsgID_VALUE").getInt(clazz);
                }

                log.info("Load message>> " + msgId + ": [message " + messageClass + "], [handler " + handlerClass + "]");
                newMap.put(msgId, new Dictionary(messageClass, handlerClass));
            }

            map.clear();
            map.putAll(newMap);
        }
        else
        {
            log.error("No messages! loading cancelled. file: " + filePath);
        }
    }

}
