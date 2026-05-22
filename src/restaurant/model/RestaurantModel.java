/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurant.model;

import java.util.*;
import java.util.concurrent.*;

public class RestaurantModel {
    private List<MenuItem> menu;
    private Map<String, Order> orders;
    private BlockingQueue<Order> orderQueue;
    private int orderCounter;
    
    public RestaurantModel() {
        this.menu = new CopyOnWriteArrayList<>();
        this.orders = new ConcurrentHashMap<>();
        this.orderQueue = new LinkedBlockingQueue<>();
        this.orderCounter = 0;
        initializeMenu();
    }
    
    private void initializeMenu() {
        // Makanan
        menu.add(new Food("F001", "Nasi Goreng Spesial", 35000, 4, "Medium"));
        menu.add(new Food("F002", "Mie Ayam Bakso", 28000, 3, "Pedas"));
        menu.add(new Food("F003", "Sate Ayam Madura", 40000, 5, "Tidak Pedas"));
        menu.add(new Food("F004", "Rendang Padang", 45000, 6, "Pedas"));
        menu.add(new Food("F005", "Gado-Gado", 25000, 3, "Tidak Pedas"));
        
        // Minuman
        menu.add(new Drink("D001", "Es Teh Manis", 8000, 1, true));
        menu.add(new Drink("D002", "Jus Jeruk Segar", 15000, 2, true));
        menu.add(new Drink("D003", "Kopi Hitam", 12000, 2, false));
        menu.add(new Drink("D004", "Es Cincau", 10000, 1, true));
        menu.add(new Drink("D005", "Milkshake Coklat", 18000, 3, true));
        
        // Dessert
        menu.add(new Dessert("S001", "Puding Coklat", 18000, 2, "Coklat Chip"));
        menu.add(new Dessert("S002", "Es Krim Vanilla", 15000, 1, "Sprinkles"));
        menu.add(new Dessert("S003", "Pancake Madu", 22000, 4, "Madu"));
    }
    
    public List<MenuItem> getAllMenu() {
        return new ArrayList<>(menu);
    }
    
    public List<MenuItem> getMenuByCategory(String category) {
        List<MenuItem> result = new ArrayList<>();
        for (MenuItem item : menu) {
            if (item.getCategory().equals(category)) {
                result.add(item);
            }
        }
        return result;
    }
    
    public MenuItem getMenuItemById(String id) {
        for (MenuItem item : menu) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }
    
    public Order createOrder(String customerName, List<MenuItem> selectedItems) {
        String orderId = generateOrderId();
        Order order = new Order(orderId, customerName);
        
        for (MenuItem item : selectedItems) {
            order.addItem(item);
        }
        
        if (!order.getItems().isEmpty()) {
            orders.put(orderId, order);
            return order;
        }
        return null;
    }
    
    private String generateOrderId() {
        return "ORD" + String.format("%04d", ++orderCounter);
    }
    
    public void addOrderToQueue(Order order) {
        try {
            orderQueue.put(order);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public BlockingQueue<Order> getOrderQueue() {
        return orderQueue;
    }
    
    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }
    
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }
    
    public void updateOrderStatus(String orderId, Order.OrderStatus status) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setStatus(status);
            if (status == Order.OrderStatus.COMPLETED) {
                order.setCompletedTime(new Date());
            }
        }
    }
    
    public List<Order> getActiveOrders() {
        List<Order> active = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getStatus() != Order.OrderStatus.COMPLETED &&
                order.getStatus() != Order.OrderStatus.CANCELLED) {
                active.add(order);
            }
        }
        return active;
    }
}