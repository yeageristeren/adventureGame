package com.textadventure.Game;

import com.google.gson.JsonSyntaxException;
import com.textadventure.Engine.GameLoader;
import com.textadventure.Model.Player;
import com.textadventure.Model.Room;

import java.io.IOException;
import java.util.HashMap;

public class Game {
    private HashMap<String, Room> roomHashMap;
    private Player player;
    private final GameLoader gameloader ;


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

        String startRoomName;
        if (gameloader.getPlayerStart() == null || gameloader.getPlayerStart().trim().isEmpty()) {
            throw new GameLoader.GameDataException("unable to load player start");
        } else {
            startRoomName = gameloader.getPlayerStart();
        }
        Player player = new Player(startRoomName);

        System.out.println("[Initialize] Player object created.");
        System.out.println("[Initialize] Player starting room set to: '" + startRoomName + "'.");
        System.out.println("----------------------------------------");
        System.out.println("Game initialization complete!");
        System.out.println("----------------------------------------");
    }


    public Room getRoom(String roomName){
        return this.roomHashMap.get(roomName);
    }

    public Room getCurrentRoom(){
        return this.roomHashMap.get(this.player.getCurrentRoomName());
    }
}
