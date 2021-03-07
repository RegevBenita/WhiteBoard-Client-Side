package com.whiteboard.regi.whiteboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.whiteboard.regi.model.Circle;
import com.whiteboard.regi.model.Line;
import com.whiteboard.regi.model.MyText;
import com.whiteboard.regi.model.Oval;
import com.whiteboard.regi.model.Rectangle;
import com.whiteboard.regi.model.Shape;

import java.util.ArrayList;
import java.util.Stack;

//class that handle the screen touch and painting
public class TouchEventView extends View {
    private Paint paint = new Paint();
    private int color = Color.WHITE;
    private float xStartPos;
    private float yStartPos;
    private float x1Draw, x2Draw;
    private float y1Draw, y2Draw;
    private float xEndPos;
    private float yEndPos;
    private float radius;
    private boolean fill = false;
    private String selectedShape  = "line";
    private ArrayList<Shape> otherShapes = new ArrayList<>();
    private ArrayList<Shape> allShapes;
    private Stack<Shape> shapes = new Stack<>();
    private Stack<Shape> undoStack = new Stack<>();
    private boolean drawShape;
    private int undoCounter = 0;
    private String text;

    public TouchEventView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true); //smoother drawing
        paint.setColor(color);
        paint.setTextSize(26);
        paint.setStrokeJoin(Paint.Join.ROUND); //when you change direction your corners won't be sharp
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3f);
        this.setBackgroundColor(Color.BLACK);
    }

    public ArrayList<Shape> getOtherShapes() {
        return otherShapes;
    }

    //method that set otherShape to be all the shapes that are not
    // the current shapes that the user draw
    public void setOtherShapes(ArrayList<Shape> otherShapes) {
        for(Shape shape : shapes) {
            for (int i = 0; i < otherShapes.size(); i++)
                if (shape.equals(otherShapes.get(i)))
                    otherShapes.remove(i);
        }
        for(Shape shape : undoStack) {
            for (int i = 0; i < otherShapes.size(); i++)
                if (shape.equals(otherShapes.get(i)))
                    otherShapes.remove(i);
        }
        this.otherShapes = otherShapes;
    }

    public ArrayList<Shape> getAllShapes() {
        allShapes = new ArrayList<>();
        allShapes.addAll(shapes);
        if(otherShapes!=null)
            allShapes.addAll(otherShapes);
        return allShapes;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    // method that draw the shape
    @Override
    protected void onDraw(Canvas canvas) {
        getAllShapes();
        for(Shape shape : allShapes) {         //draw all the shapes that in the stack
            paint.setColor(shape.getColor());
            switch (shape.getShapeType()) {
                case "line":
                    canvas.drawLine(shape.getX1(), shape.getY1(), shape.getX2(), shape.getY2(), paint);
                    break;
                case "circle":
                    if(shape.isFill()) {
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawCircle(shape.getX1(), shape.getY1(), shape.getRadius(), paint);
                    } else {
                        paint.setStyle(Paint.Style.STROKE);
                        canvas.drawCircle(shape.getX1(), shape.getY1(), shape.getRadius(), paint);
                    }
                    break;
                case "oval":
                    if(shape.isFill()) {
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawOval(new RectF(shape.getX1(), shape.getY1(), shape.getX2(), shape.getY2()), paint);
                    } else {
                        paint.setStyle(Paint.Style.STROKE);
                        canvas.drawOval(new RectF(shape.getX1(), shape.getY1(), shape.getX2(), shape.getY2()), paint);
                    }
                    break;
                case "rectangle":
                    if(shape.isFill()) {
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawRect(shape.getX1(), shape.getY1(), shape.getX2(), shape.getY2(), paint);
                    } else {
                        paint.setStyle(Paint.Style.STROKE);
                        canvas.drawRect(shape.getX1(), shape.getY1(), shape.getX2(), shape.getY2(), paint);
                    }
                    break;
                case "text":
                    canvas.drawText(shape.getShapeText(), shape.getX1(), shape.getY1() , paint);
                    break;
            }
        }
        if(drawShape==true) {         //only if I draw shape
            paint.setColor(color);
            switch (selectedShape) {
                case "line":
                    canvas.drawLine(xStartPos, yStartPos, xEndPos, yEndPos, paint);
                    break;
                case "circle":
                    if(fill) {
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawCircle(xStartPos, yStartPos, radius, paint);
                    } else {
                        paint.setStyle(Paint.Style.STROKE);
                        canvas.drawCircle(xStartPos, yStartPos, radius, paint);
                    }
                    break;
                case "oval":
                    if(fill) {
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawOval(new RectF(x1Draw, y1Draw, x2Draw, y2Draw), paint);
                    } else {
                        paint.setStyle(Paint.Style.STROKE);
                        canvas.drawOval(new RectF(x1Draw, y1Draw, x2Draw, y2Draw), paint);
                    }
                    break;
                case "rectangle":
                    if(fill) {
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawRect(x1Draw, y1Draw, x2Draw, y2Draw, paint);
                    } else {
                        paint.setStyle(Paint.Style.STROKE);
                        canvas.drawRect(x1Draw, y1Draw, x2Draw, y2Draw, paint);
                    }
                    break;
            }
        }
    }

    //method that create a shape according to the button that was selected
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (selectedShape) {
            case "line":
                return drawLine(event);
            case "circle":
                return drawCircle(event);
            case "oval":
                return drawOval(event);
            case "rectangle":
                return drawRectangle(event);
            case "text":
                return drawText(event);
            default: break;
        }
        return true;
    }

    // method that arrange the point order so it can be draw on the canvas
    private void arrangePoints(float x1, float y1, float x2, float y2) {
        if(x1>x2) {
            x1Draw = x2;
            x2Draw = x1;
        } else {
            x1Draw = x1;
            x2Draw = x2;
        }
        if(y1>y2) {
            y1Draw = y2;
            y2Draw = y1;
        } else {
            y1Draw = y1;
            y2Draw = y2;
        }
    }

    //method that calculate the rectangle shape
    public boolean drawRectangle(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                paint.setColor(color);
                if(undoCounter!=0)
                    clearUndoStack();
                drawShape = true;
                xStartPos = event.getX();
                yStartPos = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                xEndPos = event.getX();
                yEndPos = event.getY();
                arrangePoints(xStartPos, yStartPos, xEndPos, yEndPos);
                break;
            case MotionEvent.ACTION_UP:
                xEndPos = event.getX();
                yEndPos = event.getY();
                arrangePoints(xStartPos, yStartPos, xEndPos, yEndPos);
                shapes.add(new Rectangle(0, fill, color, x1Draw, y1Draw, x2Draw, y2Draw));
                drawShape = false;
                break;
            default:
                return false;
        }
        invalidate();   //repaint
        return true;
    }

    //method that calculate the circle shape
    public boolean drawCircle(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                paint.setColor(color);
                if(undoCounter!=0)
                    clearUndoStack();
                drawShape = true;
                xStartPos = event.getX();
                yStartPos = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                xEndPos = event.getX();
                yEndPos = event.getY();
                radius = (float) Math.abs(Math.sqrt(Math.pow(xStartPos - xEndPos,2)+Math.pow(yStartPos - yEndPos,2))); // calculate the circle radius
                break;
            case MotionEvent.ACTION_UP:
                xEndPos = event.getX();
                yEndPos = event.getY();
                radius = (float) Math.abs(Math.sqrt(Math.pow(xStartPos - xEndPos,2)+Math.pow(yStartPos - yEndPos,2)));
                shapes.add(new Circle(0, fill, color, xStartPos, yStartPos, radius));
                drawShape = false;
                break;
            default:
                return false;
        }
        invalidate();   //repaint
        return true;
    }

    //method that calculate the oval shape
    public boolean drawOval(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                paint.setColor(color);
                if(undoCounter!=0)
                    clearUndoStack();
                drawShape = true;
                xStartPos = event.getX();
                yStartPos = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                xEndPos = event.getX();
                yEndPos = event.getY();
                arrangePoints(xStartPos, yStartPos, xEndPos, yEndPos);
                break;
            case MotionEvent.ACTION_UP:
                xEndPos = event.getX();
                yEndPos = event.getY();
                arrangePoints(xStartPos, yStartPos, xEndPos, yEndPos);
                shapes.add(new Oval(0, fill, color, x1Draw, y1Draw, x2Draw, y2Draw));
                drawShape = false;
                break;
            default:
                return false;
        }
        invalidate();   //repaint
        return true;
    }

    //method that calculate the line shape
    public boolean drawLine(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                paint.setColor(color);
                if(undoCounter!=0)
                    clearUndoStack();
                drawShape = true;
                xStartPos = event.getX();
                yStartPos = event.getY();
//                path.moveTo(xStartPos,yStartPos);
                return true;
            case MotionEvent.ACTION_MOVE:
                xEndPos = event.getX();
                yEndPos = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                xEndPos = event.getX();
                yEndPos = event.getY();
                shapes.add(new Line(0,fill, color, xStartPos , yStartPos, xEndPos, yEndPos));
                drawShape = false;
                break;
            default:
                return false;
        }
        invalidate();   //repaint
        return true;
    }

    public boolean drawText(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            paint.setColor(color);
            if(undoCounter!=0)
                clearUndoStack();
            xStartPos = event.getX();
            yStartPos = event.getY();
            shapes.add(new MyText(0,color, xStartPos, yStartPos, text));
            drawShape = false;
            invalidate();
            return true;
        }
        return false;
    }

    //method that return the selectedShap
    public String getSelectedShape() {
        return selectedShape;
    }

    //method that set the selectedShape
    public void setSelectedShape(String selectedShape) {
        this.selectedShape = selectedShape;
    }

    // method that undo 1 shape
    public void undo() {
        if(!shapes.isEmpty()) {
            undoCounter++;
            drawShape = false;
            undoStack.add(shapes.pop());
            invalidate();
        }
    }

    // method that redo 1 shape
    public void redo() {
        if(!undoStack.isEmpty()) {
            undoCounter--;
            drawShape = false;
            shapes.add(undoStack.pop());
            setOtherShapes(otherShapes);
            invalidate();
        }
    }

    //method to that clear the undo stack
    public void clearUndoStack() {
        undoStack.removeAllElements();
        undoCounter = 0;
    }

    public boolean isFill() {
        return fill;
    }

    public void setFillOrEmpty() {
        fill = !fill;
    }

    public void sendToAll(Context context, int id) {
        Services.updateDraw(context,getAllShapes(),id);
        Log.d("In send to all", getAllShapes().toString());
    }
}