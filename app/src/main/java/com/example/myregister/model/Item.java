package com.example.myregister.model;
import java.io.Serializable;  // הוספתי את ה-Serializable


public class Item implements Serializable {  // הוספתי את ה-Serializable פה

    protected String id;

    protected String name;
    protected String type;

    protected String aboutItem;
    protected double price;
    protected String pic;


    public Item() {
    }

    public Item(String id, String name, String type, String pic, String  aboutItem, double price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.aboutItem = aboutItem;
        this.price = price;
        this.pic = pic;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }




    public String getAboutItem() {
        return aboutItem;
    }

    public void setAboutItem(String aboutItem) {
        this.aboutItem = aboutItem;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", aboutItem='" + aboutItem + '\'' +
                ", price=" + price +
                ", pic='" + pic + '\'' +
                '}';
    }
}