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

public class AdminOrderPending extends AppCompatActivity {

    ListView orderPendingListView;
    ArrayList<String> orderPendingList=new ArrayList<String>();
    ArrayAdapter<String> arrayAdapterOrderPending;
    String orderPendingClass;
    TextView orderPendingPrompt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_pending);
        orderPendingPrompt=findViewById(R.id.adminOrderPendingPrompt);
        orderPendingPrompt.setText("Nothing to show here!!");
        orderPendingListView=findViewById(R.id.adminlistofOrderPending);

        arrayAdapterOrderPending=new ArrayAdapter<String>(this,R.layout.rows,orderPendingList);
        orderPendingListView.setAdapter(arrayAdapterOrderPending);
        String temp_rest_name= ParseUser.getCurrentUser().get("RestaurantName").toString();
        temp_rest_name = temp_rest_name.replaceAll("[^a-zA-Z0-9]", "");

        orderPendingClass= temp_rest_name+"OPending";

        fillUp();
    }
    public void RefreshButtonop(View view)
    {
        fillUp();
    }

    public void fillUp()
    {

        orderPendingList.clear();
        ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>(orderPendingClass);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0) {
                        orderPendingPrompt.setText("");
                        Toast.makeText(AdminOrderPending.this, "In", Toast.LENGTH_SHORT).show();
                        for (ParseObject parseObject : objects) {
                            if(parseObject.get("order_type").toString().compareTo("online")==0) {
                                String temp_message = "Order Pending for " + parseObject.get("cust_name") + " of order-id- " + parseObject.get("order_id");
                                orderPendingList.add(temp_message);

                            }
                            else
                            {
                                String temp_message = "Order Pending for table no " + parseObject.get("table_no") + " of order-id- " + parseObject.get("order_id");
                                orderPendingList.add(temp_message);
                            }
                        }
                        arrayAdapterOrderPending.notifyDataSetChanged();
                    }
                    else
                    {
                        orderPendingPrompt.setText("Nothing to show here!!");
                    }
                }
                else
                {
                    Toast.makeText(AdminOrderPending.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}