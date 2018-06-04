package com.example.shreyanshjain.profile_ui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParsing {

    private int code,top,type;
    private JSONObject result,source,details;
    private String jsonData,header,bg,fg,text1,text2,text3,imageUrl;
    private JSONArray data;

    public JSONParsing(String jsonData) throws JSONException {
        this.jsonData = jsonData;
        source = new JSONObject(jsonData);
        result = source.getJSONObject("result");
        code = source.getInt("code");
        top = result.getInt("top_image");
        type = result.getInt("view_type");
        header = result.getString("top_heading");
        bg = result.getString("top_image_bg");
        fg = result.getString("top_image_fg");
        data = result.getJSONArray("data");
    }

    public int getCode() {
        return code;
    }

    public int getTop() {
        return top;
    }

    public int getType() {
        return type;
    }

    public JSONObject getResult() {
        return result;
    }

    public JSONObject getSource() {
        return source;
    }

    public void setDetails(String temp) throws JSONException {
        details = new JSONObject(temp);
        text1 = details.getString("text1");
        text2 = details.getString("text2");
        text3 = details.getString("text3");
        imageUrl = details.getString("image");
        //return details;
    }

    public String getHeader() {
        return header;
    }

    public String getBg() {
        return bg;
    }

    public String getFg() {
        return fg;
    }

    public String getText1() throws JSONException {
        return text1;
    }

    public String getText2() throws JSONException {
        return text2;
    }

    public String getText3() throws JSONException {
        return text3;
    }

    public String getImageUrl() throws JSONException {
        return imageUrl;
    }

    public JSONArray getData() {
        return data;
    }

    public Card1Data getTexts()
    {
        Card1Data card1 = new Card1Data(text1,text2,text3,imageUrl);
        return card1;
    }
}
