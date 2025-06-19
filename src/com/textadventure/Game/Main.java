package com.textadventure.Game;


import com.textadventure.Engine.GameLoader;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws GameLoader.GameDataException, IOException {
        System.out.println("Welcome to the Adventure game");
        Game game = new Game();
        game.implement("adventure.json");
    }
}