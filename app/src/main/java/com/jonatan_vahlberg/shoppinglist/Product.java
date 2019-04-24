package com.jonatan_vahlberg.shoppinglist;

import io.realm.RealmObject;

/**Realm Object Used in Version 1.0*/
public class Product extends RealmObject {

    //Properties
    private String  name;
    private int  amount;
    private String image;
    private boolean  checked = false;

    //Getters And Setters
    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) { this.amount = amount; }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isChecked() { return checked; }

    public void setChecked(boolean checked) { this.checked = checked; }

    public Product(String name,int amount, String image){
        this.name = name;
        this.amount = amount;
        this.image = image;
    }
    //Realm Constructor
    public  Product(){

    }


}
