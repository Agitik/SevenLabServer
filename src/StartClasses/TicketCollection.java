package StartClasses;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс для хранения самой коллекции
 * @author Дмитрий Толочек P3130
 * @version 1.0 Before Check
 */

public class TicketCollection {
    static HashMap<Long, Ticket> tickets = new HashMap<>();

    public static LocalDate makingDay;

    public static void addTicket(Ticket ticket, Long key){
        ReentrantLock locker = new ReentrantLock();
        locker.lock();
        tickets.put(key, ticket);
        locker.unlock();
    }

    public static void delTicket(Long key){
        ReentrantLock locker = new ReentrantLock();
        locker.lock();
        tickets.remove(key);
        locker.unlock();
    }

    public static void clearTickets(){
        ReentrantLock locker = new ReentrantLock();
        locker.lock();
        tickets.clear();
        locker.unlock();
    }

    public static void replaceTicket(Ticket newTicket, Long key){
        ReentrantLock locker = new ReentrantLock();
        locker.lock();
        tickets.replace(key, newTicket);
        locker.unlock();
    }

    public static HashMap<Long, Ticket> getCopy(){
        ReentrantLock locker = new ReentrantLock();
        locker.lock();
        HashMap<Long, Ticket> nt = new HashMap<>(tickets);
        locker.unlock();
        return nt;
    }

    public static int getCollectionSize(){
        ReentrantLock locker = new ReentrantLock();
        locker.lock();
        int size = tickets.size();
        locker.unlock();
        return size;
    }

    public static Ticket getTicketByKey(Long key){
        ReentrantLock locker = new ReentrantLock();
        locker.lock();
        Ticket t = tickets.get(key);
        locker.unlock();
        return t;
    }
}
