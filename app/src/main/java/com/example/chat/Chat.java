package com.example.chat;

import android.content.Context;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Chat {
    public static void showChatList(Context context, LinearLayout linearLayout){
        linearLayout.removeAllViews(); // Важно!!!
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ArrayList<String> names = new ArrayList<>();
        names.add("Петя"); names.add("Вася"); names.add("Коля");
        ChatListAdapter chatListAdapter = new ChatListAdapter(context, names);
        recyclerView.setAdapter(chatListAdapter);
        linearLayout.addView(recyclerView);
    }
}
