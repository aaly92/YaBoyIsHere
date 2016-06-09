package com.example.aaly.yaboyishere;

/**
 * Created by aaly on 6/9/16.
 */
public class SlackPost {

    private String text;
    private String username;

    public SlackPost(String text, String username) {
        this.text = text;
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public String getUsername() {
        return username;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
