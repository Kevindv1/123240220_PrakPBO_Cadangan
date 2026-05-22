/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurant.view;

import restaurant.model.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;

public class RestaurantView extends JFrame {
    private JTabbedPane tabbedPane;
    private OrderPanel orderPanel;
    private KitchenPanel kitchenPanel;
    private OrderHistoryPanel historyPanel;
    private JLabel statusBar;
    private Timer timeTimer;
    
    public RestaurantView() {
        initUI();
    }
    
    private void initUI() {
        setTitle("🏪 Restaurant Management System - Makan Enak");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        orderPanel = new OrderPanel();
        kitchenPanel = new KitchenPanel();
        historyPanel = new OrderHistoryPanel();
        
        tabbedPane.addTab("📝 Pemesanan", orderPanel);
        tabbedPane.addTab("👨‍🍳 Dapur", kitchenPanel);
        tabbedPane.addTab("📊 Riwayat Pesanan", historyPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Status Bar
        statusBar = new JLabel("✅ Restoran siap melayani");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(statusBar, BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel();
        header.setBackground(new Color(46, 204, 113));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        header.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("MAKAN ENAK RESTAURANT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Sistem Pemesanan & Manajemen Dapur Online");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);
        
        header.add(textPanel, BorderLayout.WEST);
        
        JLabel timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        timeLabel.setForeground(Color.WHITE);
        updateTime(timeLabel);
        
        timeTimer = new Timer(1000, e -> updateTime(timeLabel));
        timeTimer.start();
        
        header.add(timeLabel, BorderLayout.EAST);
        
        return header;
    }
    
    private void updateTime(JLabel label) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss - EEE, dd MMM yyyy");
        label.setText("🕐 " + sdf.format(new java.util.Date()));
    }
    
    public OrderPanel getOrderPanel() { return orderPanel; }
    public KitchenPanel getKitchenPanel() { return kitchenPanel; }
    public OrderHistoryPanel getHistoryPanel() { return historyPanel; }
    public int getSelectedIndex() { return tabbedPane.getSelectedIndex(); }
    
    public void addTabChangeListener(javax.swing.event.ChangeListener listener) {
        tabbedPane.addChangeListener(listener);
    }
    
    public void setStatusMessage(String message) {
        statusBar.setText(message);
    }
    
    public void showMessage(String title, String message, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }
    
    public void dispose() {
        if (timeTimer != null) {
            timeTimer.stop();
        }
        super.dispose();
    }
}