/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurant.controller;

import restaurant.model.*;
import restaurant.view.*;
import java.util.concurrent.*;

/**
 * Interface untuk item yang bisa dimasak
 * (Polymorphism)
 */
interface Cookable {
    void cook() throws InterruptedException;
}

/**
 * Adapter untuk membuat MenuItem menjadi Cookable
 */
class CookableAdapter implements Cookable {
    private MenuItem item;
    private KitchenPanel view;
    
    public CookableAdapter(MenuItem item, KitchenPanel view) {
        this.item = item;
        this.view = view;
    }
    
    @Override
    public void cook() throws InterruptedException {
        // Simulasi proses memasak
        view.addLog("🔪 Memasak " + item.getName() + " (" + item.getCategory() + ")");
        Thread.sleep(item.getPreparationTime() * 1000L);
        view.addLog("✅ " + item.getName() + " selesai dimasak!");
    }
}

/**
 * Kitchen Controller - Mengelola logika dapur dengan multithreading
 */
public class KitchenController implements Runnable {
    private RestaurantModel model;
    private RestaurantView view;
    private boolean isRunning;
    private ExecutorService cookingExecutor;
    private ScheduledExecutorService scheduler;
    
    public KitchenController(RestaurantModel model, RestaurantView view) {
        this.model = model;
        this.view = view;
        this.isRunning = true;
        this.cookingExecutor = Executors.newFixedThreadPool(3); // 3 koki parallel
        this.scheduler = Executors.newScheduledThreadPool(1);
    }
    
    @Override
    public void run() {
        view.getKitchenPanel().addLog("👨‍🍳 Dapur mulai beroperasi...");
        view.getKitchenPanel().addLog("🔥 " + Runtime.getRuntime().availableProcessors() + " core processor tersedia");
        view.getKitchenPanel().setKitchenStatus(true);
        
        // Schedule periodic queue status update
        scheduler.scheduleAtFixedRate(() -> {
            if (isRunning) {
                int queueSize = model.getOrderQueue().size();
                SwingUtilities.invokeLater(() -> {
                    view.getKitchenPanel().updateQueueSize(queueSize);
                });
            }
        }, 0, 1, TimeUnit.SECONDS);
        
        // Main loop untuk memproses order
        while (isRunning) {
            try {
                // Poll order dari queue dengan timeout
                Order order = model.getOrderQueue().poll(2, TimeUnit.SECONDS);
                
                if (order != null) {
                    processOrder(order);
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        // Shutdown executors
        cookingExecutor.shutdown();
        scheduler.shutdown();
        
        view.getKitchenPanel().addLog("🛑 Dapur berhenti beroperasi");
        view.getKitchenPanel().setKitchenStatus(false);
    }
    
    /**
     * Memproses satu order (Multithreading untuk item-item di dalamnya)
     */
    private void processOrder(Order order) {
        // Update status menjadi COOKING
        model.updateOrderStatus(order.getOrderId(), Order.OrderStatus.COOKING);
        view.getKitchenPanel().updateOrderStatus(order.getOrderId(), Order.OrderStatus.COOKING);
        
        SwingUtilities.invokeLater(() -> {
            view.getKitchenPanel().addLog("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            view.getKitchenPanel().addLog("📦 Memproses Order: " + order.getOrderId());
            view.getKitchenPanel().addLog("👤 Customer: " + order.getCustomerName());
            view.getKitchenPanel().addLog("📋 Jumlah item: " + order.getItems().size());
            view.getKitchenPanel().addOrderToKitchen(order);
        });
        
        // Polymorphism: Setiap item dimasak secara parallel
        CompletableFuture<?>[] cookingTasks = order.getItems().stream()
            .map(item -> new CookableAdapter(item, view.getKitchenPanel()))
            .map(cookable -> CompletableFuture.runAsync(() -> {
                try {
                    cookable.cook();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    view.getKitchenPanel().addLog("❌ Gagal memasak item!");
                }
            }, cookingExecutor))
            .toArray(CompletableFuture[]::new);
        
        // Tunggu semua item selesai dimasak
        try {
            CompletableFuture.allOf(cookingTasks).get(30, TimeUnit.SECONDS);
            
            // Update status menjadi READY
            model.updateOrderStatus(order.getOrderId(), Order.OrderStatus.READY);
            view.getKitchenPanel().updateOrderStatus(order.getOrderId(), Order.OrderStatus.READY);
            
            SwingUtilities.invokeLater(() -> {
                view.getKitchenPanel().addLog("🎉 Order " + order.getOrderId() + " selesai dimasak!");
                view.getKitchenPanel().addLog("📢 Panggilan untuk: " + order.getCustomerName());
                view.getKitchenPanel().addLog("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            });
            
            // Simulasi menunggu diambil customer (5 detik)
            Thread.sleep(5000);
            
            // Update status menjadi COMPLETED
            model.updateOrderStatus(order.getOrderId(), Order.OrderStatus.COMPLETED);
            view.getKitchenPanel().updateOrderStatus(order.getOrderId(), Order.OrderStatus.COMPLETED);
            
            // Hapus dari active orders setelah selesai
            view.getKitchenPanel().removeOrder(order.getOrderId());
            
            SwingUtilities.invokeLater(() -> {
                view.getKitchenPanel().addLog("✅ Order " + order.getOrderId() + " telah diambil customer");
                view.getHistoryPanel().refreshOrderList(model.getAllOrders());
            });
            
        } catch (Exception e) {
            view.getKitchenPanel().addLog("❌ Error memproses order " + order.getOrderId() + ": " + e.getMessage());
            model.updateOrderStatus(order.getOrderId(), Order.OrderStatus.CANCELLED);
        }
    }
    
    /**
     * Menghentikan kitchen controller
     */
    public void stop() {
        isRunning = false;
        cookingExecutor.shutdownNow();
        scheduler.shutdownNow();
        view.getKitchenPanel().addLog("🛑 Menghentikan dapur...");
    }
    
    /**
     * Mendapatkan status dapur
     */
    public boolean isRunning() {
        return isRunning;
    }
    
    /**
     * Mendapatkan jumlah antrian
     */
    public int getQueueSize() {
        return model.getOrderQueue().size();
    }
    
    /**
     * Mendapatkan jumlah koki aktif
     */
    public int getActiveChefs() {
        return ((ThreadPoolExecutor) cookingExecutor).getActiveCount();
    }
}
