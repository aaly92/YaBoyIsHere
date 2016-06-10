package com.example.aaly.yaboyishere;

public class SlackPost {

    private String text;
    private String username;
    private String icon_emoji;

    public SlackPost(String text, String username, String iconEmoji) {
        this.text = text;
        this.username = username;
        this.icon_emoji = iconEmoji;
    }
}
