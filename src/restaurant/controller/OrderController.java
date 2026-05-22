/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurant.controller;

import restaurant.model.*;
import restaurant.view.*;
import javax.swing.*;
import java.util.List;

/**
 * Order Controller - Mengelola logika pemesanan
 */
public class OrderController {
    private RestaurantModel model;
    private RestaurantView view;
    
    public OrderController(RestaurantModel model, RestaurantView view) {
        this.model = model;
        this.view = view;
    }
    
    /**
     * Membuat pesanan baru
     */
    public Order createOrder(String customerName, List<MenuItem> items) {
        if (customerName == null || customerName.trim().isEmpty()) {
            view.showMessage("Error", "Nama customer tidak boleh kosong!", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        if (items == null || items.isEmpty()) {
            view.showMessage("Error", "Pilih menu terlebih dahulu!", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        Order order = model.createOrder(customerName, items);
        
        if (order != null) {
            view.setStatusMessage("✅ Pesanan " + order.getOrderId() + " berhasil dibuat");
        }
        
        return order;
    }
    
    /**
     * Mendapatkan detail pesanan
     */
    public Order getOrderDetail(String orderId) {
        Order order = model.getOrder(orderId);
        if (order == null) {
            view.showMessage("Error", "Order ID tidak ditemukan!", JOptionPane.ERROR_MESSAGE);
        }
        return order;
    }
    
    /**
     * Membatalkan pesanan
     */
    public boolean cancelOrder(String orderId) {
        Order order = model.getOrder(orderId);
        
        if (order == null) {
            view.showMessage("Error", "Order ID tidak ditemukan!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (order.getStatus() == Order.OrderStatus.COMPLETED) {
            view.showMessage("Error", "Pesanan sudah selesai, tidak dapat dibatalkan!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (order.getStatus() == Order.OrderStatus.COOKING) {
            view.showMessage("Error", "Pesanan sedang dimasak, tidak dapat dibatalkan!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        model.updateOrderStatus(orderId, Order.OrderStatus.CANCELLED);
        view.setStatusMessage("❌ Pesanan " + orderId + " telah dibatalkan");
        view.showMessage("Info", "Pesanan " + orderId + " berhasil dibatalkan", JOptionPane.INFORMATION_MESSAGE);
        
        return true;
    }
    
    /**
     * Mendapatkan semua pesanan aktif
     */
    public List<Order> getActiveOrders() {
        return model.getActiveOrders();
    }
    
    /**
     * Mendapatkan semua pesanan
     */
    public List<Order> getAllOrders() {
        return model.getAllOrders();
    }
    
    /**
     * Menghitung total pendapatan
     */
    public double getTotalRevenue() {
        double total = 0;
        for (Order order : model.getAllOrders()) {
            if (order.getStatus() == Order.OrderStatus.COMPLETED) {
                total += order.calculateTotal();
            }
        }
        return total;
    }
    
    /**
     * Mendapatkan statistik pesanan
     */
    public String getOrderStatistics() {
        int total = model.getAllOrders().size();
        int completed = 0;
        int cooking = 0;
        int pending = 0;
        int cancelled = 0;
        
        for (Order order : model.getAllOrders()) {
            switch (order.getStatus()) {
                case COMPLETED:
                    completed++;
                    break;
                case COOKING:
                    cooking++;
                    break;
                case PENDING:
                    pending++;
                    break;
                case CANCELLED:
                    cancelled++;
                    break;
            }
        }
        
        return String.format(
            "Total Pesanan: %d\nSelesai: %d\nDimasak: %d\nMenunggu: %d\nDibatalkan: %d",
            total, completed, cooking, pending, cancelled
        );
    }
}