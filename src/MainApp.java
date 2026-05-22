/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package restaurant;

import restaurant.controller.RestaurantController;
import javax.swing.*;
import java.awt.*;

/**
 * Main Class - Entry point untuk aplikasi Restaurant Management System
 * 
 * @author Restaurant Team
 * @version 1.0
 */
public class MainApp {
    
    public static void main(String[] args) {
        // Menjalankan aplikasi di Event Dispatch Thread (EDT) untuk keamanan Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Set Look and Feel sesuai sistem operasi
                    setSystemLookAndFeel();
                    
                    // Tampilkan splash screen
                    showSplashScreen();
                    
                    // Inisialisasi controller dan start aplikasi
                    RestaurantController controller = new RestaurantController();
                    controller.start();
                    
                    // Tambahkan shutdown hook untuk cleanup saat aplikasi ditutup
                    addShutdownHook(controller);
                    
                } catch (Exception e) {
                    System.err.println("Error saat memulai aplikasi: " + e.getMessage());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, 
                        "Gagal memulai aplikasi: " + e.getMessage(),
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    /**
     * Mengatur Look and Feel sesuai sistem operasi
     */
    private static void setSystemLookAndFeel() {
        try {
            // Menggunakan Nimbus Look and Feel untuk tampilan yang lebih modern
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            // Jika Nimbus tidak tersedia, gunakan sistem default
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Gagal mengatur Look and Feel: " + e.getMessage());
        }
    }
    
    /**
     * Menampilkan splash screen saat loading
     */
    private static void showSplashScreen() {
        JWindow splash = new JWindow();
        try {
            // Membuat panel splash screen
            JPanel splashPanel = new JPanel(new BorderLayout());
            splashPanel.setBackground(new Color(46, 204, 113));
            splashPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
            
            // Title
            JLabel titleLabel = new JLabel("MAKAN ENAK RESTAURANT", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            titleLabel.setForeground(Color.WHITE);
            
            // Subtitle
            JLabel subtitleLabel = new JLabel("Restaurant Management System", SwingConstants.CENTER);
            subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            subtitleLabel.setForeground(new Color(255, 255, 255, 200));
            
            // Loading text
            JLabel loadingLabel = new JLabel("Loading...", SwingConstants.CENTER);
            loadingLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            loadingLabel.setForeground(new Color(255, 255, 255, 180));
            
            // Progress bar
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            progressBar.setBackground(new Color(255, 255, 255, 100));
            progressBar.setForeground(Color.WHITE);
            
            JPanel textPanel = new JPanel(new GridLayout(3, 1, 0, 10));
            textPanel.setOpaque(false);
            textPanel.add(titleLabel);
            textPanel.add(subtitleLabel);
            textPanel.add(loadingLabel);
            
            splashPanel.add(textPanel, BorderLayout.CENTER);
            splashPanel.add(progressBar, BorderLayout.SOUTH);
            
            splash.setContentPane(splashPanel);
            splash.pack();
            splash.setSize(500, 300);
            splash.setLocationRelativeTo(null);
            splash.setVisible(true);
            
            // Simulasi loading (2 detik)
            Thread.sleep(2000);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            splash.dispose();
        }
    }
    
    /**
     * Menambahkan shutdown hook untuk membersihkan resource saat aplikasi ditutup
     */
    private static void addShutdownHook(RestaurantController controller) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n🛑 Menutup aplikasi Restaurant Management System...");
            controller.shutdown();
            System.out.println("✅ Aplikasi ditutup dengan aman");
            System.out.println("Terima kasih telah menggunakan sistem kami!");
        }));
    }
}
