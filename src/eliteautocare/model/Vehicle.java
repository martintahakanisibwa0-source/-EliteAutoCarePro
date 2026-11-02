package eliteautocare.model;

public class Vehicle {
    private String plateNumber;
    private String make;
    private String model;
    private int year;
    private String ownerName;

    public Vehicle(String plateNumber, String make, String model, int year, String ownerName) {
        this.plateNumber = plateNumber;
        this.make = make;
        this.model = model;
        this.year = year;
        this.ownerName = ownerName;
    }

    public String getPlateNumber() { return plateNumber; }
    public String getMake()        { return make; }
    public String getModel()       { return model; }
    public int getYear()           { return year; }
    public String getOwnerName()   { return ownerName; }

    public void setMake(String make)             { this.make = make; }
    public void setModel(String model)           { this.model = model; }
    public void setYear(int year)                { this.year = year; }
    public void setOwnerName(String ownerName)   { this.ownerName = ownerName; }
}