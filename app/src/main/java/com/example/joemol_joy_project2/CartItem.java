package com.example.joemol_joy_project2;

public class CartItem {

    private String firebaseKey; // Firebase key for the cart item
    private String name;
    private double price;
    private int quantity;

    private String url;

    public CartItem(){

    }

    public CartItem(String firebaseKeyId, String url, String name, Double price, Integer quantity) {
        this.firebaseKey = firebaseKey;
        this.name = name;
        this.url = url;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and setters
    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    public String getImage() {
        return url;
    }

    public void setImage(String url) {
        this.url = url;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
