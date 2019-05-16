package com.jonatan_vahlberg.shoppinglist;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;

public class ShoppingItem extends RealmObject {

    //Properties
    private String  name;
    private int  amount;
    private String amountType;
    private boolean  checked = false;
    private boolean toBeDeleted = false;

    //Parent
    @LinkingObjects("listOfItems")
    private final RealmResults<ShoppingList> itemParents = null;

    //Getters And Setters
    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) { this.amount = amount; }

    public String getAmountType() { return amountType; }

    public void setAmountType(String amountType) { this.amountType = amountType; }


    public boolean isChecked() { return checked; }

    public void setChecked(boolean checked) { this.checked = checked; }

    public boolean isToBeDeleted(){return toBeDeleted;};

    public void setToBeDeleted(Boolean toBeDeleted) {this.toBeDeleted = toBeDeleted;}

    public RealmResults<ShoppingList> getItemParents(){
        return itemParents;
    }

    public ShoppingItem(String name,int amount, String image){
        this.name = name;
        this.amount = amount;
    }
    //Realm Constructor
    public  ShoppingItem(){

    }
}
