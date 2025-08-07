package com.github.opensharing.framework.netty.hander;

import java.util.List;

import com.github.opensharing.framework.netty.MessageProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * MyMessageDecoder
 *
 * @author jwen
 * Date 2023/11/14
 */
@Slf4j
public class MyMessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        //需要将得到二进制字节码-> MessageProtocol 数据包(对象)

        //封装成 MessageProtocol 对象，放入 out， 传递下一个handler业务处理
        MessageProtocol messageProtocol = new MessageProtocol(in);
        //放入out传给下一个hanlder进行处理
        out.add(messageProtocol);
    }
}