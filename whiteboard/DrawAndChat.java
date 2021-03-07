package com.whiteboard.regi.whiteboard;

import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Class that contain the chat and the draw fragments
public class DrawAndChat extends AppCompatActivity {
    private static final int REFRESH_TIME = 5000;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String USER_URL;
    private int groupId;
    private int userId, userColor;
    private String userName;
    private Handler handler;
    private DrawFragment drawFragment;
    private ChatFragment chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_and_chat);
        handler = new Handler();
        chat =new ChatFragment();
        drawFragment = new DrawFragment();
        groupId = getIntent().getExtras().getInt("groupId");
        userId = getIntent().getExtras().getInt("userId");
        userName = getIntent().getExtras().getString("userName");
        userColor = getIntent().getExtras().getInt("userColor");
        USER_URL = GroupsMenu.GROUPS_URL + "/" + groupId + "/user/" + userId;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        schedualGetShapes();
        ViewPagerAdapter viewPagerAdapter = (ViewPagerAdapter)viewPager.getAdapter();
        drawFragment =(DrawFragment)viewPagerAdapter.getItem(0);
        chat = (ChatFragment) viewPagerAdapter.getItem(1);

    }

    //method that get the messages and the shapes every REFRESH_TIME ms
    public void schedualGetShapes() {
        handler.postDelayed(new Runnable() {
            public void run() {
                Log.d("DrawFragment", "schedualGetShapes");
                handler.postDelayed(this, REFRESH_TIME);
                drawFragment.getShapes();
                chat.getChat();
            }
        }, REFRESH_TIME);
    }

    public int getUserColor() {
        return userColor;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    //method that set the fragment
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DrawFragment(), "Draw");
        adapter.addFragment(new ChatFragment(), "Chat");
        viewPager.setAdapter(adapter);
    }

    //method that delete the user from the group while pressing the back button that on the phone
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        if (keyCode == KeyEvent.KEYCODE_BACK) { //handle the back key, delete the user from the group
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, USER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }};
            Log.d("USER URL", USER_URL);
            requestQueue.add(stringRequest);
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);  //remove the handler
        super.onDestroy();
    }

    //class that set the title and the fragment that match the title
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
