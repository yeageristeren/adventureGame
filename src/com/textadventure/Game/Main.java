package com.textadventure.Game;


import com.google.gson.JsonSyntaxException;
import com.textadventure.Engine.CommandParser;
import com.textadventure.Engine.GameLoader;
import com.textadventure.Model.Item;
import com.textadventure.Model.Room;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        CommandParser parser=new CommandParser();
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
        System.out.println("\n[Main] Entering main game loop...");
        while(true){


            Room currentRoom= game.getCurrentRoom();
            if(currentRoom==null){
                System.err.println("\n[Main] FATAL ERROR: Cannot determine player's current location.");
                break;
            }
            if(currentRoom.getName().equalsIgnoreCase("sewer grate")){
                System.out.println("Wohoooo!!!!! You have escaped.");
                System.out.println("Enjoy your freedom...");
                break;
            }
            System.out.println("----------------------");
            System.out.println("Current Location : "+ currentRoom.getName());
            System.out.println(currentRoom.getDescription());
            ArrayList<Item> roomItems=currentRoom.getItems();
            System.out.print("Items : | ");
            if(roomItems==null||roomItems.isEmpty()){
                System.out.println("No items.");
            }else{
                for(Item item:roomItems){
                    System.out.print(item.getItemName()+" | ");
                }
            }
            System.out.println();
            HashMap<String,String> roomExits = currentRoom.getExits();
            if(roomExits==null){
                System.out.println("No Path Ahead.");}
            else{
                System.out.print("Exits : ");
                for(String direction:roomExits.keySet()){
                    System.out.print(direction+" ");
                }
                System.out.println();
            }

            System.out.println("-------------------");

            System.out.print("Command : ");String cmd = sc.nextLine().toLowerCase();
            String[] cmds = parser.parse(cmd);
            if(cmds==null){
                System.out.println("Type a command...");continue;
            }
            if(cmds[0]!=null){
                if(cmds[0].equals("quit") || cmds[0].equals("exit")){
                    System.out.println("-------------------");
                    System.out.println("Quiting the game.");
                    System.out.println("-------------------");
                    break;
                }else{
                    game.processCommand(cmds);
                }
            }
        }
        sc.close();
    }
}