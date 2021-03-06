package com.example.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

public class StartFragment extends Fragment {
    AppCompatButton appCompatBtnStart;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        appCompatBtnStart = v.findViewById(R.id.appCompatBtnStart);
        appCompatBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.changeFragment(new AuthFragment());
            }
        });
        return v;
    }
}
