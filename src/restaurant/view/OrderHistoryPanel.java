/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurant.view;

import restaurant.model.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class OrderHistoryPanel extends JPanel {
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JTextArea detailArea;
    private RestaurantModel model;
    
    public OrderHistoryPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initComponents();
    }
    
    public void setModel(RestaurantModel model) {
        this.model = model;
    }
    
    private void initComponents() {
        // Table untuk daftar pesanan
        String[] columns = {"Order ID", "Customer", "Status", "Total", "Waktu Order", "Waktu Selesai"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        orderTable = new JTable(tableModel);
        orderTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        orderTable.setRowHeight(30);
        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showOrderDetail();
            }
        });
        
        // Set column widths
        orderTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(
