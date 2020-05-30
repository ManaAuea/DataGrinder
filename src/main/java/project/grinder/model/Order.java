package project.grinder.model;

import java.time.LocalDate;

public class Order {
    
    private String item;
    private int amount;
    private double price;
    private LocalDate date;

    public Order(String item, int amount, double price, LocalDate date) {
        this.item = item;
        this.amount = amount;
        this.price = price;
        this.date = date;
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

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}