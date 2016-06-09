package com.example.aaly.yaboyishere;

/**
 * Created by aaly on 6/9/16.
 */
public class SlackPost {

    private String text;

    public SlackPost(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
