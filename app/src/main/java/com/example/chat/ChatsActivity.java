package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ChatsActivity extends AppCompatActivity {
    RecyclerView chatsRecyclerView;
    DataOutputStream out;
    ObjectInputStream ois;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        chatsRecyclerView = findViewById(R.id.chatsRecyclerView);
        chatsRecyclerView.setLayoutManager(new LinearLayoutManager(ChatsActivity.this));

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<String> usersName = new ArrayList<>();
                    Socket socket = new Socket("62.113.106.148", 9178);
                    out = new DataOutputStream(socket.getOutputStream());
                    ois = new ObjectInputStream(socket.getInputStream());
                    Object object = ois.readObject();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(object.getClass().equals(usersName.getClass())){
                                recyclerViewInit((ArrayList<String>) object);
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


    public void recyclerViewInit(ArrayList<String> usersName){
        ChatAdapter chatAdapter = new ChatAdapter(usersName);
        chatsRecyclerView.setAdapter(chatAdapter);
    }

    public class ChatHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView chatTextView;
        String userName;
        public ChatHolder(LayoutInflater inflater, ViewGroup  viewGroup) {
            super(inflater.inflate(R.layout.single_chat, viewGroup, false));
            chatTextView = itemView.findViewById(R.id.chatTextView);
            chatTextView.setOnClickListener(this);
        }
        public void bind(String userName){
            this.userName = userName;
            chatTextView.setText(userName);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ChatsActivity.this, MainActivity.class);
            intent.putExtra("userName", this.userName);
            startActivity(intent);
        }
    }
    public class ChatAdapter extends RecyclerView.Adapter<ChatHolder> {
        ArrayList<String> chats = new ArrayList<>();
        public ChatAdapter(ArrayList<String> chats){
            this.chats = chats;
        }
        @Override
        public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(ChatsActivity.this);
            return new ChatHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(ChatHolder holder, int position) {
            String userName = chats.get(position);
            holder.bind(userName);
        }

        @Override
        public int getItemCount() { // Количество элементов которое нужно будет отображать
            return chats.size();
        }
    }
}