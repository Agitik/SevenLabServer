package NetworkExchange;

import DBWorker.db_config;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Server {
    public static boolean ExitCode = false;

    public static void server(int PORT) throws IOException, ClassNotFoundException {
        ExecutorService readClient = Executors.newCachedThreadPool();
        ExecutorService executeCommand = Executors.newCachedThreadPool();
        ExecutorService send_answer = Executors.newFixedThreadPool(5);
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", PORT));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
        Scanner scr = new Scanner(System.in);
        while (!ExitCode) {
            if (System.in.available() > 0) {
                if (scr.next().equals("exit")) {
                    ExitCode = true;
                }
            }
            selector.select(2000);
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext() & !ExitCode) {
                SelectionKey key = iter.next();
                int interestOps = key.interestOps();
                if (key.isAcceptable() & !ExitCode) {
                    Runnable readCallClient = () -> {
                        SocketChannel client = null;
                        try {
                            client = serverSocket.accept();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            client.configureBlocking(false);
                        } catch (IOException e) {
                            System.out.println("IO в потоке");
                        }
                        try {
                            client.register(selector, SelectionKey.OP_READ);
                        } catch (ClosedChannelException e) {
                            System.out.println("CCE в потоке");
                        }
                    };
                    readClient.submit(readCallClient);
                }
                if (key.isReadable() & !ExitCode) {
                    Runnable callableExe = () -> {
                        SocketChannel client = (SocketChannel) key.channel();
                        try {
                            client.read(buffer);
                        } catch (IOException e) {
                            System.out.println("Косяк в потоке exe");
                        }

                        //Обработка вопроса
                        ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
                        ObjectInputStream ois = null;
                        try {
                            ois = new ObjectInputStream(bais);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        QuestionPack qp = null;
                        try {
                            qp = (QuestionPack) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        AnswerPack ap = UnPacker.UnPackAndExecute(qp);
                        Runnable answer = () -> {
                            //Подготовка ответа
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ObjectOutputStream oos = null;
                            try {
                                oos = new ObjectOutputStream(baos);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                oos.writeObject(ap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            byte[] data = baos.toByteArray();

                            try {
                                client.write(ByteBuffer.wrap(data));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            buffer.clear();
                            key.cancel();
                        };
                        send_answer.submit(answer);
                    };
                    executeCommand.submit(callableExe);
                }
                iter.remove();
            }
        }
    }
}