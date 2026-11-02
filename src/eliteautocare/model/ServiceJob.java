package eliteautocare.model;

import java.time.LocalDate;
import java.text.NumberFormat;
import java.util.Locale;

public class ServiceJob {
    private int jobId;
    private String plateNumber;
    private String serviceType;
    private String status;
    private double cost;
    private LocalDate date;

    public ServiceJob(int jobId, String plateNumber, String serviceType,
                      String status, double cost, LocalDate date) {
        this.jobId = jobId;
        this.plateNumber = plateNumber;
        this.serviceType = serviceType;
        this.status = status;
        this.cost = cost;
        this.date = date;
    }

    public int getJobId()            { return jobId; }
    public String getPlateNumber()   { return plateNumber; }
    public String getServiceType()   { return serviceType; }
    public String getStatus()        { return status; }
    public double getCost()          { return cost; }
    public LocalDate getDate()       { return date; }

    public void setStatus(String status) { this.status = status; }
    public void setCost(double cost)     { this.cost = cost; }

    // Helper method to format money as UGX (e.g., UGX 250,000)
    public String getFormattedCost() {
        NumberFormat ugxFormat = NumberFormat.getCurrencyInstance(new Locale("en", "UG"));
        // Return with UGX prefix and no decimals
        return "UGX " + String.format("%,.0f", cost); 
    }
}