package NetworkExchange;

import DBWorker.Primal_Query_Makers;
import PutAndGetFromDB.BaseTicketBDCommands;
import PutAndGetFromDB.CollectionFiller;
import StartClasses.Person;
import StartClasses.Ticket;
import StartClasses.TicketCollection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class CommandExecuter {
    public static AnswerPack info(){
        AnswerPack ap = new AnswerPack();
        ap.answer.add("Тип коллекции: HashTable");
        ap.answer.add("Тип хранимых объектов: Tickets");
        ap.answer.add("Колличество элементов: " + TicketCollection.getCollectionSize());
        ap.answer.add("Дата инициализации коллекции: " + TicketCollection.makingDay);
        return ap;
    }

    public static AnswerPack show(){
        AnswerPack ap = new AnswerPack();
        Comparator<Ticket> AlpComp = ((o2, o1) -> {
            ArrayList<String> buf = new ArrayList<>();
            int ret;
            buf.add(o1.getName());
            buf.add(o2.getName());
            Collections.sort(buf);
            if (buf.get(0).equals(o1.getName())) {
                ret = 1;
            } else if (buf.get(0).equals(buf.get(1))) {
                ret = 0;
            } else {
                ret = -1;
            }
            return ret;
        });
        TicketCollection.getCopy().values().stream().sorted(AlpComp).forEach(ticket -> ap.answer.add(ticket.showInfo()));
        return ap;
    }

    public static AnswerPack clear(QuestionPack qp){
        AnswerPack ap = new AnswerPack();
        if(BaseTicketBDCommands.clearTicketsTable(qp.arg.user)){
            CollectionFiller.updateCollectionFromDB();
            ap.answer.add("Все объекты коллекции удалены.");
        }else{
            ap.answer.add("Команда выполнена");
        }
        return ap;
    }


    public static AnswerPack insert_null(QuestionPack pack){
        AnswerPack ap = new AnswerPack();
        boolean notConsist = TicketCollection.getCopy().keySet().stream()
                .noneMatch(x -> x.equals(pack.arg.key));
        if(!notConsist){
            ap.answer.add("Ошибка: Билет с таким key уже существует!");
        } else {
            Ticket nt = pack.arg.ticket;
            nt.setDefaultCreationDate();
            boolean done = BaseTicketBDCommands.addTicketInTable(pack.arg.user, nt);
            if(done){
                try {
                    ResultSet resultSet = Primal_Query_Makers.queryWithIncludedValues("SELECT max(id) FROM tickets");
                    resultSet.next();
                    nt.setId(Long.parseLong(resultSet.getString("max")));
                    TicketCollection.addTicket(nt, pack.arg.key);
                } catch (SQLException throwables) {
                    ap.answer.add("Ошибка: Билет не смог попасть в коллекцию!");
                }
                ap.answer.add("Элемент добавлен в коллекцию!");
            }else{
                ap.answer.add("Ошибка: Билет не смог попасть в БД!");
            }
        }
        return ap;
    }

    /**
     * Реализация команды "update_id"
     * @return ответ от сервера
     */
    public static AnswerPack update_id(QuestionPack pack){
        AnswerPack ap = new AnswerPack();
        boolean isIDreal = TicketCollection.getCopy().values().stream()
                .anyMatch(ticket -> ticket.getId().equals(pack.arg.id));
        if(isIDreal){
            Ticket t = pack.arg.ticket;
            t.setId(pack.arg.id);
            t.setDefaultCreationDate();
            if(BaseTicketBDCommands.updTicketInTable(pack.arg.user, pack.arg.ticket)){
                TicketCollection.replaceTicket(t, pack.arg.id);
            }
            ap.answer.add("Билет успешно изменен!");
        } else {
            ap.answer.add("Билета с таким ID не существует!");
            ap.answer.add("Используйте Show для просмотра всех элементов коллекции");
        }
        return ap;
    }

    /**
     * Реализация команды "remove_key"
     * @return ответ от сервера
     */
    public static AnswerPack remove_key(QuestionPack pack) {
        AnswerPack ap = new AnswerPack();
        boolean isIDreal = TicketCollection.getCopy().containsKey(pack.arg.key);
        if (isIDreal) {
            Ticket t = TicketCollection.getCopy().get(pack.arg.key);
            if (BaseTicketBDCommands.delCurrentTicket(pack.arg.user, t.getId())) {
                CollectionFiller.updateCollectionFromDB();
            }
            ap.answer.add("Команда выполнена!");
        } else {
            ap.answer.add("Ошибка выполнения");
        }
        return ap;
    }

    /**
     * Реализация команды "replace_if_greater"
     * @return ответ от сервера
     */
    public static AnswerPack replace_if_greater(QuestionPack pack){
        AnswerPack ap = new AnswerPack();
        Ticket rep = pack.arg.ticket;
        TicketCollection.getCopy().values().stream()
                .filter(ticket -> ticket.compareTo(rep) > 0)
                .forEach(ticket -> {
                    BaseTicketBDCommands.updTicketInTable(pack.arg.user, ticket);});
        CollectionFiller.updateCollectionFromDB();
        ap.answer.add("\nКоманда выполнена успешно!");
        return ap;
    }

    /**
     * Реализация команды "remove_greater_keys"
     * @return ответ от сервера
     */
    public static AnswerPack remove_greater_keys(QuestionPack pack){
        AnswerPack ap = new AnswerPack();
        Set<Long> res = TicketCollection.getCopy().keySet();
        res.forEach(r -> {
            if(r > pack.arg.key){
                BaseTicketBDCommands.delCurrentTicket(pack.arg.user, r);
            }
        });
        CollectionFiller.updateCollectionFromDB();
        ap.answer.add("\nКоманда выполнена!");
        return ap;
    }

    /**
     * Реализация команды "remove_lower_keys"
     * @return ответ от сервера
     */
    public static AnswerPack remove_lower_keys(QuestionPack pack){
        AnswerPack ap = new AnswerPack();
        Set<Long> res = TicketCollection.getCopy().keySet();
        res.forEach(r -> {
            if(r < pack.arg.key){
                BaseTicketBDCommands.delCurrentTicket(pack.arg.user, r);
            }
        });
        CollectionFiller.updateCollectionFromDB();
        ap.answer.add("\nКоманда выполнена!");
        return ap;
    }

    /**
     * Реализация команды "count_less_then_type"
     * @return ответ от сервера
     */
    public static AnswerPack count_less_then_type(QuestionPack pack){
        AnswerPack ap = new AnswerPack();
        int vt = pack.arg.type.ordinal();
        long rez = TicketCollection.getCopy().values().stream()
                .filter(ticket -> ticket.getType().ordinal() < vt)
                .count();
        ap.answer.add("\nResult: " + rez);
        return ap;
    }

    /**
     * Реализация команды "print_field_ascending_person"
     * @return ответ от сервера
     */
    public static AnswerPack print_field_ascending_person(){
        AnswerPack ap = new AnswerPack();
        List<Person> realPeople = new ArrayList<>();
        TicketCollection.getCopy().values().stream()
                .filter(ticket -> ticket.getPerson() != null)
                .forEach(ticket -> realPeople.add(ticket.getPerson()));
        realPeople.stream().sorted().forEach(person -> ap.answer.add("\n"+person.showInfo()+"\n"));
        return ap;
    }

    /**
     * Реализация команды "print_field_descending_type"
     * @return ответ от сервера
     */
    public static AnswerPack print_field_descending_type(){
        AnswerPack ap = new AnswerPack();
        Comparator<Ticket> comparator = (o1, o2) -> o2.getType().ordinal() - o1.getType().ordinal();
        List<Ticket> ticketTypeSorted = TicketCollection.getCopy().values().stream()
                .sorted(comparator).collect(Collectors.toList());
        for (Ticket t : ticketTypeSorted){
            ap.answer.add(t.getType().toString());
        }
        return ap;
    }

    /**
     * Реализация команды "execute_script"
     * @return ответ от сервера
     */
    public static AnswerPack execute_script(QuestionPack pack){
        AnswerPack ap = new AnswerPack();
        for (QuestionPack p : pack.arg.script) {
            AnswerPack asp = UnPacker.UnPackAndExecute(p);
            ap.answer.addAll(asp.answer);
        }
        return ap;
    }

    /**
     * Реализация команды "client_exit"
     * @return ответ от сервера
     */
    public static AnswerPack client_exit(){
        AnswerPack ap = new AnswerPack();
        ap.answer.add("Инициирован выход клиента.");
        return ap;
    }

    /**
     * Реализация команды login
     * @return ответ от сервера
     */
    public static AnswerPack login(QuestionPack pack){
        AnswerPack ap = new AnswerPack();
        try {
            ResultSet resultSet = Primal_Query_Makers.queryWithIncludedValues("SELECT * FROM users WHERE login = '" + pack.arg.user.login
                    + "' AND password = '" + pack.arg.user.password + "';");
            ap.status = resultSet.next();
        }catch (SQLException e){
            ap.answer.add("Ошибка запроса логина.");
        }
        return ap;
    }

    /**
     * Реализация команды registration
     * @return ответ от сервера
     */
    public static AnswerPack registration(QuestionPack pack){
        AnswerPack ap = new AnswerPack();
        try {
            Primal_Query_Makers.queryWithoutValues("INSERT INTO"
                    + " users("
                    + "login, "
                    + "password)"
                    + " VALUES('"
                    + pack.arg.user.login + "', "
                    + "'" + pack.arg.user.password + "')");
            ap.status = true;
            ap.answer.add("Аккаунт зарегестрирован.");
        }catch (SQLException e){
            ap.answer.add("Такой логин уже зарегистрирован!");
            ap.status = false;
        }
        return ap;
    }
}
