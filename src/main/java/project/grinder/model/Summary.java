package project.grinder.model;

import java.util.List;

public class Summary {
    
    private User info;
    private List<Order> order;
    private double totalAmount;

    public Summary(User info, List<Order> order, double totalAmount) {
        this.info = info;
        this.order = order;
        this.totalAmount = totalAmount;
    }

    public User getInfo() {
        return this.info;
    }

    public void setInfo(User info) {
        this.info = info;
    }

    public List<Order> getOrder() {
        return this.order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }

    public double getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}