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

public class AdminProfile extends AppCompatActivity {

    public void onClickOfLogOutAdmin(View view)
    {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                    Toast.makeText(AdminProfile.this, "Successful Log out!!!", Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("return",1);
                    finishAffinity();
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        TextView textViewUsername=findViewById(R.id.adminameprofile);
        String welcomeContent="Username-"+ ParseUser.getCurrentUser().getUsername();
        textViewUsername.setText(welcomeContent);

        TextView textviewRestaurantName=findViewById(R.id.admincurrentRestname);
        String restaurantName="Restaurant Name-"+ParseUser.getCurrentUser().get("RestaurantName").toString();
        textviewRestaurantName.setText(restaurantName);
    }
}