package com.yehowah;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    //对于每个传入的消息都要调用
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: "+in.toString(CharsetUtil.UTF_8));
        ctx.write(in);
//        super.channelRead(ctx, msg);
    }

    //通知ChannelInboundHandler 最后一次对channelRead()的调用时当前批量读取中的最后一条消息
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        //        super.channelReadComplete(ctx);
    }

    //在读取操作期间，有异常抛出时会调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
