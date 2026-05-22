/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurant.controller;

import restaurant.model.*;
import restaurant.view.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main Controller - Mengelola interaksi antara Model dan View
 */
public class RestaurantController {
    private RestaurantModel model;
    private RestaurantView view;
    private OrderController orderController;
    private KitchenController kitchenController;
    private Thread kitchenThread;
    
    public RestaurantController() {
        this.model = new RestaurantModel();
        this.view = new RestaurantView();
        this.orderController = new OrderController(model, view);
        this.kitchenController = new KitchenController(model, view);
        
        // Set model untuk history panel
        view.getHistoryPanel().setModel(model);
        
        initEventHandlers();
        setupWindowListener();
    }
    
    private void initEventHandlers() {
        // Order button handler
        view.getOrderPanel().getOrderButton().addActionListener(e -> placeOrder());
        
        // Clear cart button handler
        view.getOrderPanel().getClearButton().addActionListener(e -> {
            view.getOrderPanel().clearCart();
            view.setStatusMessage("🛒 Keranjang dikosongkan");
        });
        
        // Tab change listener untuk refresh history
        view.addTabChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (view.getSelectedIndex() == 2) { // History tab
                    refreshHistory();
                    view.setStatusMessage("📊 Memuat riwayat pesanan...");
                } else if (view.getSelectedIndex() == 1) { // Kitchen tab
                    updateKitchenDisplay();
                }
            }
        });
        
        // Setup menu display
        setupMenuDisplay();
    }
    
    private void setupWindowListener() {
        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                shutdown();
            }
        });
    }
    
    private void setupMenuDisplay() {
        // Set menu ke order panel
        view.getOrderPanel().setMenuList(model.getAllMenu());
    }
    
    private void placeOrder() {
        String customerName = view.getOrderPanel().getCustomerName();
        java.util.List<MenuItem> cartItems = view.getOrderPanel().getCartItems();
        
        // Validasi input
        if (customerName.isEmpty()) {
            view.showMessage("Error", "Masukkan nama customer terlebih dahulu!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (cartItems.isEmpty()) {
            view.showMessage("Error", "Pilih menu terlebih dahulu!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validasi nama customer (tidak boleh hanya spasi)
        if (customerName.trim().isEmpty()) {
            view.showMessage("Error", "Nama customer tidak boleh kosong!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create order
        Order order = model.createOrder(customerName, cartItems);
        
        if (order != null) {
            // Add to queue
            model.addOrderToQueue(order);
            
            // Reset form
            view.getOrderPanel().resetForm();
            
            // Update status
            view.setStatusMessage("✅ Pesanan " + order.getOrderId() + " berhasil dibuat dan masuk antrian dapur");
            
            // Show success message
            view.showMessage("Sukses", 
                "Pesanan berhasil dibuat!\n\n" +
                "Order ID: " + order.getOrderId() + "\n" +
                "Customer: " + customerName + "\n" +
                "Total: Rp" + String.format("%,.0f", order.calculateTotal()) + "\n\n" +
                "Mohon tunggu, pesanan sedang diproses.",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Refresh history dan kitchen display
            refreshHistory();
            updateKitchenDisplay();
        } else {
            view.showMessage("Error", "Gagal membuat pesanan!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void refreshHistory() {
        view.getHistoryPanel().refreshOrderList(model.getAllOrders());
    }
    
    private void updateKitchenDisplay() {
        // Update queue size di kitchen panel
        int queueSize = model.getOrderQueue().size();
        view.getKitchenPanel().updateQueueSize(queueSize);
    }
    
    public void start() {
        // Start kitchen thread
        kitchenThread = new Thread(kitchenController, "Kitchen-Thread");
        kitchenThread.start();
        
        // Set initial status
        view.setStatusMessage("✅ Restoran siap melayani pesanan | Total menu: " + model.getAllMenu().size());
        
        // Initial refresh
        refreshHistory();
        
        // Start periodic updates
        startPeriodicUpdates();
        
        System.out.println("🚀 Restaurant Controller Started");
        System.out.println("📋 Total Menu: " + model.getAllMenu().size());
        System.out.println("👨‍🍳 Kitchen Thread: " + kitchenThread.getName());
    }
    
    private void startPeriodicUpdates() {
        // Timer untuk update status secara periodik
        Timer timer = new Timer(3000, e -> {
            updateKitchenDisplay();
            // Update active orders in kitchen panel
            view.getKitchenPanel().updateQueueSize(model.getOrderQueue().size());
        });
        timer.start();
    }
    
    public void shutdown() {
        System.out.println("🛑 Shutting down Restaurant Controller...");
        
        // Stop kitchen controller
        kitchenController.stop();
        
        // Wait for kitchen thread to finish
        try {
            if (kitchenThread != null && kitchenThread.isAlive()) {
                kitchenThread.join(3000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Close view
        view.setStatusMessage("🏪 Restoran tutup, terima kasih!");
        view.dispose();
        
        System.out.println("✅ Restaurant Controller shutdown complete");
    }
}