package project.grinder.model;

public class Order {
    
    private String item;
    private int amount;
    private double price;

    public Order(String item, int amount, double price) {
        this.item = item;
        this.amount = amount;
        this.price = price;
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
}