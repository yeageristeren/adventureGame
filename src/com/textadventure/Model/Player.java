package com.textadventure.Model;

import java.util.ArrayList;

public class Player {
    private String currentRoomName;
    private ArrayList<Item> inventory= new ArrayList<>();

    public Player(String roomName){
        if(roomName==null||roomName.trim().isEmpty()){
            throw new IllegalArgumentException("Room Name cant be empty.");
        }
        this.currentRoomName = roomName;
    }

    public String getCurrentRoomName(){
        return this.currentRoomName;
    }

    public ArrayList<Item> getInventory(){
        return this.inventory;
    }

    public void setCurrentRoomName(String roomName){
        if(roomName.trim().isEmpty()||roomName==null){
            throw new IllegalArgumentException("The room name cant be empty");
        }
    }

    public void takeItem(Item item){
        if(item==null){
            throw new IllegalArgumentException("Item cant be null");
        }
        this.inventory.add(item);
    }

    public boolean dropItem(Item item){
        return this.inventory.remove(item);
    }
}
