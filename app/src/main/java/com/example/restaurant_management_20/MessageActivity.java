package com.example.restaurant_management_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MessageActivity extends AppCompatActivity {

    TextView tempMessage;
    public void LogOutButton(View view)
    {
        //this is death
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                    Toast.makeText(MessageActivity.this, "Logging out successfully!! Bye!!", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
                else
                {
                    Toast.makeText(MessageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        tempMessage=findViewById(R.id.tempmessage);
        Intent intent=getIntent();
        String temp="Hello "+ ParseUser.getCurrentUser().getUsername()+" of "+intent.getStringExtra("Rolename")+" of "+intent.getStringExtra("RestaurantName");
        tempMessage.setText(temp);
    }
}