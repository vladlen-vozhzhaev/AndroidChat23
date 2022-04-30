package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class AuthActivity extends AppCompatActivity {
    Button startRegActivityBtn;
    Button submitAuth;
    EditText editTextPhoneAuth;
    EditText editTextPasswordAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        startRegActivityBtn = findViewById(R.id.startRegActivityBtn);
        submitAuth = findViewById(R.id.submitAuth);
        editTextPhoneAuth = findViewById(R.id.editTextPhoneAuth);
        editTextPasswordAuth = findViewById(R.id.editTextPasswordAuth);
        startRegActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthActivity.this, RegActivity.class);
                startActivity(intent);
            }
        });
        submitAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editTextPhoneAuth.getText().toString();
                String pass = editTextPasswordAuth.getText().toString();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Socket socket = new Socket("192.168.1.2", 9178);
                            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                            out.writeUTF("auth//"+phone+"//"+pass+"//");
                            String response = (String) ois.readObject();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(response.equals("success")){
                                        Intent intent = new Intent(AuthActivity.this, ChatsActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(AuthActivity.this, "wrong password or phone", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });
    }
}