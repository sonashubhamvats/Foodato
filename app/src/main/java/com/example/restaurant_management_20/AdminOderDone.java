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

public class AdminOderDone extends AppCompatActivity {

    ListView orderDoneListView;
    ArrayList<String> orderDoneList=new ArrayList<String>();
    ArrayAdapter<String> arrayAdapterOrderDone;
    String orderDoneClass;
    TextView orderDonePrompt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_oder_done);
        orderDonePrompt=findViewById(R.id.adminOrderDonePrompt);
        orderDonePrompt.setText("Nothing to show here!!");
        orderDoneListView=findViewById(R.id.adminlistofOrderDone);
        arrayAdapterOrderDone=new ArrayAdapter<String>(this,R.layout.rows,orderDoneList);
        orderDoneListView.setAdapter(arrayAdapterOrderDone);
        String temp_rest_name= ParseUser.getCurrentUser().get("RestaurantName").toString();
        temp_rest_name = temp_rest_name.replaceAll("[^a-zA-Z0-9]", "");

        orderDoneClass= temp_rest_name+"ODone";
        fillUp();
    }
    public void RefreshButtonod(View view)
    {
        fillUp();
    }

    public void fillUp()
    {
        orderDoneList.clear();
        ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>(orderDoneClass);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0) {
                        orderDonePrompt.setText("");
                        for (ParseObject parseObject : objects) {
                            String temp_message = "Order done for " + parseObject.get("cust_name") + " order-id- " + parseObject.get("order_id");
                            orderDoneList.add(temp_message);
                        }
                        arrayAdapterOrderDone.notifyDataSetChanged();
                    }
                    else
                    {
                        orderDonePrompt.setText("Nothing to show here!!");
                    }
                }
                else
                {
                    Toast.makeText(AdminOderDone.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}