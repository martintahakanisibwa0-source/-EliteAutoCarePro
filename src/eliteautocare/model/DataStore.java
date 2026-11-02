package eliteautocare.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    public static final List<Customer>   customers = new ArrayList<>();
    public static final List<Vehicle>    vehicles  = new ArrayList<>();
    public static final List<ServiceJob> jobs      = new ArrayList<>();

    private static int nextCustomerId = 1;
    private static int nextJobId      = 1001;

    static {
        customers.add(new Customer(nextCustomerId++, "John Mensah",  "0244000111", "john@mail.com"));
        customers.add(new Customer(nextCustomerId++, "Ama Boateng",  "0200999888", "ama@mail.com"));

        vehicles.add(new Vehicle("GT-1234-22", "Toyota", "Corolla", 2019, "John Mensah"));
        vehicles.add(new Vehicle("AS-5678-21", "Honda",  "Civic",   2020, "Ama Boateng"));

        jobs.add(new ServiceJob(nextJobId++, "GT-1234-22", "Oil Change",  "Completed",  250.0, LocalDate.now().minusDays(3)));
        jobs.add(new ServiceJob(nextJobId++, "AS-5678-21", "Brake Repair", "In Progress", 800.0, LocalDate.now()));
    }

    public static int getNextCustomerId() { return nextCustomerId++; }
    public static int getNextJobId()      { return nextJobId++; }
}