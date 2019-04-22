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
                    .channel(NioServerSocketChannel.class)// 指定所使用的NIO传输Channel
                    .localAddress(new InetSocketAddress(port))// 使用制定的端口设置套接字地址
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //EchoServerHandler被标注为@Shareable,所以我们可以总是使用同样的实例
                            ch.pipeline().addLast(serverHandler);
                        }
                    });

            //异步的绑定服务器；调用sync()方法阻塞等待直到绑定完成
            ChannelFuture future = bootstrap.bind().sync();
            //获取Channel的CloseFuture，并且阻塞当前线程直到它完成
            future.channel().closeFuture().sync();
        }finally{
            group.shutdownGracefully().sync();
        }



    }
}
