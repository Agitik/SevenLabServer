package NetworkExchange;

import DBWorker.db_config;
import PutAndGetFromDB.BaseTicketBDCommands;
import PutAndGetFromDB.CollectionFiller;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class ServerMain {
    public static boolean Exit = false;
    public static int PORT = 9000;
    public static void main(String[] args) {
        Runnable startServer = () -> {
            db_config.setDataBase("localhost");
            CollectionFiller.updateCollectionFromDB();
            start();
        };
        Thread server = new Thread(startServer);
        server.start();
        while (!Exit){
            Scanner scanner = new Scanner(System.in);
            if (scanner.nextLine().toLowerCase().trim().equals("exit")){
                Exit = true;
                System.out.println("Сервер отключен!");
            }
        }
    }
    public static void start(){
        try {
            Server.server(PORT);
        } catch (IOException e) {
            System.out.println("Ошибка на сервере");
            System.out.println("Пробую использовать другой порт");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException interruptedException) {
                System.out.println("Не получилось подождать");
            }
            PORT--;
            System.out.println("Пробую порт: " + PORT);
            start();
        } catch (ClassNotFoundException e) {
            System.out.println("Ошибка чтения запроса");
            e.printStackTrace();
        } catch (NoSuchElementException e){
            System.out.println("Работа программы принудительно завершена!");
        }
    }
}
