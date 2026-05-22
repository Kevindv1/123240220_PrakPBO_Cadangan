/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurant.model;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Order {
    private String orderId;
    private String customerName;
    private List<MenuItem> items;
    private OrderStatus status;
    private Date orderTime;
    private Date completedTime;
    
    public enum OrderStatus {
        PENDING("⏳ Menunggu"),
        COOKING("👨‍🍳 Dimasak"),
        READY("✅ Siap"),
        COMPLETED("🎉 Selesai"),
        CANCELLED("❌ Dibatalkan");
        
        private String displayName;
        
        OrderStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public Order(String orderId, String customerName) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = new CopyOnWriteArrayList<>();
        this.status = OrderStatus.PENDING;
        this.orderTime = new Date();
    }
    
    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public List<MenuItem> getItems() { return items; }
    public OrderStatus getStatus() { return status; }
    public Date getOrderTime() { return orderTime; }
    public Date getCompletedTime() { return completedTime; }
    
    public void setStatus(OrderStatus status) { this.status = status; }
    public void setCompletedTime(Date completedTime) { this.completedTime = completedTime; }
    
    public void addItem(MenuItem item) {
        items.add(item);
    }
    
    public double calculateTotal() {
        return items.stream().mapToDouble(MenuItem::getPrice).sum();
    }
    
    public int getTotalPreparationTime() {
        return items.stream().mapToInt(MenuItem::getPreparationTime).max().orElse(0);
    }
    
    public String getFormattedOrderTime() {
        return String.format("%tH:%tM:%tS", orderTime, orderTime, orderTime);
    }
    
    public String getFormattedCompletedTime() {
        if (completedTime == null) return "-";
        return String.format("%tH:%tM:%tS", completedTime, completedTime, completedTime);
    }
}
