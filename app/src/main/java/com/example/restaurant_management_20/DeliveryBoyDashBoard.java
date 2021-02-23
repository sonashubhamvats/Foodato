package com.example.restaurant_management_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;

public class DeliveryBoyDashBoard extends AppCompatActivity {
    public void OnclickOfProfiledb(View view)
    {
        Intent intent=new Intent(getApplicationContext(),DeliveryManProfile.class);
        startActivity(intent);

    }
    public void OnClickOfOrderdb(View view)
    {
        Intent intent=new Intent(getApplicationContext(),delivaeryManOrderPending.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_dash_board);
        TextView textViewRestName = findViewById(R.id.dbRestnameDashboard);
        textViewRestName.setText(ParseUser.getCurrentUser().get("RestaurantName").toString());

        TextView welcomeMessage=findViewById(R.id.dbusernameDashboard);
        String welcomeMessageString="Hello "+ ParseUser.getCurrentUser().getUsername();
        welcomeMessage.setText(welcomeMessageString);

    }
    //on back pressed from the first pages of the app

    @Override
    public void onBackPressed() {
        finish();
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("return",1);
        startActivity(intent);
    }

}