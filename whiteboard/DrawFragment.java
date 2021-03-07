package com.whiteboard.regi.whiteboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.whiteboard.regi.model.Shape;

import java.util.ArrayList;

//Fragment that handle all the drawing
public class DrawFragment extends Fragment {
    private TouchEventView touchEventView;
    private String shape;
    private ImageButton btCircle;
    private ImageButton btOval;
    private ImageButton btLine;
    private ImageButton btRectangle;
    private ImageButton btUndo;
    private ImageButton btRedo;
    private ImageButton btSelectColor;
    private ImageButton btText;
    private ImageButton btFillOrEmpty;
    private ImageButton btSendToAll;
    private ArrayList<Shape> alShape;
    private boolean isFill;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alShape = new ArrayList<>();
        alShape = Services.getDraw(getActivity(), ((DrawAndChat) getActivity()).getGroupId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.draw, container, false);
        btCircle = (ImageButton) view.findViewById(R.id.btCircle);
        btOval = (ImageButton) view.findViewById(R.id.btOval);
        btLine = (ImageButton) view.findViewById(R.id.btLine);
        btRectangle = (ImageButton) view.findViewById(R.id.btRectangle);
        btUndo = (ImageButton) view.findViewById(R.id.btUndo);
        btRedo = (ImageButton) view.findViewById(R.id.btRedo);
        btSelectColor = (ImageButton) view.findViewById(R.id.btSelectColor);
        btText = (ImageButton) view.findViewById(R.id.btText);
        btFillOrEmpty = (ImageButton) view.findViewById(R.id.btFillOrEmpty);
        btSendToAll = (ImageButton) view.findViewById(R.id.btSendToAll);
        initButtonClickListeners();
        touchEventView = (TouchEventView) view.findViewById(R.id.drawing);
        touchEventView.setOtherShapes(alShape);
        return view;
    }

    public void getShapes() {
        touchEventView.setOtherShapes(alShape);
        touchEventView.invalidate();
        alShape = Services.getDraw(getActivity(), ((DrawAndChat) getActivity()).getGroupId());
        Log.d("DrawFragment", "schedualGetShapes");
    }

    // method that open dialog and get the text the user want to draw
    private void showDialogText() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter your text here");

        // Set up the input
        final EditText input = new EditText(getActivity());
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                touchEventView.setText(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    //method that handle the color selection
    private void selectColor() {
        ColorPickerDialogBuilder
                .with(getActivity())
                .setTitle("Choose color")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        touchEventView.setColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    //method that initialize all the buttons click listeners
    private void initButtonClickListeners() {

        btCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shape = "circle";
                btOval.setImageResource(R.drawable.oval);
                btLine.setImageResource(R.drawable.line);
                btRectangle.setImageResource(R.drawable.rectangle);
                btText.setImageResource(R.drawable.text);
                btCircle.setImageResource(R.drawable.circle_press);
                touchEventView.setSelectedShape(shape);
            }
        });
        btOval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btCircle.setImageResource(R.drawable.circle);
                btOval.setImageResource(R.drawable.oval_press);
                btLine.setImageResource(R.drawable.line);
                btRectangle.setImageResource(R.drawable.rectangle);
                btText.setImageResource(R.drawable.text);
                shape = "oval";
                touchEventView.setSelectedShape(shape);
            }
        });
        btLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btCircle.setImageResource(R.drawable.circle);
                btOval.setImageResource(R.drawable.oval);
                btLine.setImageResource(R.drawable.line_press);
                btRectangle.setImageResource(R.drawable.rectangle);
                btText.setImageResource(R.drawable.text);
                shape = "line";
                touchEventView.setSelectedShape(shape);
            }
        });
        btRectangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shape = "rectangle";
                btCircle.setImageResource(R.drawable.circle);
                btOval.setImageResource(R.drawable.oval);
                btLine.setImageResource(R.drawable.line);
                btRectangle.setImageResource(R.drawable.rectangle_press);
                btText.setImageResource(R.drawable.text);
                touchEventView.setSelectedShape(shape);
            }
        });
        btUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                touchEventView.undo();
            }
        });
        btRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                touchEventView.redo();
            }
        });
        btSelectColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectColor();
            }
        });
        btText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogText();
                shape = "text";
                btCircle.setImageResource(R.drawable.circle);
                btOval.setImageResource(R.drawable.oval);
                btLine.setImageResource(R.drawable.line);
                btRectangle.setImageResource(R.drawable.rectangle);
                btText.setImageResource(R.drawable.text_press);
                touchEventView.setSelectedShape(shape);
            }
        });

        btFillOrEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFill) {
                    btFillOrEmpty.setImageResource(R.drawable.fill_or_empty);
                    isFill = !isFill;
                }
                else {
                    btFillOrEmpty.setImageResource(R.drawable.fill_or_empty_press);
                    isFill = !isFill;
                }
                touchEventView.setFillOrEmpty();
            }
        });

        btSendToAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                touchEventView.sendToAll(getActivity(), ((DrawAndChat) getActivity()).getGroupId());
            }
        });
    }
}