package com.example.restaurant_management_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;

public class AdminDashBoard extends AppCompatActivity {

    public void OnClickOfProfileAdmin(View view)
    {
        startActivity(new Intent(getApplicationContext(),AdminProfile.class));
    }

    public void OnClickOfAddMenuAdmin(View view)
    {
        startActivity(new Intent(getApplicationContext(),AdminCreateMenu.class));
    }

    public void OnClickOfOrderDone(View view)
    {
        startActivity(new Intent(getApplicationContext(),AdminOderDone.class));
    }

    public void OnClickOfOrderPending(View view)
    {
        startActivity(new Intent(getApplicationContext(),AdminOrderPending.class));
    }

    public void OnClickOfOrderProcessing(View view)
    {
        startActivity(new Intent(getApplicationContext(),AdminOrderProcessing.class));
    }

    public void OnClickOfOrderOutforDelivery(View view)
    {
        startActivity(new Intent(getApplicationContext(),AdminOrderOutForDelivery.class));
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash_board);
        TextView textViewWelcome=findViewById(R.id.adminDashWelcomeMessage);
        String welcomeContent="Welcome "+ ParseUser.getCurrentUser().getUsername();
        textViewWelcome.setText(welcomeContent);

        TextView textviewRestaurantName=findViewById(R.id.adminDashRestaurantName);
        String restaurantName=ParseUser.getCurrentUser().get("RestaurantName").toString();
        textviewRestaurantName.setText(restaurantName);


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