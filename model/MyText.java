package com.whiteboard.regi.model;

import org.json.JSONObject;

public class MyText extends Shape {
    public MyText(int id, int color, float x1, float y1, String text) {
        super(id, "text", false, color, x1, y1, 0, 0, 0, text);
    }

    public MyText(JSONObject object){
        super(object);
    }
}
