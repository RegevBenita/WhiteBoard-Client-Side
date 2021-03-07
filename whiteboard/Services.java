package com.whiteboard.regi.whiteboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.whiteboard.regi.model.Group;
import com.whiteboard.regi.model.Message;
import com.whiteboard.regi.model.Shape;
import com.whiteboard.regi.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//class that handle all the communication with the server
public final class Services {

    //method that return array list of all the shapes that need to be draw from the database
    public static ArrayList<Shape> getDraw(Context context, int id) {
        String shapeUrl = GroupsMenu.GROUPS_URL + "/" + id +"/shape";
        final ArrayList<Shape> alShapes = new ArrayList<>();
        final Gson gson = new Gson();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        Log.d("Groups id: ", String.valueOf(id));

        JsonArrayRequest jsonArrayRequest =  new JsonArrayRequest(Request.Method.GET, shapeUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i=0; i<response.length(); i++) {
                                Shape shape = gson.fromJson(response.getJSONObject(i).toString(), Shape.class);
                                alShapes.add(shape);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("onErrorResponse   ", error.toString());
                    }
                });
        requestQueue.add(jsonArrayRequest);
        return alShapes;
    }

    //method that post all the shape that been draw
    public static void updateDraw(Context context, ArrayList<Shape> shapes, int groupId) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONArray jsonArrayShapes = new JSONArray();
        Log.d("START OF JSON ARRAY  ", shapes.toString());
        for (Shape shape : shapes) {
            try {
                JSONObject jsonObjectShape = new JSONObject();
                jsonObjectShape.put("color", String.valueOf(shape.getColor()));
                jsonObjectShape.put("fill", String.valueOf(shape.isFill()));
                jsonObjectShape.put("radius", String.valueOf(shape.getRadius()));
                jsonObjectShape.put("shapeText", String.valueOf(shape.getShapeText()));
                jsonObjectShape.put("shapeType", String.valueOf(shape.getShapeType()));
                jsonObjectShape.put("x1", String.valueOf(shape.getX1()));
                jsonObjectShape.put("x2", String.valueOf(shape.getX2()));
                jsonObjectShape.put("y1", String.valueOf(shape.getY1()));
                jsonObjectShape.put("y2", String.valueOf(shape.getY2()));
                jsonObjectShape.put("id", String.valueOf(shape.getId()));
                jsonArrayShapes.put(jsonObjectShape);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("JSONARRAY = ", jsonArrayShapes.toString());
        String shapeUrl = GroupsMenu.GROUPS_URL + "/" + groupId + "/shape";
        Log.d("URL = ", shapeUrl);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, shapeUrl, jsonArrayShapes,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("onErrorResponse   ", error.toString());
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    //method that gets all the messages
    public static ArrayList<Message> getAllMessages(final Context context, int groupID) {
        String messageUrl = GroupsMenu.GROUPS_URL + "/" + groupID +"/message";
        final ArrayList<Message> alMessage = new ArrayList<>();
        final Gson gson = new Gson();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        Log.d("Groups id: ", String.valueOf(groupID));

        JsonArrayRequest jsonGetArrayRequest = new JsonArrayRequest(Request.Method.GET, messageUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i=0; i<response.length(); i++) {
                                Message message = gson.fromJson(response.getJSONObject(i).toString(), Message.class);
                                Log.d("MESSAGE TEXT: ", message.getText());
                                alMessage.add(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("onErrorResponse   ", error.toString());
                    }
                });
        requestQueue.add(jsonGetArrayRequest);
        return alMessage;
    }

    //method that send a message
    public static void sendMessage(Context context, Message message, int groupId) {
        Map<String,String> params = new HashMap<>();
        params.put("id","0");
        params.put("userName",message.getUserName());
        params.put("text",message.getText());
        params.put("userColor", String.valueOf(message.getUserColor()));
        String messageURL = GroupsMenu.GROUPS_URL + "/" + groupId + "/message";
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonPostObjectRequest = new JsonObjectRequest(Request.Method.POST, messageURL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("onErrorResponse   ", error.toString());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }
        };
        requestQueue.add(jsonPostObjectRequest);
    }

    //method that add new user and send the new user data to the server
    public static void addUser(final Context context, final ArrayList<Group> alGroups, String userName, final int groupId) {
        final Gson gson = new Gson();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final int color = (int)(Math.random()*(-16777216));
        Map<String,String> params = new HashMap<>();
        params.put("id","0");
        params.put("txtColor",String.valueOf(color));
        params.put("name",userName);
        String userURL = GroupsMenu.GROUPS_URL + "/" + groupId + "/user";

        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, userURL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        User user = gson.fromJson(response.toString(), User.class);
                        Log.d("USER ID  ","" + user.getId());
                        Intent intent = new Intent(context, DrawAndChat.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("userName",user.getName());
                        bundle.putInt("userId", user.getId());
                        bundle.putInt("userColor", color);
                        if (!alGroups.isEmpty())
                            bundle.putInt("groupId", groupId);
                        else bundle.putInt("groupId", 1);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("onErrorResponse   ", error.toString());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    //method that retrieve  the groups
    public static ArrayList<Group> fetchGroups(Context context, final CallBack callBack) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final Gson gson = new Gson();
        final ArrayList<Group> alGroups = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, GroupsMenu.GROUPS_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i=0; i<response.length(); i++) {
                                Group group = gson.fromJson(response.getJSONObject(i).toString(), Group.class);
                                alGroups.add(group);    // for calculation of the last id
                            }
                            callBack.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("onErrorResponse   ", error.toString());
                    }
                });
        requestQueue.add(jsonArrayRequest);
        return alGroups;
    }

    //method that add new group and send the new group data to the server
    public static void addGroup(final Context context, final ArrayList<Group> alGroups, String groupName, final String userName) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final Gson gson = new Gson();
        Map<String,String> params = new HashMap<>();
        if(!alGroups.isEmpty())
            params.put("id",String.valueOf(alGroups.get(alGroups.size()-1).getId()+1));
        else params.put("id","1");
        params.put("name",groupName);

        JsonObjectRequest  stringRequest = new JsonObjectRequest(Request.Method.POST, GroupsMenu.GROUPS_URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int groupId;
                        Group group = gson.fromJson(response.toString(), Group.class);
                        if(group.getId()!=0) {
                            if(!alGroups.isEmpty())
                                groupId = alGroups.get(alGroups.size() - 1).getId()+1;
                            else groupId = 1;
                            Services.addUser(context, alGroups, userName, groupId);
                        } else {
                            Toast toast=Toast.makeText(context,"Group already exist", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("onErrorResponse   ", error.toString());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}
