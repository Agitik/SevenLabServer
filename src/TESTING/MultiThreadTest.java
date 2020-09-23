package TESTING;


import Accounter.User;
import NetworkExchange.AnswerPack;
import NetworkExchange.ClientCommands;
import NetworkExchange.QuestionPack;
import NetworkExchange.UnPacker;
import DBWorker.db_config;
import PutAndGetFromDB.BaseTicketBDCommands;
import PutAndGetFromDB.CollectionFiller;
import StartClasses.Ticket;
import TESTING.makers.ConsoleTicketMaker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadTest {
    public static void main(String[] args) {
        User user = new User("testuser", "w");
        Ticket t1 = ConsoleTicketMaker.setTicketFromConsole();

        ExecutorService executorService = Executors.newCachedThreadPool();

        db_config.setDataBase("localhost");
        CollectionFiller.updateCollectionFromDB();
        Runnable info = () -> {
            AnswerPack ap = UnPacker.UnPackAndExecute(new QuestionPack(ClientCommands.INFO));
            ap.answer.forEach(System.out::println);
        };

        Runnable show = () -> {
            AnswerPack ap = UnPacker.UnPackAndExecute(new QuestionPack(ClientCommands.SHOW));
            ap.answer.forEach(System.out::println);
        };

        Runnable add = () -> {
            Boolean ret = BaseTicketBDCommands.addTicketInTable(user, t1);
        };

        executorService.submit(info);
        executorService.submit(add);
        executorService.submit(show);

    }
}
