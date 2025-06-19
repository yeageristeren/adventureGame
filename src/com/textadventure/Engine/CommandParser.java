package com.textadventure.Engine;

public class CommandParser {

    CommandParser(){

    }

    public String[] parse(String input){
        if(input== null || input.trim().isEmpty()){
            return new String[0];
        }
        String lower= input.toLowerCase();
        String trimmed=lower.trim();
        String[] parsed = trimmed.split("\\s+");
        return parsed;
    }
}
