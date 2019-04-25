package com.jonatan_vahlberg.shoppinglist;

import io.realm.RealmObject;

public class ShoppingItem extends RealmObject {

    //Properties
    private String  name;
    private int  amount;
    private String image;
    private String amountType;
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


    public String getAmountType() { return amountType; }

    public void setAmountType(String amountType) { this.amountType = amountType; }


    public boolean isChecked() { return checked; }

    public void setChecked(boolean checked) { this.checked = checked; }

    public ShoppingItem(String name,int amount, String image){
        this.name = name;
        this.amount = amount;
        this.image = image;
    }
    //Realm Constructor
    public  ShoppingItem(){

    }
}
