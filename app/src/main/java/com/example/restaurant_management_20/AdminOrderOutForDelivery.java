package com.example.restaurant_management_20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class AdminOrderOutForDelivery extends AppCompatActivity {

    ListView orderOutForDeliveryListView;
    ArrayList<String> orderOutForDeliveryList=new ArrayList<String>();
    ArrayAdapter<String> arrayAdapterOrderOutForDelivery;
    String orderOutForDeliveryClass;
    TextView orderOutForDeliveryPrompt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_out_for_delivery);
        orderOutForDeliveryPrompt=findViewById(R.id.adminOrderOutForDeliveryPrompt);
        orderOutForDeliveryPrompt.setText("Nothing to show here!!");
        orderOutForDeliveryListView=findViewById(R.id.adminlistofOrderOutForDelivery);
        arrayAdapterOrderOutForDelivery=new ArrayAdapter<String>(this,R.layout.rows,orderOutForDeliveryList);
        orderOutForDeliveryListView.setAdapter(arrayAdapterOrderOutForDelivery);
        String temp_rest_name= ParseUser.getCurrentUser().get("RestaurantName").toString();
        temp_rest_name = temp_rest_name.replaceAll("[^a-zA-Z0-9]", "");

        orderOutForDeliveryClass= temp_rest_name+"OOutForDelivery";
        fillUp();
    }
    public void RefreshButtonofd(View view)
    {
        fillUp();
    }

    public void fillUp()
    {
        orderOutForDeliveryList.clear();
        ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>(orderOutForDeliveryClass);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0) {
                        orderOutForDeliveryPrompt.setText("");
                        for (ParseObject parseObject : objects) {
                            String temp_message = "Order out for delivery for " + parseObject.get("cust_name") + " order-id- " + parseObject.get("order_id")
                                    +" by the delivery man "+parseObject.get("delivery_by");
                            orderOutForDeliveryList.add(temp_message);
                        }
                        arrayAdapterOrderOutForDelivery.notifyDataSetChanged();
                    }
                    else
                    {
                        orderOutForDeliveryPrompt.setText("Nothing to show here!!");
                    }
                }
                else
                {
                    Toast.makeText(AdminOrderOutForDelivery.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}