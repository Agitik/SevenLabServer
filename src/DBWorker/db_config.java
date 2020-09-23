package DBWorker;

/**
 * Класс, содержащий информацию для подключения к Базе Данных.
 * @author Дмитрий Толочек P3130
 * @version 1.0 After 6 lab check.
 */

public class db_config {
    public static String DB_DRIVER = "org.postgresql.Driver";
    public static String DB_CONNECTION = "";
    public static String DB_USER = "";
    public static String DB_PASSWORD = "";

    //Константные значения
    public static String DB_CONNECTION_SE = "jdbc:postgresql://pg:5432/studs";
    public static String DB_USER_SE = "s251093";
    public static String DB_PASSWORD_SE = "skf359";
    public static String DB_CONNECTION_LOCAL = "jdbc:postgresql://localhost:5432/testOne";
    public static String DB_USER_LOCAL = "postgres";
    public static String DB_PASSWORD_LOCAL = "dimon2001";

    public static void setDataBase(String db){
        if(db.equals("localhost")){
            DB_CONNECTION = DB_CONNECTION_LOCAL;
            DB_USER = DB_USER_LOCAL;
            DB_PASSWORD = DB_PASSWORD_LOCAL;
        } else if(db.equals("se.ifmo.ru")){
            DB_CONNECTION = DB_CONNECTION_SE;
            DB_USER = DB_USER_SE;
            DB_PASSWORD = DB_PASSWORD_SE;
        }
    }

}
