package com.example.restaurant_management_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class CustomerFinalBill extends AppCompatActivity {

    TextView finaLBill;
    int totalPrice;

    String address_of_delivery,cust_name;
    String order_given_by_the_customer="";
    String order_pending_class_name,notifications_to_customer_class_name;
    public void OnClickOfProceedInFinalBill(View view)
    {

        ParseQuery<ParseObject> parseQuery1=new ParseQuery<ParseObject>(notifications_to_customer_class_name);
        parseQuery1.whereEqualTo("cust_name",cust_name);
        parseQuery1.addDescendingOrder("order_id");
        parseQuery1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    final int order_id;
                    if(objects.size()>0)
                    {
                        order_id=(int)objects.get(0).get("order_id")+1;
                    }
                    else
                    {
                        order_id=1;
                    }

                    Toast.makeText(CustomerFinalBill.this, objects.size()+"", Toast.LENGTH_SHORT).show();
                    final ParseObject parseObject=new ParseObject(order_pending_class_name);
                    parseObject.put("order_id",order_id);
                    parseObject.put("cust_name",cust_name);
                    parseObject.put("cust_addr",CustomerOrderSummary.address);
                    parseObject.put("name",order_given_by_the_customer);
                    parseObject.put("total",totalPrice);
                    parseObject.put("order_type","online");
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null)
                            {
                                Toast.makeText(CustomerFinalBill.this, "Order Registered", Toast.LENGTH_SHORT).show();
                                String messageToTheCustomer="Order Processing_"+order_given_by_the_customer;
                                ParseObject parseObject1=new ParseObject(notifications_to_customer_class_name);
                                parseObject1.put("cust_name",cust_name);
                                parseObject1.put("order_id",order_id);
                                parseObject1.put("message",messageToTheCustomer);
                                parseObject1.put("total",totalPrice);
                                parseObject1.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e==null)
                                        {
                                            Toast.makeText(CustomerFinalBill.this, "Message Saved", Toast.LENGTH_SHORT).show();
                                            CustomerMenuDishSelection.selectedItems.clear();
                                            CustomerMenuDishSelection.priceList.clear();
                                            finish();
                                            Intent intent=new Intent(getApplicationContext(),CustomerDashBoard.class);
                                            intent.putExtra("restname","-NA-@123");
                                            startActivity(intent);
                                        }
                                    }
                                });

                            }
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_final_bill);
        finaLBill=findViewById(R.id.custfinalBill);
        finaLBill.setMovementMethod(new ScrollingMovementMethod());
        Intent intent=getIntent();
        totalPrice=intent.getIntExtra("tp",0);
        address_of_delivery=CustomerOrderSummary.address;
        cust_name= ParseUser.getCurrentUser().getUsername();

        String temp_rest_name= CustomerDashBoard.rest_name;
        temp_rest_name = temp_rest_name.replaceAll("[^a-zA-Z0-9]", "");

        order_pending_class_name=temp_rest_name+"OPending";

        notifications_to_customer_class_name=temp_rest_name+"NCustomer";

        String order_to_be_written="";
        for(int i=0;i<CustomerMenuDishSelection.selectedItems.size();i++)
        {
            String[] single_dish_name=CustomerMenuDishSelection.selectedItems.get(i).split("_",2);
            if(i<(CustomerMenuDishSelection.selectedItems.size()-1))
            {
                order_given_by_the_customer += (single_dish_name[0] + ",");
            }
            else
            {
                order_given_by_the_customer+=single_dish_name[0];
            }
            order_to_be_written+=(single_dish_name[0]+"\n");
        }

        String finalString="Name- "+ParseUser.getCurrentUser().getUsername()+"\n"+"Restaurant Name- "+CustomerDashBoard.rest_name
                +"\n"+"Your order-\n"+order_to_be_written+"Total Bill- "+totalPrice;

        finaLBill.setText(finalString);
        Toast.makeText(this, order_given_by_the_customer, Toast.LENGTH_LONG).show();
    }
}