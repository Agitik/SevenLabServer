package TESTING;

import DBWorker.Primal_Query_Makers;
import DBWorker.db_config;

import java.sql.SQLException;

public class CreateTable {
    public static void main(String[] args) {
        try {
            db_config.setDataBase("localhost");
            Primal_Query_Makers.queryWithoutValues(
                            "CREATE TABLE tickets("
                            + "id SERIAL, "
                            + "ticket_name TEXT NOT NULL, "
                            + "xCord INTEGER NOT NULL, "
                            + "yCord INTEGER NOT NULL, "
                            + "creationDate DATE NOT NULL, "
                            + "price BIGINT NOT NULL, "
                            + "refundable BOOLEAN, "
                            + "ticketType TEXT NOT NULL, "
                            + "is_have_person BOOLEAN NOT NULL, "
                            + "person_weight REAL, "
                            + "eye_color TEXT, "
                            + "hair_color TEXT, "
                            + "nationality TEXT, "
                            + "xLoc BIGINT, "
                            + "yLoc REAL, "
                            + "locName TEXT,"
                            + "owner_login TEXT NOT NULL"
                            + ")"
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
