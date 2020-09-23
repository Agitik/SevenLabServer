package PutAndGetFromDB;

import Accounter.User;
import StartClasses.*;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Класс для создания билета из базы данных из конкретного ResultSet
 * @author Дмитрий Толочек P3130
 * @version 1.0 Before Check
 */

public class TicketMakerFromDB {
    /**
     * Метод, который создает билет из базы данных
     * @param resultSet результат запроса к бд
     * @return билет
     * @throws SQLException ошибка SQL
     */
    public static Ticket makeTicketFromDB(ResultSet resultSet) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(resultSet.getLong("id"));
        ticket.setName(resultSet.getString("ticket_name"));
        ticket.setCoordinates(makeCoordinatesFromDB(resultSet));
        Date date = resultSet.getDate("creationdate");
        ticket.setCreationDate(date.toLocalDate());
        ticket.setPrice(resultSet.getFloat("price"));
        ticket.setRefundable(resultSet.getBoolean("refundable"));
        ticket.setType(TicketType.getType(resultSet.getString("tickettype")));
        ticket.setPerson(makePersonFromDB(resultSet));
        ticket.owner = new User();
        ticket.owner.login = resultSet.getString("owner_login");
        return ticket;
    }

    /**
     * Метод, который создает человека из базы данных
     * @param resultSet результат запроса к бд
     * @return человек
     * @throws SQLException ошибка SQL
     */
    public static Person makePersonFromDB(ResultSet resultSet) throws SQLException {
        Person person = new Person();
        if(resultSet.getBoolean("is_have_person")){
            person.setWeight(resultSet.getDouble("person_weight"));
            person.setEyeColor(Color.getType(resultSet.getString("eye_color")));
            person.setHairColor(Color.getType(resultSet.getString("hair_color")));
            person.setNationality(Country.getType(resultSet.getString("nationality")));
            person.setLocation(makeLocationFromDB(resultSet));
        }else{
            person = null;
        }
        return person;
    }

    /**
     * Метод, который создает координаты из базы данных
     * @param resultSet результат запроса к бд
     * @return координаты
     * @throws SQLException ошибка SQL
     */
    public static Coordinates makeCoordinatesFromDB(ResultSet resultSet) throws SQLException {
        Coordinates coordinates = new Coordinates();
        coordinates.setX(resultSet.getInt("xcord"));
        coordinates.setY(resultSet.getInt("ycord"));
        return coordinates;
    }

    /**
     * Метод, который создает локацию из базы данных
     * @param resultSet результат запроса к бд
     * @return локация
     * @throws SQLException ошибка SQL
     */
    public static Location makeLocationFromDB(ResultSet resultSet) throws SQLException {
        Location location = new Location();
        if(resultSet.getBoolean("is_person_have_location")){
            location.setName(resultSet.getString("nameloc"));
            location.setX(resultSet.getLong("xloc"));
            location.setY(resultSet.getInt("yloc"));
        }else{
            location = null;
        }
        return location;
    }
}
