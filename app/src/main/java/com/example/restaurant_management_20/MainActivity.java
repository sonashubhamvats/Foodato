package com.example.restaurant_management_20;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {

    EditText username,password;

    public void LogInDirectly()
    {
        String role=ParseUser.getCurrentUser().getString("Role");
        switch(role)
        {
            case "Admin":
                finish();
                startActivity(new Intent(getApplicationContext(),AdminDashBoard.class));
                break;
            case "Cashier":
                finish();
                startActivity(new Intent(getApplicationContext(),CashierDashboard.class));
                break;
            case "Chef":
                finish();
                startActivity(new Intent(getApplicationContext(),ChefDashboard.class));
                break;
            case "DeliveryBoy":
                finish();
                startActivity(new Intent(getApplicationContext(),DeliveryBoyDashBoard.class));
                break;
            case "Customer":
                finish();
                startActivity(new Intent(getApplicationContext(),CustomerChooseRest.class));
                break;
        }
    }

    public void LogInFromButton(View view)
    {
        if(username.getText().toString().compareTo("")!=0&&password.getText().toString().compareTo("")!=0)
        {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are u sure??")
                    .setMessage("The details that u have entered will be used to log in..Continue??")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if (e == null) {
                                        String role = user.getString("Role");
                                        switch(role)
                                        {
                                            case "Admin":
                                                finish();
                                                startActivity(new Intent(getApplicationContext(),AdminDashBoard.class));
                                                break;
                                            case "Cashier":
                                                finish();
                                                startActivity(new Intent(getApplicationContext(),CashierDashboard.class));
                                                break;
                                            case "Chef":
                                                finish();
                                                startActivity(new Intent(getApplicationContext(),ChefDashboard.class));
                                                break;
                                            case "DeliveryBoy":
                                                finish();
                                                startActivity(new Intent(getApplicationContext(),DeliveryBoyDashBoard.class));
                                                break;
                                            case "Customer":
                                                finish();
                                                startActivity(new Intent(getApplicationContext(),CustomerChooseRest.class));
                                                break;
                                        }
                                    } else {
                                        new AlertDialog.Builder(MainActivity.this)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setTitle("Error")
                                                .setMessage(e.getMessage())
                                                .setPositiveButton("Continue",null)
                                                .show();
                                    }
                                }
                            });
                        }
                    })
                    .setNegativeButton("No",null)
                    .show();

        }
        else
        {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Error")
                    .setMessage("Fill in all the details to proceed")
                    .setPositiveButton("Continue",null)
                    .show();
        }
    }

    public void onRegisterRequest(View view)
    {

        startActivity(new Intent(getApplicationContext(),Registration.class));
    }
    public void onSignInRequest(View view)
    {

        startActivity(new Intent(getApplicationContext(),SignUp.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=findViewById(R.id.username);
        password=findViewById(R.id.Password);
        Intent intent=getIntent();
        if(intent.getIntExtra("return",0)!=0)
        {
            //temporary measure
            ParseUser currentUser=ParseUser.getCurrentUser();
            if(currentUser!=null)
            {
                Toast.makeText(this, "A user is here "+currentUser.getUsername(), Toast.LENGTH_SHORT).show();
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null) {
                            Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else
            {
                Toast.makeText(this, "No user", Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            //temporary measure
            ParseUser currentUser=ParseUser.getCurrentUser();
            if(currentUser!=null)
            {
                LogInDirectly();
            }
            else
            {
                Toast.makeText(this, "No user", Toast.LENGTH_SHORT).show();
            }

        }
    }


}