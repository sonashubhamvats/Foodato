package com.example.restaurant_management_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class CashierChooseDishFromMenu extends AppCompatActivity {


    TextView restaurant_name,error_prompt;
    ListView menu_in_cash;
    public static ArrayList<String> menuItems=new ArrayList<String>();
    public static ArrayList<Integer> priceList=new ArrayList<Integer>();
    public static ArrayList<String> selectedItems=new ArrayList<String>();
    ArrayAdapter<String> arrayAdapterMenuItems;
    String menu_class_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_choose_dish_from_menu);
        restaurant_name=findViewById(R.id.txtcashierMenuRestname);
        error_prompt=findViewById(R.id.txtcashierPrompt);

        menu_in_cash=findViewById(R.id.cashierMenuList);
        arrayAdapterMenuItems=new ArrayAdapter<String>(this,R.layout.rows,menuItems);
        menu_in_cash.setAdapter(arrayAdapterMenuItems);
        SetTheOnItemClickListener(menu_in_cash);

        restaurant_name.setText(CashierDashboard.rest_name);
        String temp_rest_name= CashierDashboard.rest_name;

        temp_rest_name = temp_rest_name.replaceAll("[^a-zA-Z0-9]", "");
        menu_class_name=temp_rest_name+"Menu";

        ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>(menu_class_name);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()==0)
                    {
                        error_prompt.setText("No menu available-");

                    }
                    else
                    {
                        error_prompt.setText("");
                        menuItems.clear();
                        for(ParseObject parseObject:objects)
                        {
                            menuItems.add(parseObject.get("Name").toString()+"_Price-"+parseObject.get("Price").toString());

                        }
                        arrayAdapterMenuItems.notifyDataSetChanged();
                    }
                }
                else
                {
                    Toast.makeText(CashierChooseDishFromMenu.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void SetTheOnItemClickListener(final ListView listOfItems)
    {
        listOfItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItems.add(menuItems.get(position));
                String[] price=menuItems.get(position).split("-",2);
                priceList.add((int)Double.parseDouble(price[1]));
                startActivity(new Intent(getApplicationContext(),CashierTempOrderSummary.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        selectedItems.clear();
        priceList.clear();
        finish();

    }
}