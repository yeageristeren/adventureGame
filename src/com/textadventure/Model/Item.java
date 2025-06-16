package com.textadventure.Model;

public class Item {
    private String name;
    private String description;

    Item(String itemName, String itemDescription) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be null or empty.");
        }
        if (description == null) {
            throw new IllegalArgumentException("Item description cannot be null.");
        }
        this.name = itemName;
        this.description = itemDescription;
    }

    public String getItemName() {
        return name;
    }
    public String getItemDescription() {
        return description;
    }
}
