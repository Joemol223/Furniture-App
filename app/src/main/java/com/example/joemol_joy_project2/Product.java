package com.example.joemol_joy_project2;

public class Product {
    private String url;
    private String name;
    private String desc;
    private String longDesc;
    private double price;

    public Product(){

    }

    public Product(String url, String name, String desc,String longDesc, double price) {
        this.url = url;
        this.name = name;
        this.desc = desc;
        this.longDesc = longDesc;
        this.price = price;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
