package game.core.message;

/**
 *
 * @date 2014-7-3
 * @author pengmian
 */
public class MessageDictionary
{
    private final static DictionaryManager dictionaries = new DictionaryManager(); // 消息字典集合

    /**
     * 用枚举来实现单例
     */
    private enum Singleton
    {
        INSTANCE;
        MessageDictionary processor;

        Singleton()
        {
            this.processor = new MessageDictionary();
        }

        MessageDictionary getMessageDictionary()
        {
            return processor;
        }
    }

    /**
     * 获取MessageDictionary的实例对象.
     *
     * @return
     */
    public static MessageDictionary getInstance()
    {
        return Singleton.INSTANCE.getMessageDictionary();
    }

    /**
     * 读取消息映射
     *
     * @param str
     * @throws Exception
     */
    public void load(String str) throws Exception
    {
        dictionaries.init(str);
    }

    /**
     * 获得消息字典
     *
     * @param msgId
     * @return
     */
    public Dictionary getDictionary(int msgId)
    {
        return dictionaries.get(msgId);
    }

    public int size(){
        return dictionaries.size();
    }
}
