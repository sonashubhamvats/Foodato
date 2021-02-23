package com.example.restaurant_management_20;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class Registration extends AppCompatActivity {

    public EditText username,restName,adminPassword,chefPassword,cashierPassword,deliveryBoyPassword,personalPassword;
    public void onClickOfRegister(View view)
    {
        final String this_username=username.getText().toString(),this_restName=restName.getText().toString(),
                this_adminPassword=adminPassword.getText().toString(),this_chefpassword=chefPassword.getText().toString(),
                this_cashierPassword=cashierPassword.getText().toString(),this_deliveryBoypassword=deliveryBoyPassword.getText().toString();

        if(this_username.compareTo("")==0||this_restName.compareTo("")==0||this_adminPassword.compareTo("")==0
        ||this_chefpassword.compareTo("")==0||this_cashierPassword.compareTo("")==0||this_deliveryBoypassword.compareTo("")==0)
        {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Enter all the details!!")
                    .setMessage("In order for a successful registration u need to enter all details")
                    .setPositiveButton("Continue",null).show();
        }
        else
        {
            new AlertDialog.Builder(Registration.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are u sure??")
                    .setMessage("The details that u have entered will be used to make ur account on our app's database..Continue??")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //ParseUserLogin
                            ParseUser parseUser=new ParseUser();
                            parseUser.setUsername(username.getText().toString());
                            parseUser.setPassword(personalPassword.getText().toString());
                            parseUser.put("Role","Admin");
                            parseUser.put("RestaurantName",restName.getText().toString());

                            parseUser.signUpInBackground(new SignUpCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e==null)
                                    {
                                        Toast.makeText(Registration.this, username.getText().toString()+" Congrats!!! u have been registered successfully as an Admin", Toast.LENGTH_LONG).show();
                                        //restaurant registration
                                        ParseObject list_of_restaurants=new ParseObject("ListOfRestaurants");
                                        list_of_restaurants.put("NameOfTheRestaurant",this_restName);
                                        list_of_restaurants.put("AdminCommonPassword",this_adminPassword);
                                        list_of_restaurants.put("CashierCommonPassword",this_cashierPassword);
                                        list_of_restaurants.put("ChefCommonPassword",this_chefpassword);
                                        list_of_restaurants.put("DeliveryBoyCommonPassword",this_deliveryBoypassword);
                                        list_of_restaurants.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if(e==null)
                                                {
                                                    Toast.makeText(Registration.this, "Restaurant Successfully registered", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                                    intent.putExtra("return",1);
                                                    startActivity(intent);

                                                }
                                                else
                                                {
                                                    Toast.makeText(Registration.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });



                                    }
                                    else
                                    {
                                        Toast.makeText(Registration.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }
                    })
                    .setNegativeButton("No",null)
                    .show();



        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        username=findViewById(R.id.Username);
        restName=findViewById(R.id.restname);
        adminPassword=findViewById(R.id.AdminPassword);
        chefPassword=findViewById(R.id.ChefPassword);
        cashierPassword=findViewById(R.id.CashierPassword);
        deliveryBoyPassword=findViewById(R.id.DeliveryPassword);
        personalPassword=findViewById(R.id.personalAdminPassword);
    }
}