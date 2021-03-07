package com.whiteboard.regi.whiteboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    private EditText etUserName;
    private Button btAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUserName = (EditText) findViewById(R.id.etUserName);
        btAccept = (Button) findViewById(R.id.btAccept);

        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(MainActivity.this, GroupsMenu.class);
                bundle.putString("userName", etUserName.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
