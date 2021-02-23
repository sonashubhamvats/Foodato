package com.example.restaurant_management_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;

public class ChefDashboard extends AppCompatActivity {


    public void OnclickOfProfileChef(View view)
    {
        Intent intent=new Intent(getApplicationContext(),ChefProfile.class);
        startActivity(intent);

    }
    public void OnClickOfOrderChef(View view)
    {
        Intent intent=new Intent(getApplicationContext(),ChefOrderSelection.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_dashboard);
        TextView textViewRestName = findViewById(R.id.chefRestnameDashboard);
        textViewRestName.setText(ParseUser.getCurrentUser().get("RestaurantName").toString());

        TextView welcomeMessage=findViewById(R.id.chefusernameDashboard);
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