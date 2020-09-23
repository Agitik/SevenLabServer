package DBWorker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static DBWorker.db_config.*;

/**
 * Класс для создания примитивных запросов к базе данных.
 * На его основе будут делаться другие классы для работы с БД.
 * @author Дмитрий Толочек P3130
 * @version 1.0 After 6 lab check.
 */

public class Primal_Query_Makers {

    /**
     * Метод для создания запросов, которые не требуют возвращения значение.
     * Пишет в консоль информативное сообщение о выполнении запроса.
     * @param query SQL запрос
     * @throws SQLException ошибка на стороне SQL
     */
    public static synchronized boolean queryWithoutValues(String query) throws SQLException {
        Connection connection = getDBConnection();
        boolean status = connection.createStatement().execute(query);
        System.out.println("Запрос выполнен успешно!");
        connection.close();
        return status;
    }

    /**
     * Метод для создания запросов, которые требуют возвращение значение.
     * @param query SQL запрос
     * @return значение запроса после выполнения
     * @throws SQLException ошибка на стороне SQL.
     */
    public static synchronized ResultSet queryWithIncludedValues(String query) throws SQLException {
        Connection connection = getDBConnection();
        ResultSet resultSet = connection.createStatement().executeQuery(query);
        connection.close();
        return resultSet;
    }

    private static synchronized Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }
}