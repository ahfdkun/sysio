package io.rpcdemo.rpc.transport;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.rpcdemo.rpc.ResponseMappingCallback;
import io.rpcdemo.util.Packmsg;


public class ClientResponses  extends ChannelInboundHandlerAdapter {

    //consumer.....
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Packmsg responsepkg = (Packmsg) msg;

        //曾经我们没考虑返回的事情
        ResponseMappingCallback.runCallBack(responsepkg);

    }
}

