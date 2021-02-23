package com.example.restaurant_management_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;

public class CustomerDashBoard extends AppCompatActivity {

    public static String rest_name;
    public void OnclickOfProfile(View view)
    {
        Intent intent=new Intent(getApplicationContext(),CustomerProfile.class);
        intent.putExtra("restname",rest_name);
        startActivity(intent);

    }
    public void OnClickOfOrder(View view)
    {
        Intent intent=new Intent(getApplicationContext(),CustomerMenuDishSelection.class);
        startActivity(intent);

    }

    public void ViewOrderStatus(View view)
    {
        startActivity(new Intent(getApplicationContext(),OrderStatusCustomer.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dash_board);
        Intent intent=getIntent();
        TextView textViewRestName = findViewById(R.id.custRestnameDashboard);
        if(intent.getStringExtra("restname").compareTo("-NA-@123")!=0)
        {
            rest_name = intent.getStringExtra("restname");

        }
        textViewRestName.setText(rest_name);

        TextView welcomeMessage=findViewById(R.id.custusernameDashboard);
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