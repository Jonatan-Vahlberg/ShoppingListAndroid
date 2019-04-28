package com.jonatan_vahlberg.shoppinglist;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ShoppingList extends RealmObject {

    //Primary key when searching Realm Database
    @PrimaryKey
    private long id;
    private String name;
    private String date = "";
    private RealmList<ShoppingItem> listOfItems;

    //Getters And Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public RealmList<ShoppingItem> getListOfItems() {
        return listOfItems;
    }

    public void setListOfItems(RealmList<ShoppingItem> listOfItems) {
        this.listOfItems = listOfItems;
    }

    //Public Constructor used by realm
    public ShoppingList(){

    }
}
