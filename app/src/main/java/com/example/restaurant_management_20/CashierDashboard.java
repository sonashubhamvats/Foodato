package com.example.restaurant_management_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;

public class CashierDashboard extends AppCompatActivity {

    public static String rest_name;
    public void OnclickOfProfileCashier(View view)
    {
        Intent intent=new Intent(getApplicationContext(),CashierProfile.class);
        intent.putExtra("restname",rest_name);
        startActivity(intent);

    }
    public void OnClickOfOrderCashier(View view)
    {
        Intent intent=new Intent(getApplicationContext(),CashierChooseDishFromMenu.class);
        startActivity(intent);

    }

    public void ViewOrderStatusCashier(View view)
    {
        startActivity(new Intent(getApplicationContext(),CashierNotifications.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_dashboard);
        TextView textViewRestName = findViewById(R.id.cashierRestnameDashboard);
        rest_name=ParseUser.getCurrentUser().get("RestaurantName").toString();
        textViewRestName.setText(rest_name);

        TextView welcomeMessage=findViewById(R.id.cashierusernameDashboard);
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