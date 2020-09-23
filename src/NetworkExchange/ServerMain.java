package NetworkExchange;

import DBWorker.db_config;
import PutAndGetFromDB.BaseTicketBDCommands;
import PutAndGetFromDB.CollectionFiller;

import java.io.IOException;
import java.util.NoSuchElementException;


public class ServerMain {
    public static int PORT = 9000;
    public static void main(String[] args) {
        db_config.setDataBase("localhost");
        CollectionFiller.updateCollectionFromDB();
        start();
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
