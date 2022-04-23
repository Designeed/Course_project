package com.example.Sleepy.main.modules.onBoarding.domain;

public class OnBoarding {
    private String Title, Description;
    private int Image;
    private int Animation;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public int getAnimation(){ return Animation; }

    public void setAnimationLottie(int animation) {
        Animation = animation;
    }
}
