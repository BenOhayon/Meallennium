package com.example.ben.meallennium.model.entities;

public class Restaurant extends User {

    private String restaurantName;
    private String owner;

    public Restaurant(String restaurantName, String owner, String password) {
        super(restaurantName, password);
        this.owner = owner;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
