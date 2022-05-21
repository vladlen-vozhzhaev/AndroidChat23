package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chat.database.DBMethods;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

public class RegActivity extends AppCompatActivity {
    EditText editTextNameReg;
    EditText editTextPhoneReg;
    EditText editTextTextPasswordReg;
    Button submitReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        editTextNameReg = findViewById(R.id.editTextNameReg);
        editTextPhoneReg = findViewById(R.id.editTextPhoneReg);
        editTextTextPasswordReg = findViewById(R.id.editTextTextPasswordReg);
        submitReg = findViewById(R.id.submitReg);
        submitReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String name = editTextNameReg.getText().toString();
                        String phone = editTextPhoneReg.getText().toString();
                        String pass = editTextTextPasswordReg.getText().toString();
                        UUID uuid = UUID.randomUUID();
                        try {
                            Socket socket = new Socket("192.168.1.6", 9178);
                            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                            out.writeUTF("registration//"+name+"//"+phone+"//"+pass+"//"+uuid.toString());
                            String response = (String) ois.readObject();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(response.equals("user_exist")){
                                        Toast.makeText(RegActivity.this, "user_exist", Toast.LENGTH_SHORT).show();
                                    }else{
                                        DBMethods dbMethods = new DBMethods(RegActivity.this);
                                        dbMethods.addUser(phone, name, uuid.toString(), "0");
                                        Toast.makeText(RegActivity.this, "success", Toast.LENGTH_SHORT).show();
                                    }
                                    onBackPressed();
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