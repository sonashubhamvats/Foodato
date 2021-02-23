package com.example.restaurant_management_20;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class CustomerChooseRest extends AppCompatActivity {

    public Spinner rest_name;
    public void OnClickOfProceedInTheChooseRestName(View view)
    {
        if(rest_name.getSelectedItem().toString().compareTo("-Select your restaurant-")==0)
        {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Oops!!??")
                    .setMessage("Pls select the name of the restaurant to proceed")
                    .setPositiveButton("Ok,Got it",null)
                    .show();

        }
        else
        {
            Intent intent=new Intent(getApplicationContext(),CustomerDashBoard.class);
            intent.putExtra("restname",rest_name.getSelectedItem().toString());
            finish();
            startActivity(intent);
        }
    }
    public void fillUpTheSpinner()
    {
        final ArrayList<String> arrayOfRestName=new ArrayList<String>();
        arrayOfRestName.add("-Select your restaurant-");
        final ArrayAdapter<String>arrayAdapterOfListOfRestNames=new ArrayAdapter<String>(this,R.layout.rows,arrayOfRestName);
        arrayAdapterOfListOfRestNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rest_name.setAdapter(arrayAdapterOfListOfRestNames);

        ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>("ListOfRestaurants");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    for(ParseObject parseObject:objects)
                    {
                        arrayOfRestName.add(parseObject.getString("NameOfTheRestaurant"));
                    }
                    arrayAdapterOfListOfRestNames.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(CustomerChooseRest.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_choose_rest);
        TextView textView=findViewById(R.id.custWelcomeMessageRestChoose);
        String welcomeMessage="Welcome- "+ ParseUser.getCurrentUser().getUsername();
        textView.setText(welcomeMessage);
        rest_name=findViewById(R.id.custRestSelectionSpinner);
        fillUpTheSpinner();
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