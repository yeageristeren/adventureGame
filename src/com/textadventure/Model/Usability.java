package com.textadventure.Model;

public class Usability{
    private String target;
    private String effectDescription;
    private boolean consumesItem;
    private String unlocksExit;
    private String removesTarget;
    private String addsTarget;
    private String changesRoomDescriptionTo;
    private String addsItemToInventory;


    public String getTarget() { return target; }
    public String getEffectDescription() { return effectDescription; }
    public boolean isConsumesItem() { return consumesItem; }
    public String getUnlocksExit() { return unlocksExit; }
    public String getRemovesTarget() { return removesTarget; }
    public String getAddsTarget() { return addsTarget; }
    public String getChangesRoomDescriptionTo() { return changesRoomDescriptionTo; }
    public String getAddsItemToInventory() { return addsItemToInventory; }
}
