package TESTING;

import Accounter.User;
import DBWorker.db_config;
import PutAndGetFromDB.BaseTicketBDCommands;
import StartClasses.Ticket;
import TESTING.makers.ConsoleTicketMaker;

public class addTest {
    public static void main(String[] args) {
        db_config.setDataBase("localhost");

        User user = new User("testuser", "w");

        Ticket t1 = ConsoleTicketMaker.setTicketFromConsole();
        Runnable task = () -> {
            Boolean ret = BaseTicketBDCommands.addTicketInTable(user, t1);
        };

        new Thread(task).start();
    }
}
