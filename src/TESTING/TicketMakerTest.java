package TESTING;

import Accounter.User;
import NetworkExchange.Argument;
import NetworkExchange.ClientCommands;
import NetworkExchange.CommandExecuter;
import NetworkExchange.QuestionPack;
import DBWorker.db_config;
import PutAndGetFromDB.CollectionFiller;

public class TicketMakerTest {
    public static void main(String[] args) {
        db_config.setDataBase("localhost");

        CollectionFiller.updateCollectionFromDB();

        User us = new User("tolochek_ds", "d");
        Argument arg = new Argument();
        arg.user = us;
        arg.key = Long.parseLong("1");
        QuestionPack qp = new QuestionPack(ClientCommands.CLEAR, arg);

        CommandExecuter.login(qp).answer.forEach(System.out::println);
    }
}
