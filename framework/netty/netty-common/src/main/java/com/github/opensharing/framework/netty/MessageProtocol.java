package com.github.opensharing.framework.netty;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;

/**
 * 自定义消息协议
 *
 * @author jwen
 * Date 2023/11/13
 */
public class MessageProtocol {
    private int len = 1024*5; //关键
    private byte[] content = new byte[1024*5];

    public MessageProtocol() {
    }

    public MessageProtocol(String msg) {
        content = msg.getBytes(StandardCharsets.UTF_8);
        len = msg.getBytes(StandardCharsets.UTF_8).length;
    }

    public MessageProtocol(ByteBuf in) {
        len = in.readInt();
        content = new byte[len];
        in.readBytes(content);
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
