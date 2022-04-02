package com.example.Sleepy.adapters;

public class TimeCards {
    private String Title;
    private String SecText;


    public TimeCards(String title, String secText) {
        Title = title;
        SecText = secText;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSecText() {
        return SecText;
    }

    public void setSecText(String secText) {
        SecText = secText;
    }
}
