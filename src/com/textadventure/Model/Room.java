package com.textadventure.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Room {
    private String name;
    private String description;
    private HashMap<String, Condition> exits = new HashMap<>();
    private ArrayList<Item> items = new ArrayList<>();

    public Room(String name, String description) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Room name cannot be null or empty.");
        }
        if (description == null) {
            throw new IllegalArgumentException("Room description cannot be null.");
        }

        this.name = name.trim();
        this.description = description;
    }

    public String getName() {
        return this.name;
    }
    public String getDescription() {
        return this.description;
    }
    public HashMap<String, Condition> getExits() {
        return this.exits;
    }
    public ArrayList<Item> getItems() {
        return this.items;
    }

    public void addItem(Item item){
        if(item==null){
            throw new IllegalArgumentException("Invalid item: NULL");
        }
        this.items.add(item);
    }
    public boolean removeItem(Item item){
        if(item==null){
            throw new IllegalArgumentException("Invalid item: NULL");
        }
        return this.items.remove(item);//returns whether the item is removed
    }
    public void addExit(String direction,Condition condition){
        if(condition==null||direction==null){
            throw new IllegalArgumentException("Invalid item: NULL");
        }
        exits.put(direction.trim().toLowerCase(),condition);
    }
    public void setDescription(String description){
        this.description=description;
    }
}
