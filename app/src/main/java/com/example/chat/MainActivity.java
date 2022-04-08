package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    EditText editText;
    Button sendBtn;
    ObjectInputStream ois;
    DataOutputStream out;
    RecyclerView recyclerView;
    ArrayList<Message> messages = new ArrayList<>();
    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = getIntent().getStringExtra("userName");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
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
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerViewInit("/tell "+userName+" "+message, true);
                                }
                            });
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
                                recyclerViewInit(response, false);
                                //textView.append(response+"\n");
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
    public void recyclerViewInit(String message, boolean formCurrentUser){
        Message message1 = new Message(message, formCurrentUser);
        messages.add(message1);
        MessageAdapter messageAdapter = new MessageAdapter(messages);
        recyclerView.setAdapter(messageAdapter);
    }
    public class MessageHolder extends RecyclerView.ViewHolder{
        TextView singleMessageTextView;
        Message message;
        public MessageHolder(LayoutInflater inflater, ViewGroup  viewGroup) {
            super(inflater.inflate(R.layout.single_message, viewGroup, false));
            singleMessageTextView = itemView.findViewById(R.id.singleMessageTextView);
        }
        public void bind(Message message){
            this.message = message;
            if(message.isFromCurrentUser())
                singleMessageTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            singleMessageTextView.setText(message.getMessage());
        }
    }
    public class MessageAdapter extends RecyclerView.Adapter<MessageHolder>{
        ArrayList<Message> messages = new ArrayList<>();
        public MessageAdapter(ArrayList<Message> messages){ this.messages = messages;}
        @Override
        public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            return new MessageHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(MessageHolder holder, int position) {
            Message message = messages.get(position);
            holder.bind(message);
        }

        @Override
        public int getItemCount() { // Количество элементов которое нужно будет отображать
            return messages.size();
        }
    }
}