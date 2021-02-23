package com.example.restaurant_management_20;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class OrderStatusCustomer extends AppCompatActivity {

    TextView customerOrderStatusPrompt;
    ListView listOfOrderStatus;
    ArrayList<String> listOfMessages=new ArrayList<String>();
    ArrayList<Integer> helperList=new ArrayList<Integer>();
    ArrayAdapter<String> arrayAdaptorListOfMessages;
    String notiCust;
    public void OnClickRefreshNoti(View view)
    {
        ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>(notiCust);
        parseQuery.whereEqualTo("cust_name", ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size()==0)
                {
                    customerOrderStatusPrompt.setText("Looks empty here, order more?");
                }
                else
                {
                    listOfMessages.clear();
                    helperList.clear();
                    customerOrderStatusPrompt.setText("");
                    for(ParseObject parseObject:objects)
                    {
                        String first_message;
                        String[] orderStatus=parseObject.get("message").toString().split("_",2);
                        first_message=orderStatus[0]+" for Order_id "+parseObject.get("order_id")+" for price "+parseObject.get("total");
                        listOfMessages.add(first_message);
                        helperList.add(1);
                        String[] indi_dishes=orderStatus[1].split(",");
                        for(int i=0;i<indi_dishes.length;i++)
                        {
                            listOfMessages.add(indi_dishes[i]);
                            helperList.add(0);
                        }

                    }
                    arrayAdaptorListOfMessages.notifyDataSetChanged();
                }

            }
        });

    }
    public void RefreshCustomer()
    {
        listOfMessages.clear();
        helperList.clear();
        ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>(notiCust);
        parseQuery.whereEqualTo("cust_name", ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size()==0)
                {
                    customerOrderStatusPrompt.setText("Looks empty here, order more?");
                }
                else
                {

                    customerOrderStatusPrompt.setText("");
                    for(ParseObject parseObject:objects)
                    {
                        String first_message;
                        String[] orderStatus=parseObject.get("message").toString().split("_",2);
                        first_message=orderStatus[0]+" for Order_id "+parseObject.get("order_id")+" for price "+parseObject.get("total");
                        listOfMessages.add(first_message);
                        helperList.add(1);
                        String[] indi_dishes=orderStatus[1].split(",");
                        for(int i=0;i<indi_dishes.length;i++)
                        {
                            listOfMessages.add(indi_dishes[i]);
                            helperList.add(0);
                        }

                    }
                }
                arrayAdaptorListOfMessages.notifyDataSetChanged();

            }
        });


    }
    public void deleteNotiCust(View view)
    {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are u sure??")
                .setMessage("This action will delete the info of all the orders that had been already delivered!!")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>(notiCust);
                        parseQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if(e==null)
                                {
                                    if(objects.size()>0)
                                    {
                                        for(ParseObject parseObject:objects)
                                        {
                                            String message=parseObject.get("message").toString();
                                            String[] parts=message.split("-");
                                            if(parts.length>1)
                                            {
                                                if(parts[0].compareTo("Order Delivered by")==0)
                                                {

                                                    Toast.makeText(OrderStatusCustomer.this, "Order done for " + parts[0], Toast.LENGTH_SHORT).show();
                                                    parseObject.deleteInBackground(new DeleteCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            if (e == null) {
                                                                Toast.makeText(OrderStatusCustomer.this, "Object deleted", Toast.LENGTH_SHORT).show();
                                                                RefreshCustomer();
                                                            }
                                                        }
                                                    });
                                                }
                                            }

                                        }



                                    }
                                    else
                                    {
                                        Toast.makeText(OrderStatusCustomer.this, "What are u even trying to delete???", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(OrderStatusCustomer.this, "Something is wrong!!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                })
                .setNegativeButton("No",null)
                .show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status_customer);

        customerOrderStatusPrompt=findViewById(R.id.custpromptorderstatus);
        listOfOrderStatus=findViewById(R.id.custListOrderStatus);
        arrayAdaptorListOfMessages=new ArrayAdapter<String>(this,R.layout.rows,listOfMessages);
        listOfOrderStatus.setAdapter(arrayAdaptorListOfMessages);

        String temp_rest_name= CustomerDashBoard.rest_name;
        temp_rest_name = temp_rest_name.replaceAll("[^a-zA-Z0-9]", "");

        notiCust=temp_rest_name+"NCustomer";
        ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>(notiCust);
        parseQuery.whereEqualTo("cust_name", ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size()==0)
                {
                    customerOrderStatusPrompt.setText("Looks empty here, order more?");
                }
                else
                {
                    listOfMessages.clear();
                    helperList.clear();
                    customerOrderStatusPrompt.setText("");
                    for(ParseObject parseObject:objects)
                    {
                        String first_message;
                        String[] orderStatus=parseObject.get("message").toString().split("_",2);
                        first_message=orderStatus[0]+" for Order_id "+parseObject.get("order_id")+" for price "+parseObject.get("total");
                        listOfMessages.add(first_message);
                        helperList.add(1);
                        String[] indi_dishes=orderStatus[1].split(",");
                        for(int i=0;i<indi_dishes.length;i++)
                        {
                            listOfMessages.add(indi_dishes[i]);
                            helperList.add(0);
                        }

                    }
                }
                arrayAdaptorListOfMessages.notifyDataSetChanged();
            }
        });
    }
}