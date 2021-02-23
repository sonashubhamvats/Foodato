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

public class CustomerProfile extends AppCompatActivity {

    public void OnClickOfBackButton(View view)
    {
        finish();
        startActivity(new Intent(getApplicationContext(),CustomerChooseRest.class));
    }
    public void OnClickOfLogOutButton(View view)
    {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("return",1);

                    Toast.makeText(CustomerProfile.this, "Successful Log out!!!", Toast.LENGTH_SHORT).show();
                    finishAffinity();

                    startActivity(intent);
                }
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        Intent intent=getIntent();
        String rest_name=intent.getStringExtra("restname");

        TextView textViewUsername=findViewById(R.id.custnameprofile);
        String contentofUsernameTextView="Username- "+ ParseUser.getCurrentUser().getUsername();
        textViewUsername.setText(contentofUsernameTextView);

        TextView textviewRestaurantName=findViewById(R.id.custcurrentRestname);
        String contentOfTextViewRestaurantName="Restaurant Name- "+rest_name;
        textviewRestaurantName.setText(contentOfTextViewRestaurantName);
    }
}