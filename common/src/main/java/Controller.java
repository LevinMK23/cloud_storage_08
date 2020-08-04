import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public ListView<String> lv;
    public TextField txt;
    public Button send;
    public SocketChannel channel;
    private Selector selector;
    private ByteBuffer buffer = ByteBuffer.allocate(256);
    private DataInputStream is;
    private DataOutputStream os;
    private final String clientFilesPath = "./common/src/main/resources/clientFiles";

    public void initStreams() throws IOException {
        Socket socket = new Socket("localhost", 8189);
        is = new DataInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            channel = SocketChannel.open();
            channel.connect(new InetSocketAddress("localhost", 8189));
            channel.configureBlocking(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        File dir = new File(clientFilesPath);
        for (String file : Objects.requireNonNull(dir.list())) {
            lv.getItems().add(file);
        }
        lv.setOnMouseClicked(a -> {
            if (a.getClickCount() == 2) {
                String fileName = lv.getSelectionModel().getSelectedItem();
                txt.setText("./upload " + fileName);
            }
        });
    }


    // ./download fileName
    // ./upload fileName
    public void sendCommand(ActionEvent actionEvent) {
        String command = txt.getText();
        try {
            channel = SocketChannel.open();
            channel.connect(new InetSocketAddress("localhost", 8189));
            channel.configureBlocking(false);
            channel.write(ByteBuffer.wrap(command.getBytes()));
            int cnt = channel.read(buffer);
            buffer.flip();
            StringBuilder tmp = new StringBuilder();
            while (buffer.hasRemaining()){
                tmp.append((char) buffer.get());
            }
            buffer.rewind();
            channel.close();
            lv.getItems().add(tmp.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        String[] op = command.split(" ");
//        if (op[0].equals("./download")) {
//            try {
//                os.writeUTF(op[0]);
//                os.writeUTF(op[1]);
//                String response = is.readUTF();
//                System.out.println("resp: " + response);
//                if (response.equals("OK")) {
//                    File file = new File(clientFilesPath + "/" + op[1]);
//                    if (!file.exists()) {
//                        boolean f = file.createNewFile();
//                    }
//                    long len = is.readLong();
//                    byte[] buffer = new byte[1024];
//                    try (FileOutputStream fos = new FileOutputStream(file)) {
//                        if (len < 1024) {
//                            int count = is.read(buffer);
//                            fos.write(buffer, 0, count);
//                        } else {
//                            for (long i = 0; i < len / 1024; i++) {
//                                int count = is.read(buffer);
//                                fos.write(buffer, 0, count);
//                            }
//                        }
//                    }
//                    lv.getItems().add(op[1]);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            // TODO: 7/23/2020 upload
//            try {
//                byte[] buffer = new byte[1024];
//                //command
//                os.write(op[0].getBytes());
//                os.flush();
//                int cnt = is.read(buffer);
//                byte[] bytes = new byte[cnt];
//                if (cnt >= 0) System.arraycopy(buffer, 0, bytes, 0, cnt);
//                String response = new String(bytes, UTF_8);
//                System.out.println("command ./upload: " + response);
//                if (response.equals("command received!")) {
//                    //fileName
//                    String fileName = op[1];
//                    System.out.println("fileName = " + fileName);
//                    os.write(fileName.getBytes());
//                    os.flush();
//                    cnt = is.read(buffer);
//                    bytes = new byte[cnt];
//                    if (cnt >= 0) System.arraycopy(buffer, 0, bytes, 0, cnt);
//                    response = new String(bytes, UTF_8);
//                    System.out.println("fileName - " + fileName + ": " + response);
//                    if (response.equals("command received!")) {
//                        //fileBytes
//                        File file = new File(clientFilesPath + "/" + fileName);
//                        FileInputStream fis = new FileInputStream(file);
//                        while (fis.available() > 0) {
//                            cnt = fis.read(buffer);
//                            os.write(buffer, 0, cnt);
//                        }
//                        cnt = is.read(buffer);
//                        bytes = new byte[cnt];
//                        if (cnt >= 0) System.arraycopy(buffer, 0, bytes, 0, cnt);
//                        response = new String(bytes, UTF_8);
//                        System.out.println("file received: " + response);
//                    }
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
    }
}
