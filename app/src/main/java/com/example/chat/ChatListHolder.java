package com.example.chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ChatListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView chatTextView;
    String userName;
    Context context;
    public ChatListHolder(LayoutInflater inflater, ViewGroup viewGroup) {
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
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("userName", this.userName);
        context.startActivity(intent);
    }
}
