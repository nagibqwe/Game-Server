package com.game.player.structs;

/**
 * @explain: desc
 * @time Created on 2019/11/27 10:47.
 * @author: tc
 */
public class QuitGameDefine {

	public static final int Normal = 0;//正常退出
	public static final int HeartTooQuick = 1;//心跳过快
	public static final int HeartStop = 2;//心跳停止了
	public static final int GM = 3;//gm命令踢下线
	public static final int SocketClosed = 4;//socket断开退出
	public static final int RepeatLogin = 5;//重复登录
	public static final int MyOutClient = 6;//主动关闭调用的接口
	public static final int ClientMsg = 7;//网络信息过来的关闭
	public static final int PlayerLoginTick = 8;//登录完成踢出编号

	public static final String[] QuitGameDesc = {
			"正常退出",
			"心跳过快",
			"心跳停止了",
			"gm命令踢下线",
			"socket断开退出",
			"重复登录",
			"主动关闭调用的接口",
			"网络信息过来的关闭",
			"登录完成踢出编号"
	};
}
