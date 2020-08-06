import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ListViewHandler extends ChannelInboundHandlerAdapter {

    private final Callback callBack;

    public ListViewHandler(Callback callBack) {
        this.callBack = callBack;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //callBack.callBack(msg);
        //NettyNetwork.listView.getItems().add(msg);
    }
}
