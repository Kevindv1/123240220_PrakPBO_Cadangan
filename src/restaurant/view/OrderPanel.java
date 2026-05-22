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
import java.util.List;

public class OrderPanel extends JPanel {
    private JTextField customerNameField;
    private JTextArea orderSummaryArea;
    private DefaultListModel<MenuItem> cartModel;
    private JList<MenuItem> cartList;
    private JLabel totalLabel;
    private JButton orderButton, clearButton;
    private JPanel menuContainer;
    private Map<String, List<JCheckBox>> categoryCheckboxes;
    private List<MenuItem> allMenu;
    
    public OrderPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        cartModel = new DefaultListModel<>();
        categoryCheckboxes = new HashMap<>();
        allMenu = new ArrayList<>();
        
        initComponents();
    }
    
    private void initComponents() {
        // Left Panel - Menu
        JPanel menuPanel = createMenuPanel();
        add(menuPanel, BorderLayout.CENTER);
        
        // Right Panel - Cart
        JPanel cartPanel = createCartPanel();
        add(cartPanel, BorderLayout.EAST);
    }
    
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "📋 Menu Makanan & Minuman",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        
        // Customer Info
        JPanel customerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        customerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JLabel nameLabel = new JLabel("Nama Customer:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        customerNameField = new JTextField(25);
        customerNameField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        customerPanel.add(nameLabel);
        customerPanel.add(customerNameField);
        panel.add(customerPanel, BorderLayout.NORTH);
        
        // Menu Categories using JTabbedPane
        JTabbedPane categoryTabs = new JTabbedPane();
        categoryTabs.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        categoryTabs.addTab("🍳 Makanan", createCategoryPanel("Makanan"));
        categoryTabs.addTab("🥤 Minuman", createCategoryPanel("Minuman"));
        categoryTabs.addTab("🍰 Dessert", createCategoryPanel("Dessert"));
        
        panel.add(categoryTabs, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCategoryPanel(String category) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel itemsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        categoryCheckboxes.put(category, new ArrayList<>());
        
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(450, 400));
        panel.add(scrollPane);
        
        return panel;
    }
    
    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
            "🛒 Keranjang Pesanan",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        panel.setPreferredSize(new Dimension(380, 0));
        
        cartList = new JList<>(cartModel);
        cartList.setCellRenderer(new MenuItemRenderer());
        cartList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cartList.setBackground(new Color(255, 255, 200));
        
        JScrollPane scrollPane = new JScrollPane(cartList);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        scrollPane.setPreferredSize(new Dimension(360, 250));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Summary Panel
        JPanel summaryPanel = new JPanel(new BorderLayout(5, 5));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        orderSummaryArea = new JTextArea(8, 25);
        orderSummaryArea.setEditable(false);
        orderSummaryArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        orderSummaryArea.setBackground(new Color(240, 240, 240));
        JScrollPane summaryScroll = new JScrollPane(orderSummaryArea);
        summaryScroll.setBorder(BorderFactory.createTitledBorder("Ringkasan Pesanan"));
        
        totalLabel = new JLabel("Total: Rp0", SwingConstants.RIGHT);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(new Color(231, 76, 60));
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 5));
        orderButton = new JButton("✅ Pesan Sekarang");
        orderButton.setBackground(new Color(46, 204, 113));
        orderButton.setForeground(Color.WHITE);
        orderButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        orderButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        clearButton = new JButton("🗑️ Kosongkan");
        clearButton.setBackground(new Color(231, 76, 60));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        buttonPanel.add(orderButton);
        buttonPanel.add(clearButton);
        
        summaryPanel.add(summaryScroll, BorderLayout.CENTER);
        summaryPanel.add(totalLabel, BorderLayout.NORTH);
        summaryPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        panel.add(summaryPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    public void setMenuList(List<MenuItem> menu) {
        this.allMenu = menu;
        
        // Clear existing checkboxes
        for (List<JCheckBox> checkboxes : categoryCheckboxes.values()) {
            checkboxes.clear();
        }
        
        // Populate menu items to categories
        for (MenuItem item : menu) {
            String category = item.getCategory();
            List<JCheckBox> checkboxes = categoryCheckboxes.get(category);
            
            if (checkboxes != null) {
                JCheckBox checkBox = new JCheckBox(item.toString());
                checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                checkBox.addActionListener(e -> {
                    if (checkBox.isSelected()) {
                        addToCart(item);
                    } else {
                        removeFromCart(item);
                    }
                });
                checkboxes.add(checkBox);
                
                // Add to the appropriate panel (need to find the panel)
                addCheckboxToCategoryPanel(category, checkBox);
            }
        }
    }
    
    private void addCheckboxToCategoryPanel(String category, JCheckBox checkBox) {
        // Find the category tab and add checkbox
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                if (panel.getBorder() instanceof TitledBorder) {
                    TitledBorder border = (TitledBorder) panel.getBorder();
                    // Add to the correct panel (simplified - in production use proper reference)
                }
            }
        }
    }
    
    public void addToCart(MenuItem item) {
        cartModel.addElement(item);
        updateSummary();
    }
    
    private void removeFromCart(MenuItem item) {
        for (int i = 0; i < cartModel.size(); i++) {
            if (cartModel.getElementAt(i).getId().equals(item.getId())) {
                cartModel.remove(i);
                break;
            }
        }
        updateSummary();
    }
    
    public void clearCart() {
        cartModel.clear();
        updateSummary();
        
        // Uncheck all checkboxes
        for (List<JCheckBox> checkboxes : categoryCheckboxes.values()) {
            for (JCheckBox cb : checkboxes) {
                cb.setSelected(false);
            }
        }
    }
    
    private void updateSummary() {
        double total = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("┌─────────────────────────┐\n");
        sb.append("│     DETAIL PESANAN      │\n");
        sb.append("├─────────────────────────┤\n");
        
        for (int i = 0; i < cartModel.size(); i++) {
            MenuItem item = cartModel.getElementAt(i);
            sb.append(String.format("│ %-23s │\n", item.getEmoji() + " " + item.getName()));
            sb.append(String.format("│   Rp%,-15.0f │\n", item.getPrice()));
            total += item.getPrice();
        }
        
        sb.append("├─────────────────────────┤\n");
        sb.append(String.format("│ %-12s Rp%,8.0f │\n", "TOTAL:", total));
        sb.append("└─────────────────────────┘");
        
        orderSummaryArea.setText(sb.toString());
        totalLabel.setText(String.format("Total: Rp%,.0f", total));
    }
    
    public String getCustomerName() {
        return customerNameField.getText().trim();
    }
    
    public List<MenuItem> getCartItems() {
        List<MenuItem> items = new ArrayList<>();
        for (int i = 0; i < cartModel.size(); i++) {
            items.add(cartModel.getElementAt(i));
        }
        return items;
    }
    
    public JButton getOrderButton() { return orderButton; }
    public JButton getClearButton() { return clearButton; }
    
    public void resetForm() {
        customerNameField.setText("");
        clearCart();
    }
    
    private class MenuItemRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, 
                                                      int index, boolean isSelected, 
                                                      boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof MenuItem) {
                MenuItem item = (MenuItem) value;
                setText(String.format("%s %s - Rp%,.0f", item.getEmoji(), item.getName(), item.getPrice()));
            }
            return this;
        }
    }
}