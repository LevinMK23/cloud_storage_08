package NIO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

public class NIOServer implements Runnable {
    private ServerSocketChannel server;
    private State state;
    private Selector selector;
    private static int cnt = 1;
    private String serverFilePath = "./common/src/main/resources/serverFiles";
    private ByteBuffer one = ByteBuffer.allocate(1);
    private ByteBuffer buffer = ByteBuffer.allocate(256);

    public NIOServer() throws IOException {
        server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress(8189));
        server.configureBlocking(false);
        selector = Selector.open();
        server.register(selector, SelectionKey.OP_ACCEPT);
        state = State.COMMAND;
    }

    @Override
    public void run() {
        try {
            System.out.println("server started");
            while (server.isOpen()) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        handleAccept(key);
                    }
                    if (key.isReadable()) {
                        // TODO: 7/23/2020 fileStorage handle
                        //handleRead(key);
                        handleEchoRead(key);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private enum State {
        COMMAND, NAME, BYTES
    }

    private void handleEchoRead(SelectionKey key) throws IOException {
        System.out.println("echo");
        SocketChannel channel = (SocketChannel) key.channel();
        int bytesCount = 1;
        StringBuilder tmp = new StringBuilder();
        while (bytesCount > 0) {
            bytesCount = channel.read(buffer);
            buffer.flip();
            while (buffer.hasRemaining()) {
                tmp.append((char) buffer.get());
            }
            buffer.flip();
        }
        String message = tmp.toString();
        System.out.println("message: " + message);
        for (SelectionKey item : selector.keys()) {
            if (item.channel() instanceof SocketChannel && item.isReadable()) {
                ((SocketChannel)item.channel())
                        .write(ByteBuffer.wrap(message.getBytes()));
            }
        }
        channel.close();
    }

    private void handleRead(SelectionKey key) throws IOException {
        System.out.println("read handled");
        SocketChannel channel = (SocketChannel) key.channel();
        Path path = null;
        if (state == State.COMMAND) {
            String feedBack = read(channel);
            if (feedBack.equals("./upload")) {
                channel.write(ByteBuffer.wrap("command received!".getBytes()));
            }
            state = State.NAME;
        }
        if (state == State.NAME) {
            String fileName = read(channel);
            channel.write(ByteBuffer.wrap("command received!".getBytes()));
            path = Paths.get(serverFilePath + "/" + fileName);
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            state = State.BYTES;
        }
        if (state == State.BYTES) {
            readToFile(channel, path);
            channel.write(ByteBuffer.wrap("command received!".getBytes()));
            state = State.COMMAND;
        }
        //channel.close();
    }

    private void readToFile(SocketChannel channel, Path path) throws IOException {
        int count = 100;
        ByteBuffer buffer = ByteBuffer.allocate(80);
        while (count > 0) {
            count = channel.read(buffer);
            buffer.flip();
            byte[] tmp = new byte[cnt];
            int pos = 0;
            while (count-- > 0) {
                tmp[pos++] = buffer.get();
            }
            buffer.flip();
            Files.write(path, tmp, StandardOpenOption.APPEND);
        }
    }

    private String read(SocketChannel channel) throws IOException {
        int count = 100;
        ByteBuffer buffer = ByteBuffer.allocate(80);
        StringBuilder s = new StringBuilder();
        while (count > 0) {
            count = channel.read(buffer);
            buffer.flip();
            while (count-- > 0) {
                s.append((char) buffer.get());
            }
            System.out.println(count + ":" + s);
            buffer.flip();
        }
        String command = s.toString();
        System.out.println("command: " + command);
        return command;
    }

    private void handleAccept(SelectionKey key) throws IOException {
        String userName = "user#" + cnt++;
        System.out.println("client " + userName + " accepted!");
        SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ, userName);
    }

    public static void main(String[] args) throws IOException {
        new Thread(new NIOServer()).start();
    }
}
