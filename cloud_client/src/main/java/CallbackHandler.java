import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.application.Platform;

public class CallbackHandler extends SimpleChannelInboundHandler<String> {

    private Callback1 callBack;
    private String result;

    public String getResult() {
        return result;
    }

    public CallbackHandler(Callback1 callBack) {
        this.callBack = callBack;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        //Platform.runLater(() -> callBack.call(s));
        result = callBack.call(s);
    }
}
