package com.example.chat;

import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.database.DBMethods;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Auth {
    public static void showAuth(Context context, LinearLayout linearLayout){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 20, 0, 20);
        TextView loginTextview = new TextView(context);
        EditText loginEditText = new EditText(context);
        EditText passEditText = new EditText(context);
        Button loginBtn = new Button(context);
        Button registerBtn = new Button(context);
        loginTextview.setText("Авторизация");
        loginTextview.setGravity(Gravity.CENTER);
        loginTextview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        loginEditText.setHint("Телефон");
        loginEditText.setLayoutParams(lp);
        passEditText.setLayoutParams(lp);
        passEditText.setHint("Пароль");
        passEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        loginBtn.setLayoutParams(lp);
        loginBtn.setText("Войти");
        registerBtn.setText("Создать аккаунт");
        linearLayout.addView(loginTextview);
        linearLayout.addView(loginEditText);
        linearLayout.addView(passEditText);
        linearLayout.addView(loginBtn);
        linearLayout.addView(registerBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegActivity.class);
                context.startActivity(intent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = loginEditText.getText().toString();
                String pass = passEditText.getText().toString();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Socket socket = new Socket("192.168.1.6", 9178);
                            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                            out.writeUTF("auth//"+phone+"//"+pass);
                            String response = (String) ois.readObject();
                            if(response.equals("error")){
                                Toast.makeText(context, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show();
                            }else{
                                String[] resArray = response.split("//");
                                String name = resArray[0];
                                String uuid = resArray[1];
                                String token = resArray[2];
                                DBMethods dbMethods = new DBMethods(context);
                                dbMethods.updateUser(phone, name, uuid, token);
                                MainActivity.isAuth = true;
                                Chat.showChatList(context, linearLayout);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });
    }

    // Авторизация пользователя на сервере
    public static void userAuth(String token, Context context, LinearLayout linearLayout){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("192.168.1.6", 9178);
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF("token//"+token);
                    String response = (String) ois.readObject();
                    if(response.equals("success")){
                        Chat.showChatList(context, linearLayout);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }
}