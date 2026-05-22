/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurant.model;

public class Dessert extends MenuItem {
    private String topping;
    
    public Dessert(String id, String name, double price, int preparationTime, String topping) {
        super(id, name, price, preparationTime);
        this.topping = topping;
    }
    
    @Override
    public String getCategory() {
        return "Dessert";
    }
    
    @Override
    public String getEmoji() {
        return "🍰";
    }
    
    public String getTopping() { return topping; }
}
