package com.game.chat.structs;

/**
 * 禁言类型枚举
 */
public class ForbidChatType {
    /**
     * 工作室禁言（消息只在工作室用户之间可见）
     */
    public static final int Studio = 1;
    /**
     * 全文替换禁言（聊天内容中的内容会被替换）
     */
    public static final int Replace_All = 2;
    /**
     * 关键字替换禁言（聊天内容中的关键字会被替换）
     */
    public static final int Replace_Word = 3;
    /**
     * 常规禁言（无法发送聊天内容）
     */
    public static final int Common = 4;
    /**
     * 隐形禁言（发言别人无法看到）
     */
    public static final int Invisible = 5;
    /**
     * 隔离禁言（只能在私聊和组队情况发言）
     */
    public static final int Quarantine = 6;
}
