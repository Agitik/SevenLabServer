package PutAndGetFromDB;

import Accounter.User;
import DBWorker.Primal_Query_Makers;
import StartClasses.Ticket;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class BaseTicketBDCommands {
    public static boolean clearTicketsTable(User owner){
        try {
            return Primal_Query_Makers.queryWithoutValues(("DELETE FROM tickets WHERE owner_login = '" + owner.login + "';"));
        } catch (SQLException throwables) {
            return false;
        }
    }

    public static boolean addTicketInTable(User owner, Ticket ticket){

        boolean ans = true;
            String is_person_real = ticket.getPerson() == null ? "false" : "true";
            String weight = ticket.getPerson() == null ? "null" : ticket.getPerson().getWeight().toString();
            String eye_color = ticket.getPerson() == null ? "null" : ticket.getPerson().getEyeColor().toString();
            String hair_color = ticket.getPerson() == null ? "null" : ticket.getPerson().getHairColor().toString();
            String nationality = ticket.getPerson() == null ? "null" : ticket.getPerson().getNationality().toString();
            String is_person_have_location = ticket.getPerson() == null ? "false" : ticket.getPerson().getLocation() == null ? "false" : "true";
            String xloc = is_person_have_location.equals("false") ? "null" : ticket.getPerson().getLocation().getX().toString();
            String yloc = is_person_have_location.equals("false") ? "null" : String.valueOf(ticket.getPerson().getLocation().getY());
            String nameloc = is_person_have_location.equals("false") ? "null" : String.valueOf(ticket.getPerson().getLocation().getName());

            try {
                Primal_Query_Makers.queryWithoutValues("INSERT INTO"
                        + " tickets("
                        + "ticket_name, "
                        + "xcord, "
                        + "ycord, "
                        + "creationdate, "
                        + "price, "
                        + "refundable, "
                        + "tickettype, "
                        + "is_have_person, "
                        + "person_weight, "
                        + "eye_color, "
                        + "hair_color, "
                        + "nationality, "
                        + "is_person_have_location, "
                        + "nameloc, "
                        + "xloc, "
                        + "yloc, "
                        + "owner_login)"
                        + " VALUES("
                        + "'" + ticket.getName() + "', "
                        + ticket.getCoordinates().getX() + ", "
                        + ticket.getCoordinates().getY() + ", "
                        + "'" + ticket.getCreationDate().format(DateTimeFormatter.ofPattern("y-M-d")) + "', "
                        + ticket.getPrice() + ", "
                        + ticket.getRefundable() + ", "
                        + "'" + ticket.getType() + "', "
                        + is_person_real + ", "
                        + weight + ", "
                        + "'" + eye_color + "', "
                        + "'" + hair_color + "', "
                        + "'" + nationality + "', "
                        + is_person_have_location + ", "
                        + "'" + nameloc + "', "
                        + xloc + ", "
                        + yloc + ", "
                        + "'" + owner.login + "')");
            } catch (SQLException e){
                e.printStackTrace();
                System.out.println("Ошибка добавления билета в коллекцию");
                ans = false;
            }
            return ans;
    }

    public static boolean updTicketInTable(User owner, Ticket ticket){
        boolean ans = true;
        String is_person_real = ticket.getPerson() == null ? "false" : "true";
        String weight = ticket.getPerson() == null ? "null" : ticket.getPerson().getWeight().toString();
        String eye_color = ticket.getPerson() == null ? "null" : ticket.getPerson().getEyeColor().toString();
        String hair_color = ticket.getPerson() == null ? "null" : ticket.getPerson().getHairColor().toString();
        String nationality = ticket.getPerson() == null ? "null" : ticket.getPerson().getNationality().toString();
        String is_person_have_location = ticket.getPerson() == null ? "false" : ticket.getPerson().getLocation() == null ? "false" : "true";
        String xloc = is_person_have_location.equals("false") ? "null" : ticket.getPerson().getLocation().getX().toString();
        String yloc = is_person_have_location.equals("false") ? "null" : String.valueOf(ticket.getPerson().getLocation().getY());
        String nameloc = is_person_have_location.equals("false") ? "null" : String.valueOf(ticket.getPerson().getLocation().getName());
        try {
            Primal_Query_Makers.queryWithoutValues("UPDATE tickets SET ("
                    + "ticket_name, "
                    + "xcord, "
                    + "ycord, "
                    + "creationdate, "
                    + "price, "
                    + "refundable, "
                    + "tickettype, "
                    + "is_have_person, "
                    + "person_weight, "
                    + "eye_color, "
                    + "hair_color, "
                    + "nationality, "
                    + "is_person_have_location, "
                    + "nameloc, "
                    + "xloc, "
                    + "yloc, "
                    + "owner_login) = ("
                    + "'" + ticket.getName() + "', "
                    + ticket.getCoordinates().getX() + ", "
                    + ticket.getCoordinates().getY() + ", "
                    + "'" + ticket.getCreationDate().format(DateTimeFormatter.ofPattern("y-M-d")) + "', "
                    + ticket.getPrice() + ", "
                    + ticket.getRefundable() + ", "
                    + "'" + ticket.getType() + "', "
                    + is_person_real + ", "
                    + weight + ", "
                    + "'" + eye_color + "', "
                    + "'" + hair_color + "', "
                    + "'" + nationality + "', "
                    + is_person_have_location + ", "
                    + "'" + nameloc + "', "
                    + xloc + ", "
                    + yloc + ", "
                    + "'" + owner.login + "') WHERE id = " + ticket.getId()
                    + " AND " + "owner_login = '" + owner.login + "'");
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Ошибка обновления");
            ans = false;
        }
        return ans;
    }

    public static boolean delCurrentTicket(User owner, Long id){
        try {
            return Primal_Query_Makers.queryWithoutValues(("DELETE FROM tickets WHERE owner_login = '" + owner.login + "' AND id = " + id + ";"));
        } catch (SQLException throwables) {
            return false;
        }
    }
}
