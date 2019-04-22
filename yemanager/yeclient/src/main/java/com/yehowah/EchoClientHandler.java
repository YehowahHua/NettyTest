package com.yehowah;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf>{
    //当从服务器接收到一条消息时被调用
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("Client reseived: "+msg.toString(CharsetUtil.UTF_8));
    }
    //在到服务器的连接已经建立之后将被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty msg rocks",CharsetUtil.UTF_8));
        System.out.println("<*-- channelActive --*>");
    }

    //在处理过程中引发异常时被调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
