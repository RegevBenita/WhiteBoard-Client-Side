package com.whiteboard.regi.model;

public class Message {

    private int id, userColor;
    private String text;
    private String userName;

    public Message() {

    }

    public Message(int id, String userName, String text, int userColor) {
        this.id = id;
        this.userName = userName;
        this.text = text;
        this.userColor = userColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserColor() {
        return userColor;
    }

    public void setUserColor(int userColor) {
        this.userColor = userColor;
    }
}
