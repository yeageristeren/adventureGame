package com.textadventure.Model;

public class Item {
    private String name;
    private String description;
    private Usability usability;

    public Item(String itemName, String itemDescription,Usability usability) {
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be null or empty.");
        }
        if (itemDescription == null) {
            throw new IllegalArgumentException("Item description cannot be null.");
        }
        this.name = itemName;
        this.description = itemDescription;
        this.usability=usability;
    }

    public String getItemName() {
        return name;
    }
    public String getItemDescription() {
        return description;
    }
    public Usability getUsability(){
        return this.usability;
    }
}
