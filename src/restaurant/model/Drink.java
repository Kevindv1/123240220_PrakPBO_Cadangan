/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurant.model;

public class Drink extends MenuItem {
    private boolean withIce;
    
    public Drink(String id, String name, double price, int preparationTime, boolean withIce) {
        super(id, name, price, preparationTime);
        this.withIce = withIce;
    }
    
    @Override
    public String getCategory() {
        return "Minuman";
    }
    
    @Override
    public String getEmoji() {
        return "🥤";
    }
    
    public boolean isWithIce() { return withIce; }
}
