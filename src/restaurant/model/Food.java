/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurant.model;

public class Food extends MenuItem {
    private String spiceLevel;
    
    public Food(String id, String name, double price, int preparationTime, String spiceLevel) {
        super(id, name, price, preparationTime);
        this.spiceLevel = spiceLevel;
    }
    
    @Override
    public String getCategory() {
        return "Makanan";
    }
    
    @Override
    public String getEmoji() {
        return "🍳";
    }
    
    public String getSpiceLevel() { return spiceLevel; }
}