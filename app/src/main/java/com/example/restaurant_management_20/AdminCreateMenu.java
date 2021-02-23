package com.example.restaurant_management_20;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class AdminCreateMenu extends AppCompatActivity {

    TextView error_prompt;
    EditText name_of_the_dish,price_of_the_dish;
    ListView list_of_the_menu;
    ArrayList<String> list_of_menuitems=new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter_menuItems;

    String rest_name,menu_class_name;

    public void ClickOfAddAdmin(View view)
    {

        if(name_of_the_dish.getText().toString().compareTo("")!=0&&price_of_the_dish.getText().toString().compareTo("")!=0)
        {
            boolean isnumber=price_of_the_dish.getText().toString().matches("\\d+");

            if(isnumber)
            {
                error_prompt.setText("");
                ParseObject parseObject=new ParseObject(menu_class_name);
                parseObject.put("Name",name_of_the_dish.getText().toString());
                parseObject.put("Price",price_of_the_dish.getText().toString());
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null)
                        {
                            Toast.makeText(AdminCreateMenu.this, "Dish addedd Successfully in the menu", Toast.LENGTH_SHORT).show();
                            list_of_menuitems.add(name_of_the_dish.getText().toString()+" Price-"+price_of_the_dish.getText().toString());
                            arrayAdapter_menuItems.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(AdminCreateMenu.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else
            {
                Toast.makeText(this, "Enter a number in the Price field!!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Oops!!??")
                    .setMessage("Fill in all the details to add the dish")
                    .setPositiveButton("Ok,Got it",null)
                    .show();

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_menu);

        error_prompt=findViewById(R.id.txtadminErrorPrompt);
        name_of_the_dish=findViewById(R.id.adminNameOfTheDish);
        price_of_the_dish=findViewById(R.id.adminPriceOfTheDish);
        list_of_the_menu=findViewById(R.id.adminListofmenu);

        String temp_rest_name= ParseUser.getCurrentUser().get("RestaurantName").toString();
        temp_rest_name = temp_rest_name.replaceAll("[^a-zA-Z0-9]", "");
        rest_name=temp_rest_name;
        menu_class_name=rest_name+"Menu";

        arrayAdapter_menuItems=new ArrayAdapter<String>(this,R.layout.rows,list_of_menuitems);
        list_of_the_menu.setAdapter(arrayAdapter_menuItems);

        ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>(menu_class_name);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()==0)
                    {
                        Toast.makeText(AdminCreateMenu.this, "No current items in the menu add some", Toast.LENGTH_SHORT).show();
                        error_prompt.setText("Menu is empty- add your dishes!!");
                    }
                    else
                    {
                        error_prompt.setText("");
                        for(ParseObject parseObjects:objects)
                        {
                            String entry_in_the_list=parseObjects.getString("Name")+" Price-"+parseObjects.getString("Price");
                            list_of_menuitems.add(entry_in_the_list);


                        }
                        arrayAdapter_menuItems.notifyDataSetChanged();
                    }
                }
                else
                {
                    Toast.makeText(AdminCreateMenu.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}