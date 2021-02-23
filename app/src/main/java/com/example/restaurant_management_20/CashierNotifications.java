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

public class CashierNotifications extends AppCompatActivity {

    TextView cashierOrderStatusPrompt;
    ListView listOfOrderStatus;
    ArrayList<String> listOfMessages=new ArrayList<String>();
    ArrayList<Integer> helperList=new ArrayList<Integer>();
    ArrayAdapter<String> arrayAdaptorListOfMessages;
    String notiCash;

    public void RefreshCashierPendingNoti(View view)
    {
        ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>(notiCash);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size()==0)
                {
                    cashierOrderStatusPrompt.setText("No notifications");
                }
                else
                {
                    listOfMessages.clear();
                    helperList.clear();
                    cashierOrderStatusPrompt.setText("");
                    for(ParseObject parseObject:objects)
                    {
                        String first_message;
                        String[] orderStatus=parseObject.get("message").toString().split("_",2);
                        first_message=orderStatus[0]+" for table_no "+parseObject.get("table_no");
                        listOfMessages.add(first_message);
                        helperList.add(0);
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
    public void RefreshCashier()
    {
        listOfMessages.clear();
        helperList.clear();

        ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>(notiCash);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size()==0)
                {
                    cashierOrderStatusPrompt.setText("No notifications");
                }
                else
                {
                    cashierOrderStatusPrompt.setText("");
                    for(ParseObject parseObject:objects)
                    {
                        String first_message;
                        String[] orderStatus=parseObject.get("message").toString().split("_",2);
                        first_message=orderStatus[0]+" for table_no "+parseObject.get("table_no")+" for price "+parseObject.get("total");
                        listOfMessages.add(first_message);
                        helperList.add(0);
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

    public void deleteNotiCash(View view)
    {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are u sure??")
                .setMessage("This action will delete the info of all the orders that had been completed!!")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>(notiCash);
                        parseQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e)
                            {
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
                                                Toast.makeText(CashierNotifications.this, "Order done for " + parts[0], Toast.LENGTH_SHORT).show();
                                                parseObject.deleteInBackground(new DeleteCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {
                                                            Toast.makeText(CashierNotifications.this, "Object deleted", Toast.LENGTH_SHORT).show();
                                                            RefreshCashier();
                                                        }
                                                    }
                                                });

                                            }

                                        }
                                        RefreshCashier();

                                    }
                                    else
                                    {
                                        Toast.makeText(CashierNotifications.this, "What are u even trying to delete???", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(CashierNotifications.this, "Something is wrong!!!", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_cashier_notifications);
        cashierOrderStatusPrompt=findViewById(R.id.cashierpromptorderstatus);
        listOfOrderStatus=findViewById(R.id.cashierListOrderStatus);
        arrayAdaptorListOfMessages=new ArrayAdapter<String>(this,R.layout.rows,listOfMessages);
        listOfOrderStatus.setAdapter(arrayAdaptorListOfMessages);

        String temp_rest_name= CashierDashboard.rest_name;
        temp_rest_name = temp_rest_name.replaceAll("[^a-zA-Z0-9]", "");

        notiCash=temp_rest_name+"NCashier";
        ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>(notiCash);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size()==0)
                {
                    cashierOrderStatusPrompt.setText("No notifications");
                }
                else
                {
                    listOfMessages.clear();
                    helperList.clear();
                    cashierOrderStatusPrompt.setText("");
                    for(ParseObject parseObject:objects)
                    {
                        String first_message;
                        String[] orderStatus=parseObject.get("message").toString().split("_",2);
                        first_message=orderStatus[0]+" for table_no "+parseObject.get("table_no");
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