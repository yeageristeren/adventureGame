package com.textadventure.Engine;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.textadventure.Model.Item;
import com.textadventure.Model.Room;
import com.textadventure.Model.Usability;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;


class GameData{
    String playerStart;
    ArrayList<ItemData> items;
    ArrayList<RoomData> rooms;
}

class RoomData{
    String name;
    String description;
    ArrayList<String> items = new ArrayList<>();
    HashMap<String,String> exits=new HashMap<>();
    String requiredItem;
}

class ItemData{
    String name;
    String description;
    Usability usability;
}


public class GameLoader {

    Gson gson = new Gson();

    private String playerStart;
    private HashMap<String, Item> loadedItems;
    private HashMap<String, Room> loadedRooms;
    private HashMap<Room,Item> requiredItemHash;

    public GameLoader(){
        loadedItems=new HashMap<>();
        loadedRooms=new HashMap<>();
        requiredItemHash=new HashMap<>();
    }

    public void loadGameData(String filePath) throws IOException, JsonSyntaxException,IllegalArgumentException, GameDataException {
        if(filePath.trim().isEmpty()||filePath==null){
            throw new IllegalArgumentException("The file path cant be empty");
        }
        Path path=Paths.get(filePath);
        String jsonContent="";
        try {
            jsonContent = Files.readString(path);
        } catch (IOException e) {
            System.err.println("Error while reading th game data.");
            throw new IOException("Failed to read game data file: '" + filePath + "'. Check file existence and permissions.", e);
        }
        GameData gameData;
        try {
            gameData = gson.fromJson(jsonContent, GameData.class);
            if (gameData == null) {
                // Handle cases where JSON is valid but represents 'null' or empty object incorrectly
                throw new GameDataException("Parsed game data is null. JSON might be empty or fundamentally incorrect.");
            }
            System.out.println("JSON content successfully parsed into intermediate GameData object.");
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing JSON file: " + filePath + ". Invalid JSON syntax.");
            throw e;
        } catch (Exception e) {
            // Catch other potential runtime exceptions during parsing
            System.err.println("An unexpected error occurred during JSON parsing: " + e.getMessage());
            throw new GameDataException("Unexpected error during JSON parsing.", e);
        }

        // 4. Process the intermediate data to create actual game objects
        try {
            processIntermediateData(gameData);
            System.out.println("Intermediate GameData successfully processed into Room and Item objects.");
        } catch (GameDataException e) {
            // Catch logical errors from the processing step
            System.err.println("Error processing game data logic: " + e.getMessage());
            throw e; // Re-throw the specific game data error
        }
    }

    private void processIntermediateData(GameData gameData) throws GameDataException{
        if (gameData.items == null) {
            System.out.println("Warning: 'items' array not found or null in JSON. No items will be loaded.");
            gameData.items = new ArrayList<>(); // Avoid NullPointerException later
        }
        if (gameData.rooms == null) {
            throw new GameDataException("'rooms' array not found or null in JSON. Cannot load game world.");
        }
        if (gameData.playerStart == null || gameData.playerStart.trim().isEmpty()) {
            throw new GameDataException("'playerStart' field not found, null, or empty in JSON. Cannot determine starting room.");
        }


        playerStart= gameData.playerStart.trim();

        //loading items
        for(ItemData itemData:gameData.items){
            if(itemData==null|| itemData.name == null || itemData.name.trim().isEmpty()) {
                System.err.println("Warning: Skipping invalid item data (null or missing name).");
                continue; // Skip this invalid item
            }
            String itemName = itemData.name;
            if (loadedItems.containsKey(itemName)) {
                throw new GameDataException("Duplicate item name found in JSON: '" + itemName + "'");
            }
            String itemDesc= (itemData.description==null || itemData.description.trim().isEmpty())? "An Item" : itemData.description;
            Item item = new Item(itemName,itemDesc, itemData.usability);
            loadedItems.put(itemName,item);
            System.out.println("item: "+itemName+" created");
        }

        //loading rooms
        for(RoomData roomData: gameData.rooms){
            if(roomData==null||roomData.name==null||roomData.name.trim().isEmpty()){
                System.err.println("Warning: Skipping invalid room data (null or missing name).");
                continue;
            }
            String roomName=roomData.name.trim();
            if(loadedRooms.containsKey(roomName)){
                throw new GameDataException("Duplicate room name found in JSON: '" + roomName + "'");
            }
            String roomDesc = (roomData.description==null||roomData.description.trim().isEmpty())? "An unknown loaction": roomData.description;
            Room room = new Room(roomName,roomDesc);
            loadedRooms.put(roomName,room);
            System.out.println("created room : "+roomName);
        }


        //linking exits and items to the structures in the Room class objects
        for(RoomData roomData: gameData.rooms){
            if(roomData==null||roomData.name==null||roomData.name.trim().isEmpty()){
                System.err.println("Warning: Skipping invalid room data (null or missing name).");
                continue;
            }
            String currentRoomName=roomData.name.trim();
            Room room = loadedRooms.get(currentRoomName);

            //link exits
            if(roomData.exits!=null) {
                if (roomData == null || roomData.name == null || roomData.name.trim().isEmpty()) {
                    continue; // Skip invalid room data encountered before
                }
                for (String key : roomData.exits.keySet()) {
                    String direction = key.trim();
                    String exit = roomData.exits.get(key);
                    room.addExit(direction, exit);
                    System.out.println("  Added exit from '" + currentRoomName + "' [" + direction.toLowerCase().trim() + "] to '" + exit.trim() + "'");
                }
            }
            else{
                System.out.println(currentRoomName+" has no exits defined");
            }

            //link items
            if(roomData.items!=null) {
                for(String itemName: roomData.items){
                    if(!loadedItems.containsKey(itemName.trim()))
                    {throw new GameDataException("Item '" + itemName + "' listed in room '" + currentRoomName +
                            "' but not defined in the top-level 'items' array.");}
                    Item item = loadedItems.get(itemName.trim());
                    room.addItem(item);
                    System.out.println(itemName+" added to room "+roomData.name);
                }
            }else{
                System.out.println(currentRoomName+" has no items available");
            }
    }
    }

    public String getPlayerStart() {
        return this.playerStart;
    }

    public HashMap<String, Room> getLoadedRooms() {
        return this.loadedRooms;
    }

    public HashMap<String, Item> getLoadedItems() {
        return this.loadedItems;
    }
    public static class GameDataException extends Exception{
        public GameDataException(String msg){
            super(msg);
        }
        public GameDataException(String msg,Throwable cause){
            super(msg,cause);
        }
    }
}
