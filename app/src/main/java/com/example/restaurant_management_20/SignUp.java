package com.example.restaurant_management_20;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

public class SignUp extends AppCompatActivity {

    EditText signInUsername,signInPassword,commmonPassword;
    Spinner roleSpinner,restaurantSpinner;
    String restaurantName,rolename;

    ArrayList<String> listofRestaurants=new ArrayList<String>();
    ArrayAdapter<String> adaptorOfListOfRestaurants;
    ArrayList<String> listOfRoles=new ArrayList<String>();

    public void onClickOfSignUp(View view)
    {
        int flag=0;
        if(signInUsername.getText().toString().compareTo("")==0||signInPassword.getText().toString().compareTo("")==0)
        {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Enter all the details!!")
                    .setMessage("In order for a successful signup u need to enter all details")
                    .setPositiveButton("Continue", null).show();
            flag=1;
        }
        if(flag==0)
        {
            if (rolename.compareTo("-Select your role-") == 0)
            {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Enter all the details!!")
                        .setMessage("In order for a successful signup u need to enter all details")
                        .setPositiveButton("Continue", null).show();
                flag = 1;
            }
            else {
                if (rolename.compareTo("Customer") != 0) {
                    if (restaurantName.compareTo("---Select the restaurant name---") == 0 || commmonPassword.getText().toString().compareTo("") == 0) {
                        new AlertDialog.Builder(this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Enter all the details!!")
                                .setMessage("In order for a successful signup u need to enter all details")
                                .setPositiveButton("Continue", null).show();
                        flag = 1;

                    }
                }
            }
        }
        if(flag==0)
        {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are u sure??")
                    .setMessage("The details that u have entered will be used to make ur account on our app's database..Continue??")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //sign in procedure
                            if(rolename.compareTo("Customer")!=0)
                            {
                                ParseQuery<ParseObject> parseQueryListOfRestNames=new ParseQuery<ParseObject>("ListOfRestaurants");
                                parseQueryListOfRestNames.whereEqualTo("NameOfTheRestaurant",restaurantName);
                                parseQueryListOfRestNames.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if(e==null)
                                        {
                                            for (ParseObject object:objects)
                                            {
                                                String rolePassword=rolename+"CommonPassword";
                                                if(object.getString(rolePassword).compareTo(commmonPassword.getText().toString())==0)
                                                {
                                                    ParseUser parseUser=new ParseUser();
                                                    parseUser.setUsername(signInUsername.getText().toString());
                                                    parseUser.setPassword(signInPassword.getText().toString());
                                                    parseUser.put("Role",rolename);
                                                    parseUser.put("RestaurantName",restaurantName);
                                                    parseUser.signUpInBackground(new SignUpCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            if(e==null)
                                                            {
                                                                Toast.makeText(SignUp.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                                                intent.putExtra("return",1);
                                                                startActivity(intent);
                                                            }
                                                            else
                                                            {
                                                                Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });


                                                }
                                                else
                                                {
                                                    new AlertDialog.Builder(SignUp.this)
                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                            .setTitle("Error!")
                                                            .setMessage("The common password is not correct")
                                                            .setPositiveButton("Continue", null).show();
                                                }
                                            }

                                        }
                                    }
                                });

                            }
                            else
                            {
                                ParseUser parseUser=new ParseUser();
                                parseUser.setUsername(signInUsername.getText().toString());
                                parseUser.setPassword(signInPassword.getText().toString());
                                parseUser.put("Role",rolename);
                                parseUser.signUpInBackground(new SignUpCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e==null)
                                        {
                                            Toast.makeText(SignUp.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                        }
                                    }
                                });
                            }

                        }
                    })
                    .setNegativeButton("No",null)
                    .show();
        }

    }


    public void fillInTheSpinnerOfRestaurantNames()
    {
        ParseQuery<ParseObject> parseQueryListOfRestNames=new ParseQuery<ParseObject>("ListOfRestaurants");
        parseQueryListOfRestNames.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    for (ParseObject object:objects)
                    {
                        listofRestaurants.add(object.getString("NameOfTheRestaurant"));
                    }
                    adaptorOfListOfRestaurants.notifyDataSetChanged();
                }
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        rolename="-Select your role-";
        restaurantName="---Select the restaurant name---";
        roleSpinner=findViewById(R.id.rolespinner);
        restaurantSpinner=findViewById(R.id.restnamesSpinner);
        signInPassword=findViewById(R.id.SignUpPassword);
        signInUsername=findViewById(R.id.SignUpUsername);
        commmonPassword=findViewById(R.id.commonPassword);
        commmonPassword.setVisibility(View.INVISIBLE);
        restaurantSpinner.setVisibility(View.INVISIBLE);
        listOfRoles.add("-Select your role-");
        listOfRoles.add("Admin");
        listOfRoles.add("Chef");
        listOfRoles.add("Cashier");
        listOfRoles.add("DeliveryBoy");
        listOfRoles.add("Customer");
        ArrayAdapter<String> adaptorofRoles=new ArrayAdapter<String>(this,R.layout.rows,listOfRoles);
        adaptorofRoles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adaptorofRoles);
        listofRestaurants.add("---Select the restaurant name---");
        adaptorOfListOfRestaurants=new ArrayAdapter<String>(this,R.layout.rows,listofRestaurants);
        adaptorOfListOfRestaurants.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        restaurantSpinner.setAdapter(adaptorOfListOfRestaurants);
        fillInTheSpinnerOfRestaurantNames();
        setTheClickListenerForBothSpinners();

    }
    public void setTheClickListenerForBothSpinners()
    {
        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemClickRoleSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        restaurantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemClickRestaurantSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void onItemClickRoleSpinner()
    {
        rolename=roleSpinner.getSelectedItem().toString();
        if(rolename.compareTo("Customer")!=0&&rolename.compareTo("-Select your role-")!=0)
        {
            restaurantSpinner.setVisibility(View.VISIBLE);
            commmonPassword.setVisibility(View.VISIBLE);
            commmonPassword.setHint("Enter the common "+rolename+"'s password");
        }
        else
        {
            commmonPassword.setVisibility(View.INVISIBLE);
            restaurantSpinner.setVisibility(View.INVISIBLE);
        }
        

    }
    public void onItemClickRestaurantSpinner()
    {
        restaurantName=restaurantSpinner.getSelectedItem().toString();


    }



}