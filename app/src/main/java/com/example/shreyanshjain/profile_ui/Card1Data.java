package com.example.shreyanshjain.profile_ui;

public class Card1Data {

    String heading;
    String sub;
    String desc;
    String image;
    public Card1Data(String heading, String sub, String desc,String image) {
        this.heading = heading;
        this.sub = sub;
        this.desc = desc;
        this.image = image;
    }

    public String getHeading() {
        return heading;
    }

    public String getSub() {
        return sub;
    }

    public String getImage() {
        return image;
    }

    public String getDesc() {
        return desc;
    }
}
