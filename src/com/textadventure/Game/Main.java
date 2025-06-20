package com.textadventure.Game;


import com.google.gson.JsonSyntaxException;
import com.textadventure.Engine.GameLoader;
import com.textadventure.Model.Room;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        System.out.println("Welcome to the Adventure game");
        Game game = new Game();
        String dataPath="src/adventure.json";
        try{
            game.implement(dataPath);
            System.out.println("[Main] Game initialization successful!");

        } catch (IOException e) {
        // Handle errors related to reading the file (e.g., not found, permissions).
        System.err.println("[Main] FATAL ERROR during initialization: Cannot read game data file.");
        System.err.println("       File Path Attempted: " + dataPath);
        System.err.println("       Error: " + e.getMessage());
        e.printStackTrace();

    } catch (
    JsonSyntaxException e) {
        System.err.println("[Main] FATAL ERROR during initialization: Invalid JSON syntax in game data file.");
        System.err.println("       File Path: " + dataPath);
        System.err.println("       Error: " + e.getMessage());
        e.printStackTrace();
        return;

    } catch (
    GameLoader.GameDataException e) {
        System.err.println("[Main] FATAL ERROR during initialization: Invalid game data structure.");
        System.err.println("       File Path: " + dataPath);
        System.err.println("       Error: " + e.getMessage());
        e.printStackTrace();
        return;

    } catch (IllegalArgumentException e){
        System.err.println("[Main] FATAL ERROR during initialization: Invalid argument provided.");
        System.err.println("       Error: " + e.getMessage());
        e.printStackTrace();
        return;

    } catch (Exception e) {
        System.err.println("[Main] FATAL ERROR during initialization: An unexpected error occurred.");
        System.err.println("       Error: " + e.getMessage());
        e.printStackTrace();
        return;
    }
        boolean gameRunning = true;
        System.out.println("\n[Main] Entering main game loop...");
        while(gameRunning){

            Room currentRoom= game.getCurrentRoom();
            if(currentRoom==null){
                System.err.println("\n[Main] FATAL ERROR: Cannot determine player's current location.");
                gameRunning=false;
                break;
            }
            System.out.println("You are in "+ currentRoom.getName());
            System.out.println("This is a "+currentRoom.getDescription());
            break;
        }
    }
}