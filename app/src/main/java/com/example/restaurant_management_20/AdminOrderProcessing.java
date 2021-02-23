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

public class AdminOrderProcessing extends AppCompatActivity {

    ListView orderProcessingListView;
    ArrayList<String> orderProcessingList=new ArrayList<String>();
    ArrayAdapter<String> arrayAdapterOrderProcessing;
    String orderProcessingClass;
    TextView orderProcessingPrompt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_processing);
        orderProcessingPrompt=findViewById(R.id.adminOrderProcessingPrompt);
        orderProcessingPrompt.setText("Nothing to show here!!");
        orderProcessingListView=findViewById(R.id.adminlistofOrderProcessing);
        arrayAdapterOrderProcessing=new ArrayAdapter<String>(this,R.layout.rows,orderProcessingList);
        orderProcessingListView.setAdapter(arrayAdapterOrderProcessing);
        String temp_rest_name= ParseUser.getCurrentUser().get("RestaurantName").toString();
        temp_rest_name = temp_rest_name.replaceAll("[^a-zA-Z0-9]", "");

        orderProcessingClass= temp_rest_name+"OProcessing";
        fillUp();
    }
    public void RefreshButtonopr(View view)
    {
        fillUp();
    }

    public void fillUp()
    {
        orderProcessingList.clear();
        ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>(orderProcessingClass);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0) {
                        orderProcessingPrompt.setText("");
                        for (ParseObject parseObject : objects) {
                            if(parseObject.get("order_type").toString().compareTo("online")==0) {
                                String temp_message = "Order Processing for " + parseObject.get("cust_name") + " of order-id- " + parseObject.get("order_id")
                                        + " process by " + parseObject.get("process_by");
                                orderProcessingList.add(temp_message);

                            }
                            else
                            {
                                String temp_message = "Order Processing for table no " + parseObject.get("table_no") + " of order-id- " + parseObject.get("order_id")
                                        + " process by " + parseObject.get("process_by");
                                orderProcessingList.add(temp_message);
                            }

                        }
                        arrayAdapterOrderProcessing.notifyDataSetChanged();
                    }
                    else
                    {
                        orderProcessingPrompt.setText("Nothing to show here!!");
                    }
                }
                else
                {
                    Toast.makeText(AdminOrderProcessing.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }}