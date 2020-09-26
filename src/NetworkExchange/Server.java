package NetworkExchange;

import DBWorker.db_config;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    public static ExecutorService sendForClient = Executors.newFixedThreadPool(5);
    public static ExecutorService readClient = Executors.newCachedThreadPool();
    public static ExecutorService exeClient = Executors.newCachedThreadPool();
    public static void server(int PORT) throws IOException, ClassNotFoundException {
        boolean ExitCode = ServerMain.Exit;
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", PORT));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        Scanner scr = new Scanner(System.in);
        while (!ExitCode) {
            selector.select(2000);
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext() & !ExitCode) {
                SelectionKey key = iter.next();
                int interestOps = key.interestOps();
                if (key.isAcceptable() & !ExitCode) {
                    SocketChannel client = serverSocket.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                }
                if (key.isReadable() & !ExitCode) {
                    getClient(key.channel());
                    key.cancel();
                }
                iter.remove();
            }
            ExitCode = ServerMain.Exit;
            if(ExitCode){
                sendForClient.shutdown();
                readClient.shutdown();
                exeClient.shutdown();
            }
        }
    }

    public static void getClient(Channel ch) throws IOException, ClassNotFoundException {
        Runnable read = () -> {
            SocketChannel client = (SocketChannel) ch;
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
            try {
                client.read(buffer);
                //Обработка вопроса
                ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
                ObjectInputStream ois = new ObjectInputStream(bais);
                QuestionPack qp = (QuestionPack) ois.readObject();

                Runnable exe = () -> {
                    AnswerPack ap = UnPacker.UnPackAndExecute(qp);
                    Runnable send = () -> {
                        //Подготовка ответа
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = null;
                        try {
                            oos = new ObjectOutputStream(baos);
                            oos.writeObject(ap);

                            byte[] data = baos.toByteArray();

                            client.write(ByteBuffer.wrap(data));
                            buffer.clear();
                        } catch (IOException e) {
                            System.out.println("Чет не так с отправкой");
                        }
                    };
                    sendForClient.submit(send);
                };
                exeClient.submit(exe);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Косяк в чтении клиента");
            }
        };
        readClient.submit(read);
    }
}