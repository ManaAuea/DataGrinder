package project.grinder.model;

public class Record {

    private String item;
    private int amount;
    private double price;
    private int id;
    private String name;
    private String phone;
    private String date;

    public Record() {}
    
    public Record(int id, String name, String phone) {
        this.item = null;
        this.amount = 0;
        this.price = 0.0;
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.date = null;
    }

    public Record(String item, int amount, double price, int id, String name, String phone) {
        this.item = item;
        this.amount = amount;
        this.price = price;
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public String getItem() {
        return this.item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}