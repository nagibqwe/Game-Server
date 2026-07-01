/**
 * @date 2014/4/18 15:00
 * @author ChenLong
 */
package game.core.message;

/**
 * <b>消息编号工具类</b>
 * <pre>
 * ==================================================
 * 消息号定义：
 * 1.消息为10进制6位数
 * 2.前三位为功能编号（100~999）
 * 3.第四位为来源（1:SC   2:CS  3:SS  4:CC）
 * 4.后两位为具体功能来源的消息编号（01~99）
 * ==================================================
 * 例如
 * 消息号：100201
 * 前三位   100     xx功能
 * 第四位   2      （1:SC   2:CS  3:SS  4:CC 5:CW 6:WC）
 * 后两位   1       消息编号为1
 * </pre>
 *
 * @author ChenLong
 */
public class MessageNumber
{
    public final static int MESSAGE_SOURCE_CS = 1;
    public final static int MESSAGE_SOURCE_CW = 5;

    /**
     * 验证消息号是否有效
     *
     * @param msgId
     * @return
     */
    public static boolean isAvailable(int msgId)
    {
        return msgId > 100000 && msgId < 999999;
    }

    /**
     * 获取消息号功能
     *
     * @param msgId
     * @return
     */
    public static int getFunction(int msgId)
    {
        if (isAvailable(msgId))
            return msgId / 1000;
        else
            return 0;
    }

    /**
     * 获取消息号来源
     *
     * @param msgId
     * @return
     */
    public static int getSource(int msgId)
    {
        if (isAvailable(msgId))
            return (msgId / 100) % 10;
        else
            return 0;
    }

    /**
     * 获取消息号消息编号
     *
     * @param msgId
     * @return
     */
    public static int getSerialNum(int msgId)
    {
        if (isAvailable(msgId))
            return msgId % 100;
        else
            return 0;
    }
}
