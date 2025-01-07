package com.example.myregister.model;

public class Item {
    String id,name,type,img,size;
    Double price;

    public Item(String id, String name, String type, String img, String size, Double price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.img = img;
        this.size = size;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getImg() {
        return img;
    }

    public String getSize() {
        return size;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", img='" + img + '\'' +
                ", size='" + size + '\'' +
                ", price=" + price +
                '}';
    }
}
