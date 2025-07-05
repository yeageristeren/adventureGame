package com.textadventure.Game;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.textadventure.Engine.GameLoader;
import com.textadventure.Model.*;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

class SaveState{
    String playerCurrentLocation;
    ArrayList<String> playerInventory;
    HashMap<String,ArrayList<String>> roomState;

    SaveState(String playerCurrentLocation, ArrayList<String> playerInventory, HashMap<String,ArrayList<String>> roomState){
        this.playerCurrentLocation=playerCurrentLocation;
        this.playerInventory=playerInventory;
        this.roomState=roomState;
    }
}

public class Game {
    private HashMap<String, Room> roomHashMap;
    private HashMap<String,Item> itemHashMap;
    private Player player;
    private final GameLoader gameloader ;

    Gson gson = new Gson();

    Game(){
        this.gameloader=new GameLoader();
    }

    public void implement(String filePath) throws IOException,GameLoader.GameDataException,IllegalArgumentException, JsonSyntaxException{
        System.out.println("implementing the game loader");
        gameloader.loadGameData(filePath);
        System.out.println("Successfully loaded the game data");

        if(gameloader.getLoadedRooms()==null||gameloader.getLoadedRooms().isEmpty()){
            throw new GameLoader.GameDataException("No Rooms were loaded");
        }
        this.roomHashMap = gameloader.getLoadedRooms();

        if(gameloader.getLoadedItems()==null||gameloader.getLoadedItems().isEmpty()){
            throw new GameLoader.GameDataException("No Rooms were loaded");
        }
        this.itemHashMap = gameloader.getLoadedItems();

        String startRoomName;
        if (gameloader.getPlayerStart() == null || gameloader.getPlayerStart().trim().isEmpty()) {
            throw new GameLoader.GameDataException("unable to load player start");
        } else {
            startRoomName = gameloader.getPlayerStart();
        }
        this.player = new Player(startRoomName);


        System.out.println("[Initialize] Player object created.");
        System.out.println("[Initialize] Player starting room set to: '" + startRoomName + "'.");
        System.out.println("----------------------------------------");
        System.out.println("Game initialization complete!");
        System.out.println("----------------------------------------");
    }

    public boolean checkCondition(Condition condition){
        if(condition.getRequiredItem()==null||condition.getTargetRoom().isEmpty()){return true;}
            String itemName = condition.getRequiredItem();
            Item item = itemHashMap.get(itemName);
            if(!this.player.getInventory().contains(item)){return false;}
            else{return true;}
    }

    public void processCommand(String[] cmds){
        Room room = roomHashMap.get(this.player.getCurrentRoomName());
        String proverb = cmds[0];
        switch(proverb){
            case "go" :
                if(cmds.length==2){
                    String direction = cmds[1];
                    HashMap<String, Condition> exit = room.getExits();
                    if(!exit.containsKey(direction)){
                        System.out.println("This path has no exit.");
                    }else{
                        Condition condition = room.getExits().get(direction);
                        boolean check = checkCondition(condition);
                        if(!roomHashMap.containsKey(condition.getTargetRoom())){
                            System.out.println("the target room in this direction does not exist");
                            return;
                        }
                        if(!check){
                            System.out.println(condition.getFailMsg());
                            return;
                        }else{
                            String pathRoom = exit.get(direction).getTargetRoom();
                            this.player.setCurrentRoomName(pathRoom);
                            System.out.println("Successfully travelled.");
                        }
                    }
                }else{
                    System.out.println("Enter the right direction you want to go.");
                }
                return ;
            case "inventory":
                ArrayList<Item> inventory = this.player.getInventory();
                System.out.print("Player's Inventory : | ");
                for(Item item:inventory){
                    System.out.print(item.getItemName()+" | ");
                }
                System.out.println();
                return;
            case "take":
                int found =0;
                if(cmds.length>1){
                    String itemName="";
                    for(int i =1;i< cmds.length;i++){itemName=itemName+" "+cmds[i];}
                    itemName=itemName.trim();
                    ArrayList<Item> items = room.getItems();
                    for(Item item:new ArrayList<>(items)){ // using a copy of the list to avoid exceptions that is caused by modification of the iterator
                        if(item.getItemName().equals(itemName)){
                            found = 1;
                            this.player.takeItem(item);
                            room.removeItem(item);
                            System.out.println("Successfully taken the item.");
                        }
                    }
                    if(found==0){
                        System.out.println("Cannot find the object in the room.");
                    }
                }else{
                    System.out.println("What to take? ");
                }
                return;
            case "save":
                String playerStart = this.player.getCurrentRoomName();
                ArrayList<String> playerInventory= new ArrayList<>();
                HashMap<String,ArrayList<String>> roomItemState=new HashMap<>();
                for(Item item:this.player.getInventory()){
                    playerInventory.add(item.getItemName());
                }
                for(String roomName: roomHashMap.keySet()){
                    Room iterRoom = roomHashMap.get(roomName);
                    ArrayList<String> roomItems = new ArrayList<>();
                    if(iterRoom.getItems()!=null){for(Item item: iterRoom.getItems()){
                        roomItems.add(item.getItemName());
                    }}
                    roomItemState.put(roomName,roomItems);
                }
                SaveState saveState= new SaveState(playerStart,playerInventory,roomItemState);
                try(FileWriter writer = new FileWriter("src/savestate.json")){
                    gson.toJson(saveState,writer);

                } catch (Exception e) {
                    System.out.println("cant save state");
                    return;
                }
                return;
            case "load":
                SaveState saveState1;
                try(FileReader reader = new FileReader("src//savestate.json")){
                    saveState1= gson.fromJson(reader, SaveState.class);
                    if(saveState1==null){throw new Exception("cant load saved state");}
                    this.player.setCurrentRoomName(saveState1.playerCurrentLocation);
                    System.out.println("successfully loaded current location");
                    this.player.getInventory().clear();
                    if(!saveState1.playerInventory.isEmpty()){
                        for(String itemName:saveState1.playerInventory){
                            this.player.takeItem(itemHashMap.get(itemName));
                        }
                    }
                    System.out.println("successfully loaded inventory");
                    for(String roomName: roomHashMap.keySet()){
                        Room room1=roomHashMap.get(roomName);
                        room1.getItems().clear();
                        for(String itemNameIter:saveState1.roomState.get(roomName)){
                            room1.addItem(itemHashMap.get(itemNameIter));
                        }
                    }
                    System.out.println("successfully loaded room state");
                } catch (Exception e) {
                    System.out.println("cant read the json saved state");
                }
                return;
            case "use":
                String itemName="";
                String targetName="";
                String iter="";
                for(String cmd:cmds){
                    if(cmd.equals("use")){continue;}
                    if (cmd.equals("on")) {
                        itemName=iter.trim();
                        iter="";
                    }else{
                        iter=iter+" "+cmd;
                    }
                }
                targetName=iter.trim();
                System.out.println(itemName+targetName);
                if(itemHashMap.containsKey(itemName)){
                    Item item = itemHashMap.get(itemName);
                    if(!this.player.getInventory().contains(item)){
                        System.out.println("you dont have this item...");
                        return;
                    }
                    Usability usability = item.getUsability();
                    if(usability==null){
                        System.out.println("this item is non-usable");
                        return;
                    }
                    if(!usability.getTarget().equals(targetName)){
                        System.out.println("cant use this item on target");
                        return;
                    }

                    if(!usability.getEffectDescription().isEmpty()){
                        System.out.println(usability.getEffectDescription());
                    }else{
                        System.out.println("used "+itemName+" on "+targetName);
                    }

                    if(usability.isConsumesItem()){
                        this.player.dropItem(item);
                    }

                    boolean targetPresent=false;
                    if(usability.getTarget().equals("self")){targetPresent=true;}
                    else{
                        Item targetItem=itemHashMap.get(targetName);
                        if(room.getItems().contains(targetItem)){targetPresent=true;}
                    }
                    if(!targetPresent){
                        System.out.println("target not present");return;}

                    if(usability.getRemovesTarget()!=null&&room.getItems().contains(itemHashMap.get(usability.getRemovesTarget()))){
                        Item targetItem = itemHashMap.get(usability.getRemovesTarget());
                        room.removeItem(targetItem);
                        System.out.println("Removed "+usability.getRemovesTarget()+" from the room");
                    }

                    if(usability.getAddsTarget()!=null){
                        Optional<Item> targetItem = Optional.of(itemHashMap.get(usability.getAddsTarget()));
                        if(targetItem.isPresent()){
                            room.addItem(targetItem.get());
                            System.out.println("Added "+targetItem.get().getItemName()+" to the room");
                        }
                    }

                    if(usability.getChangesRoomDescriptionTo()!=null){
                        room.setDescription(usability.getEffectDescription());
                        System.out.println("Changed the room appearance");
                    }

                    if(usability.getAddsItemToInventory()!=null){
                        Optional<Item> toAddItem = Optional.of(itemHashMap.get(usability.getAddsItemToInventory()));
                        if(toAddItem.isPresent()){
                            this.player.getInventory().add(toAddItem.get());
                            System.out.println("Added "+toAddItem.get().getItemName()+" to the player inventory");
                        }
                    }



                }else{
                    System.out.println("No such item exists.");
                }
                return;
            default:
                System.out.println("Enter a valid command.");
                return;
        }
    }


    public Room getRoom(String roomName){
        return this.roomHashMap.get(roomName);
    }

    public Room getCurrentRoom(){
        return this.roomHashMap.get(this.player.getCurrentRoomName());
    }
}
