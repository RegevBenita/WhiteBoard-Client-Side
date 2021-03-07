package com.whiteboard.regi.model;

import org.json.JSONObject;

public class Circle extends Shape{
    public Circle(int id, boolean fill, int color, float x1, float y1, float radius) {
        super(id, "circle", fill, color, x1, y1, 0, 0, radius, "");
    }

    public Circle(JSONObject object){
        super(object);
    }
}
