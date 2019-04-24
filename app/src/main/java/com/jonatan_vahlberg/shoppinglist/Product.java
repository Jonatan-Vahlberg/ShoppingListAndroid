package com.jonatan_vahlberg.shoppinglist;

import io.realm.RealmObject;

public class Product extends RealmObject {




    private String  name;
    private int  amount;
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Product(String name,int amount, String image){
        this.name = name;
        this.amount = amount;
        this.image = image;
    }

    public  Product(){

    }


}
