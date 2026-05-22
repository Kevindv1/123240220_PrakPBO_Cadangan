/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurant.view;

import restaurant.model.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class KitchenPanel extends JPanel {
    private DefaultListModel<String> cookingLogModel;
    private JList<String> cookingLogList;
    private JPanel activeOrdersPanel;
    private JLabel statusLabel;
    private JLabel queueSizeLabel;
    
    public KitchenPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initComponents();
    }
    
    private void initComponents() {
        // Top Panel - Status
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        statusLabel = new JLabel("🟢 Dapur Aktif");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        queueSizeLabel = new JLabel("Antrian: 0");
        queueSizeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        topPanel.add(statusLabel, BorderLayout.WEST);
        topPanel.add(queueSizeLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        
        // Active Orders Panel
        activeOrdersPanel = new JPanel();
        activeOrdersPanel.setLayout(new BoxLayout(activeOrdersPanel, BoxLayout.Y_AXIS));
        activeOrdersPanel.setBackground(new Color(245, 245, 245));
        
        JScrollPane activeScroll = new JScrollPane(activeOrdersPanel);
        activeScroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "👨‍🍳 Pesanan Aktif",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        activeScroll.setPreferredSize(new Dimension(500, 400));
        add(activeScroll, BorderLayout.CENTER);
        
        // Cooking Log Panel
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
            "📝 Log Aktivitas Dapur",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        logPanel.setPreferredSize(new Dimension(400, 0));
        
        cookingLogModel = new DefaultListModel<>();
        cookingLogList = new JList<>(cookingLogModel);
        cookingLogList.setFont(new Font("Monospaced", Font.PLAIN, 11));
        cookingLogList.setBackground(new Color(255, 255, 230));
        
        JScrollPane logScroll = new JScrollPane(cookingLogList);
        logPanel.add(logScroll, BorderLayout.CENTER);
        
        add(logPanel, BorderLayout.EAST);
    }
    
    public void updateQueueSize(int size) {
        SwingUtilities.invokeLater(() -> {
            queueSizeLabel.setText("Antrian: " + size);
        });
    }
    
    public void addOrderToKitchen(Order order) {
        SwingUtilities.invokeLater(() -> {
            JPanel orderCard = createOrderCard(order);
            activeOrdersPanel.add(orderCard);
            activeOrdersPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            activeOrdersPanel.revalidate();
            activeOrdersPanel.repaint();
            addLog("📥 Pesanan Masuk: " + order.getOrderId() + " - " + order.getCustomerName());
        });
    }
    
    private JPanel createOrderCard(Order order) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(500, 150));
        
        // Order header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        JLabel orderIdLabel = new JLabel("🏷️ " + order.getOrderId());
        orderIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        orderIdLabel.setForeground(new Color(52, 152, 219));
        
        JLabel customerLabel = new JLabel("👤 " + order.getCustomerName());
        customerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel statusLabel = new JLabel("⏳ PENDING");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(Color.ORANGE);
        statusLabel.setName("statusLabel_" + order.getOrderId());
        
        header.add(orderIdLabel, BorderLayout.WEST);
        header.add(customerLabel, BorderLayout.CENTER);
        header.add(statusLabel, BorderLayout.EAST);
        
        // Items
        JPanel itemsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        itemsPanel.setOpaque(false);
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        for (MenuItem item : order.getItems()) {
            JLabel itemLabel = new JLabel(item.getEmoji() + " " + item.getName() + 
                String.format(" (Rp%,.0f)", item.getPrice()));
            itemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            itemsPanel.add(itemLabel);
        }
        
        card.add(header, BorderLayout.NORTH);
        card.add(itemsPanel, BorderLayout.CENTER);
        
        // Store order ID for reference
        card.putClientProperty("orderId", order.getOrderId());
        
        return card;
    }
    
    public void updateOrderStatus(String orderId, Order.OrderStatus status) {
        SwingUtilities.invokeLater(() -> {
            addLog(String.format("📦 Order %s: %s", orderId, status.getDisplayName()));
            
            // Update status label in the card
            for (Component comp : activeOrdersPanel.getComponents()) {
                if (comp instanceof JPanel) {
                    JPanel card = (JPanel) comp;
                    if (orderId.equals(card.getClientProperty("orderId"))) {
                        // Find status label
                        for (Component inner : card.getComponents()) {
                            if (inner instanceof JPanel) {
                                JPanel header = (JPanel) inner;
                                for (Component label : header.getComponents()) {
                                    if (label instanceof JLabel && 
                                        label.getName() != null && 
                                        label.getName().startsWith("statusLabel")) {
                                        JLabel statusLabel = (JLabel) label;
                                        statusLabel.setText(status.getDisplayName());
                                        
                                        switch (status) {
                                            case COOKING:
                                                statusLabel.setForeground(Color.BLUE);
                                                break;
                                            case READY:
                                                statusLabel.setForeground(new Color(46, 204, 113));
                                                break;
                                            case COMPLETED:
                                                statusLabel.setForeground(Color.GRAY);
                                                break;
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        });
    }
    
    public void removeOrder(String orderId) {
        SwingUtilities.invokeLater(() -> {
            for (Component comp : activeOrdersPanel.getComponents()) {
                if (comp instanceof JPanel) {
                    JPanel card = (JPanel) comp;
                    if (orderId.equals(card.getClientProperty("orderId"))) {
                        activeOrdersPanel.remove(comp);
                        activeOrdersPanel.revalidate();
                        activeOrdersPanel.repaint();
                        addLog("✅ Order " + orderId + " selesai - Dihapus dari antrian aktif");
                        break;
                    }
                }
            }
        });
    }
    
    public void addLog(String message) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
            cookingLogModel.add(0, String.format("[%s] %s", timestamp, message));
            if (cookingLogModel.size() > 100) {
                cookingLogModel.removeElementAt(cookingLogModel.size() - 1);
            }
        });
    }
    
    public void setKitchenStatus(boolean active) {
        statusLabel.setText(active ? "🟢 Dapur Aktif" : "🔴 Dapur Berhenti");
        statusLabel.setForeground(active ? new Color(46, 204, 113) : Color.RED);
    }
    
    public void clearAllOrders() {
        SwingUtilities.invokeLater(() -> {
            activeOrdersPanel.removeAll();
            activeOrdersPanel.revalidate();
            activeOrdersPanel.repaint();
            addLog("🗑️ Semua pesanan telah dihapus dari antrian");
        });
    }
}
