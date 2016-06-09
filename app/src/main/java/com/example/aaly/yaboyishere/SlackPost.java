package com.example.aaly.yaboyishere;

/**
 * Created by aaly on 6/9/16.
 */
public class SlackPost {

    private String text;
    private String username;
    private String icon_emoji;

    public SlackPost(String text, String username, String iconEmoji) {
        this.text = text;
        this.username = username;
        this.icon_emoji = iconEmoji;
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
