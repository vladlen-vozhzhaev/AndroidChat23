package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    LinearLayout mainLinerLayout;
    public static boolean isAuth = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null)
            isAuth = savedInstanceState.getBoolean("isAuth");

        mainLinerLayout = findViewById(R.id.mainLinerLayout);
        if(!isAuth){
            Auth.showAuth(MainActivity.this, mainLinerLayout);
        }else{
            Chat.showChatList(MainActivity.this, mainLinerLayout);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("isAuth", isAuth);
    }
}