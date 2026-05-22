/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurant.model;

import java.io.Serializable;

public abstract class MenuItem implements Serializable {
    private String id;
    private String name;
    private double price;
    private int preparationTime;
    
    public MenuItem(String id, String name, double price, int preparationTime) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.preparationTime = preparationTime;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getPreparationTime() { return preparationTime; }
    
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    
    public abstract String getCategory();
    public abstract String getEmoji();
    
    @Override
    public String toString() {
        return String.format("%s %s - Rp%.0f (%d detik)", getEmoji(), name, price, preparationTime);
    }
}