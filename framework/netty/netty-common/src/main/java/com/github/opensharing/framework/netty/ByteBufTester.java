package com.github.opensharing.framework.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * 测试ByteBuf
 *
 * @author jwen
 * Date 2023/11/11
 */
public class ByteBufTester {

    public static void main(String[] args) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(8, 20);

        byteBuf.writeBytes(new byte[]{1,2,3,4});

        print(byteBuf);
    }

    private static void print(ByteBuf byteBuf) {
        System.out.println("byteBuf.isDirect        : " + byteBuf.isDirect());

        System.out.println("byteBuf.isReadable      : " + byteBuf.isReadable());
        System.out.println("byteBuf.readableBytes   : " + byteBuf.readableBytes());
        System.out.println("byteBuf.readerIndex     : " + byteBuf.readerIndex());

        System.out.println("byteBuf.isWritable      : " + byteBuf.isWritable());
        System.out.println("byteBuf.writableBytes   : " + byteBuf.writableBytes());
        System.out.println("byteBuf.writerIndex     : " + byteBuf.writerIndex());

        System.out.println("byteBuf.capacity        : " + byteBuf.capacity());
        System.out.println("byteBuf.maxCapacity     : " + byteBuf.maxCapacity());
    }
}
