import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class NettyController implements Initializable {

    public Button send;
    public ListView<String> listView;
    public TextField text;
    private NettyNetwork network;

    public void sendCommand(ActionEvent actionEvent) {
        try {
            network.sendMessage(text.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        network = NettyNetwork.getInstance();
        //network.setCallBack(str -> listView.getItems().add(str));

        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return text.getText();
            }

            @Override
            protected void succeeded() {
                try {
                    listView.getItems().add(get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(task).start();

    }
}
