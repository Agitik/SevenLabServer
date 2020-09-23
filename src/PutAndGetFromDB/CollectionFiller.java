package PutAndGetFromDB;

import DBWorker.Primal_Query_Makers;
import StartClasses.Ticket;
import StartClasses.TicketCollection;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Класс, предназначенный для обновления состояния коллекции в соответствии
 * с Базой Данных
 */

public class CollectionFiller {
    public static void updateCollectionFromDB(){
        try {
            ResultSet resultSet = Primal_Query_Makers.queryWithIncludedValues("SELECT * FROM public.tickets");
            while (resultSet.next()){
                Ticket t = TicketMakerFromDB.makeTicketFromDB(resultSet);
                TicketCollection.addTicket(t, Long.parseLong(t.getId().toString()));
            }
        } catch (SQLException e){
           System.out.println("Ошибка при выполнении метода updateCollectionFromDB!");
           System.out.println("Коллекция не была обновлена!");
        }
    }
}
