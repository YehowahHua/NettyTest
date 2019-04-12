package com.yehowah;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Hello world!
 * https://blog.csdn.net/weixin_43801935/article/details/86495682
 */
public class EchoServer
{
    private final int port;
    public EchoServer(int port){
        this.port = port;
    }
    public static void main( String[] args ) throws Exception
    {
        if (args.length != 1){
            System.out.println("Usage: "+EchoServer.class.getSimpleName()+" <port>");
            return;
        }
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();

        System.out.println( "Hello World!" );
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(serverHandler);
                        }
                    });

            ChannelFuture future = bootstrap.bind().sync();
            future.channel().closeFuture().sync();
        }finally{
            group.shutdownGracefully().sync();
        }



    }
}
