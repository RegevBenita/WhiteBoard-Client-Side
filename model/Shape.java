package com.whiteboard.regi.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Shape {
    private int color, id;
    private float x1, x2, y1, y2;
    private float radius;
    private boolean fill;
    private String shapeType;
    private String shapeText;

    public Shape() {

    }

    public Shape(int id, String shapeType, boolean fill, int color, float x1, float y1, float x2, float y2, float radius, String shapeText) {
        this.id = id;
        this.shapeType = shapeType;
        this.fill = fill;
        this.color = color;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.radius = radius;
        this.shapeText = shapeText;
    }

    public Shape(JSONObject object){
        try {
            this.id = object.getInt("id");
            this.shapeType = object.getString("shapeType");
            this.fill = object.getBoolean("fill");
            this.color = object.getInt("color");
            this.x1 = (float)object.getDouble("x1");
            this.y1 = (float)object.getDouble("y1");
            this.x2 = (float)object.getDouble("x2");
            this.y2 = (float)object.getDouble("y2");
            this.radius = (float)object.getDouble("radius");
            this.shapeText = object.getString("shapeText");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of shapes
    public static ArrayList<Shape> fromJson(JSONArray jsonObjects) {
        ArrayList<Shape> shapes = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                switch (jsonObjects.getJSONObject(i).getString("shapeType")) {
                    case "rectangle":
                        shapes.add(new Rectangle(jsonObjects.getJSONObject(i)));
                        break;
                    case "circle":
                        shapes.add(new Circle(jsonObjects.getJSONObject(i)));
                        break;
                    case "oval":
                        shapes.add(new Oval(jsonObjects.getJSONObject(i)));
                        break;
                    case "line":
                        shapes.add(new Line(jsonObjects.getJSONObject(i)));
                        break;
                    case "text":
                        shapes.add(new MyText(jsonObjects.getJSONObject(i)));
                        break;
                    default:
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return shapes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public float getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

    public float getY2() {
        return y2;
    }

    public void setY2(float y2) {
        this.y2 = y2;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public boolean isFill() {
        return fill;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }

    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }

    public String getShapeText() {
        return shapeText;
    }

    public void setShapeText(String shapeText) {
        this.shapeText = shapeText;
    }

    //check if 2 shapes are equals
    @Override
    public boolean equals(Object o) {
        if(o instanceof Shape) {
            Shape s = (Shape) o;
            if ( this.shapeType.equals(s.getShapeType()) &&
                this.fill == s.isFill() && this.color == s.getColor() &&
                    (int)this.x1 == (int)s.getX1() && (int)this.x2 == (int)s.getX2() &&
                    (int)this.y1 == (int)s.getY1() && (int)this.y2 == (int)s.getY2() &&
                    (int)this.radius == (int)s.getRadius() && this.shapeText.equals(s.getShapeText())) {
                return true;

            }
        }
        return false;
    }
}
