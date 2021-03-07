package com.whiteboard.regi.whiteboard;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.whiteboard.regi.model.Message;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Fragment that handle all the chat
public class ChatFragment extends Fragment {
    private ListView lvChat;
    private Button btChatSend;
    private EditText etChatMessage;
    private ArrayList<Message> alMessage;
    private int groupID;
    private String userName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alMessage = new ArrayList<>();
        groupID = ((DrawAndChat) getActivity()).getGroupId();
        userName = ((DrawAndChat) getActivity()).getUserName();
        alMessage = Services.getAllMessages(getActivity(), groupID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        lvChat = (ListView) view.findViewById(R.id.lvChat);
        etChatMessage = (EditText) view.findViewById(R.id.etChatMessage);
        btChatSend = (Button) view.findViewById(R.id.btChatSend);
        lvChat.setAdapter(new ChatAdapter(getActivity(), R.layout.chat_raw, alMessage));
        btChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
                etChatMessage.setText("");
            }
        });
        return view;
    }

    //method that send a message
    private void sendMessage() {
        Message message = new Message(0, userName, etChatMessage.getText().toString(), ((DrawAndChat)getActivity()).getUserColor());
        alMessage.add(message);
        Services.sendMessage(getActivity(), message, groupID);
        ChatAdapter chatAdapter = new ChatAdapter(getActivity(), R.layout.chat_raw, alMessage);
        lvChat.setAdapter(chatAdapter);
        lvChat.setSelection(chatAdapter.getCount() - 1);
    }

    public void getChat() {
        ChatAdapter chatAdapter = new ChatAdapter(getActivity(), R.layout.chat_raw, alMessage);
        lvChat.setAdapter(chatAdapter);
        lvChat.setSelection(chatAdapter.getCount() - 1);    // Select the last row so it will scroll into view...
        alMessage = Services.getAllMessages(getActivity(), groupID);
    }
}
