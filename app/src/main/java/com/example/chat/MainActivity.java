package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    EditText editText;
    Button sendBtn;
    ObjectInputStream ois;
    DataOutputStream out;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        editText = findViewById(R.id.editText);
        sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String message = editText.getText().toString();
                            out.writeUTF(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("62.113.106.148", 9178);
                    ois = new ObjectInputStream(socket.getInputStream());
                    out = new DataOutputStream(socket.getOutputStream());
                    while (true){
                        String response = (String) ois.readObject();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.append(response+"\n");
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}