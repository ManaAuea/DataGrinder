package project.grinder.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Summary {
    
    private User info;
    private Map<LocalDate, List<Order>> order;
    private double totalAmount;

    public Summary(User info, Map<LocalDate, List<Order>> order, double totalAmount) {
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

    public Map<LocalDate, List<Order>> getOrder() {
        return this.order;
    }

    public void setOrder(Map<LocalDate, List<Order>> order) {
        this.order = order;
    }

    public double getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}