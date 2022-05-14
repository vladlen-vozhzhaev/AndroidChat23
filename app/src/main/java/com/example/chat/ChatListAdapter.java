package com.example.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListHolder>{
    ArrayList<String> chats = new ArrayList<>();
    Context context;
    public ChatListAdapter(Context context, ArrayList<String> chats){
        this.chats = chats;
        this.context = context;
    }
    @Override
    public ChatListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        return new ChatListHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(ChatListHolder holder, int position) {
        String userName = chats.get(position);
        holder.bind(userName);
    }

    @Override
    public int getItemCount() { // Количество элементов которое нужно будет отображать
        return chats.size();
    }
}
