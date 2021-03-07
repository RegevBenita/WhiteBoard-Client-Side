package com.whiteboard.regi.whiteboard;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.whiteboard.regi.model.Group;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.jar.JarException;

//class that handle the group creation or join group
public class GroupsMenu extends Activity {

    private Button btAddGroup;
    private EditText etAddGroup;
    private ListView lvGroups;
    private ArrayList<Group> alGroups;
    private int groupID;
    private String userName;
    public static final String GROUPS_URL = "http://10.0.2.2:8080/whiteboard/webapi/groups";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_menu);
        userName = getIntent().getExtras().getString("userName");
        btAddGroup = (Button) findViewById(R.id.btAddGroup);
        etAddGroup = (EditText) findViewById(R.id.etAddGroup);
        lvGroups = (ListView) findViewById(R.id.lvGroups);
        alGroups = new ArrayList<>();

        alGroups = Services.fetchGroups(this, new CallBack() {  //force the listview be populate after all the data arrived
            @Override
            public void onSuccess(JSONArray result) {
                populateListView();
            }
        });

        btAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGroup();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        alGroups = Services.fetchGroups(this, new CallBack() {  //force the listview be populate after all the data arrived
            @Override
            public void onSuccess(JSONArray result) {
                populateListView();
            }
        });
    }

    //method that populate the list view and handle the clicks
    public void populateListView() {
        GroupAdapter adapter = new GroupAdapter(this, android.R.layout.simple_list_item_1, alGroups);
        lvGroups.setAdapter(adapter);
        lvGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                groupID = alGroups.get(position).getId();
                Log.d("Group ID", groupID+ "    !!!!!!!" );
                addUser();
            }
        });
    }

    //method that add new group and send the new group data to the server
    public void addGroup() {
        alGroups = Services.fetchGroups(this, new CallBack() {  //force the listview be populate after all the data arrived
            @Override
            public void onSuccess(JSONArray result) {
                if(alGroups.size()>0)
                    groupID = alGroups.get(alGroups.size()-1).getId();
                else groupID = 1;
                Services.addGroup(GroupsMenu.this, alGroups, etAddGroup.getText().toString(), userName);
            }
        });
    }

//    //method that add new user and send the new user data to the server
    public void addUser() {
        Services.addUser(this,alGroups, userName, groupID);
    }

}
