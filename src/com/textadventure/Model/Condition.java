package com.textadventure.Model;

public class Condition {
    private String targetRoom;
    private String requiredItem;
    private String failMsg;

    public Condition(){}
    public Condition(String targetRoom, String requiredItem, String failMsg){
        this.failMsg=failMsg;
        this.targetRoom=targetRoom;
        this.requiredItem=requiredItem;
    }

    public String getFailMsg() {
        return failMsg;
    }

    public String getRequiredItem() {
        return requiredItem;
    }

    public String getTargetRoom() {
        return targetRoom;
    }
}
