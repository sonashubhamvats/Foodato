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

public class CashierFinalBill extends AppCompatActivity {

    TextView finaLBill;
    int totalPrice;

    String table_no;
    String order_given_by_the_customer="";
    String order_pending_class_name,notifications_to_cashier_class_name;
    public void OnClickOfProceedInFinalBillCashier(View view)
    {
        //notification table is used to get the order id of the order
        ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>(notifications_to_cashier_class_name);
        parseQuery.whereEqualTo("table_no",table_no);
        parseQuery.addDescendingOrder("order_id");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
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
                    Toast.makeText(CashierFinalBill.this, objects.size()+"", Toast.LENGTH_SHORT).show();
                    final ParseObject parseObject=new ParseObject(order_pending_class_name);
                    parseObject.put("order_id",order_id);
                    parseObject.put("name",order_given_by_the_customer);
                    parseObject.put("table_no",table_no);
                    parseObject.put("total",totalPrice);
                    parseObject.put("order_type","offline");
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null)
                            {
                                Toast.makeText(CashierFinalBill.this, "Order Registered", Toast.LENGTH_SHORT).show();
                                String messageToTheCashier="Order Processing_"+order_given_by_the_customer;
                                ParseObject parseObject1=new ParseObject(notifications_to_cashier_class_name);
                                parseObject1.put("table_no",table_no);
                                parseObject1.put("order_id",order_id);
                                parseObject1.put("message",messageToTheCashier);
                                parseObject1.put("total",totalPrice);
                                parseObject1.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e==null)
                                        {
                                            Toast.makeText(CashierFinalBill.this, "Message Saved", Toast.LENGTH_SHORT).show();
                                            CashierChooseDishFromMenu.selectedItems.clear();
                                            CashierChooseDishFromMenu.priceList.clear();
                                            finish();
                                            Intent intent=new Intent(getApplicationContext(),CashierDashboard.class);
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
        setContentView(R.layout.activity_cashier_final_bill);
        finaLBill=findViewById(R.id.cashierfinalBill);
        finaLBill.setMovementMethod(new ScrollingMovementMethod());
        Intent intent=getIntent();
        totalPrice=intent.getIntExtra("tp",0);
        table_no=CashierTempOrderSummary.tableNo;

        String temp_rest_name= CashierDashboard.rest_name;
        temp_rest_name = temp_rest_name.replaceAll("[^a-zA-Z0-9]", "");

        order_pending_class_name=temp_rest_name+"OPending";

        notifications_to_cashier_class_name=temp_rest_name+"NCashier";

        String order_to_be_written="";
        for(int i=0;i<CashierChooseDishFromMenu.selectedItems.size();i++)
        {
            String[] single_dish_name=CashierChooseDishFromMenu.selectedItems.get(i).split("_",2);
            if(i<(CashierChooseDishFromMenu.selectedItems.size()-1))
            {
                order_given_by_the_customer += (single_dish_name[0] + ",");
            }
            else
            {
                order_given_by_the_customer+=single_dish_name[0];
            }
            order_to_be_written+=(single_dish_name[0]+"\n");
        }

        String finalString="TableNo- "+table_no+"\n"+"Restaurant Name- "+CustomerDashBoard.rest_name
                +"\n"+"Your order-\n"+order_to_be_written+"Total Bill- "+totalPrice;

        finaLBill.setText(finalString);
        Toast.makeText(this, order_given_by_the_customer, Toast.LENGTH_LONG).show();
    }
}