package com.whiteboard.regi.model;

import org.json.JSONObject;

public class Line extends Shape {
    public Line(int id, boolean fill, int color, float x1, float y1, float x2, float y2) {
        super(id, "line", fill, color, x1, y1, x2, y2, 0, "");
    }

    public Line(JSONObject object){
        super(object);
    }
}
