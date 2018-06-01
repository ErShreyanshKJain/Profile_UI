package com.example.shreyanshjain.profile_ui;

public class JSONDataClass {

    int view_type,top_image;
    String top_heading,bg_url,fg_url;

    public JSONDataClass(int view_type, int top_image, String top_heading, String bg_url, String fg_url) {
        this.view_type = view_type;
        this.top_image = top_image;
        this.top_heading = top_heading;
        this.bg_url = bg_url;
        this.fg_url = fg_url;
    }

    public int getView_type() {
        return view_type;
    }

    public int getTop_image() {
        return top_image;
    }

    public String getTop_heading() {
        return top_heading;
    }

    public String getBg_url() {
        return bg_url;
    }

    public String getFg_url() {
        return fg_url;
    }
}
