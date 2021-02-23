package com.example.restaurant_management_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class delivaeryManOrderPending extends AppCompatActivity {
    TextView dbOrderPrompt;
    ListView listOfPendingOrders;
    ArrayList<String> listOfOrders=new ArrayList<String>();
    ArrayAdapter<String> adapterofListOfOrders;
    String order_out_for_delivery,order_done_class_name,notify_cust_class_name;
    Button doneButton,refreshButton;
    String curr_addr;
    public void onClickOfDonedb(View view)
    {
        //checking if there is any item in the list then done will work
        if(listOfOrders.size()>0)
        {
            ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>(order_out_for_delivery);
            parseQuery.whereEqualTo("delivery_by", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {

                        ParseQuery<ParseObject> parseQuery1 = new ParseQuery<ParseObject>(notify_cust_class_name);
                        parseQuery1.whereEqualTo("order_id", objects.get(0).get("order_id"));
                        parseQuery1.whereEqualTo("cust_name", objects.get(0).get("cust_name"));
                        parseQuery1.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    if (objects.size() == 1) {
                                        Toast.makeText(delivaeryManOrderPending.this, "here in offline", Toast.LENGTH_SHORT).show();
                                        String message = objects.get(0).get("message").toString();
                                        String[] partmessage = message.split("_", 2);
                                        message = "Order Delivered by- " + ParseUser.getCurrentUser().getUsername() + "_" + partmessage[1];
                                        objects.get(0).put("message", message);
                                        objects.get(0).saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    Toast.makeText(delivaeryManOrderPending.this, "Updated", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                } else {
                                    Toast.makeText(delivaeryManOrderPending.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        objects.get(0).deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(delivaeryManOrderPending.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                                    listOfOrders.clear();
                                    dbOrderPrompt.setText("No order for now!!");
                                    adapterofListOfOrders.notifyDataSetChanged();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(delivaeryManOrderPending.this, "Something Amiss here", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }
        else
        {
            Toast.makeText(this, "There are no orders!!", Toast.LENGTH_SHORT).show();
        }

    }
    public void OnClickOfRefreshdb(View view)
    {
        if(listOfOrders.size()==0)
        {
            final ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>(order_out_for_delivery);
            parseQuery.whereEqualTo("delivery_by", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> object, ParseException e) {
                    if (e == null) {
                        dbOrderPrompt.setText("");
                        if (object.size() == 0) {
                            ParseQuery<ParseObject> parseQuery1 = new ParseQuery<ParseObject>(order_done_class_name);
                            parseQuery1.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(final List<ParseObject> objects, ParseException e) {
                                    if (e == null) {

                                        if (objects.size() == 0) {
                                            dbOrderPrompt.setText("No orders for now!!");
                                        } else {
                                            Toast.makeText(delivaeryManOrderPending.this, objects.get(0).get("name").toString(), Toast.LENGTH_SHORT).show();
                                            int index = 0;
                                            //saving the last row of the table order pending into the order processing and deleting it from the order pending
                                            final ParseObject temp_order_in_process = new ParseObject(order_out_for_delivery);
                                            temp_order_in_process.put("order_id", objects.get(index).get("order_id"));
                                            temp_order_in_process.put("cust_name", objects.get(index).get("cust_name").toString());
                                            temp_order_in_process.put("cust_addr", objects.get(index).get("cust_addr").toString());
                                            curr_addr=objects.get(0).get("cust_addr").toString();
                                            temp_order_in_process.put("delivery_by", ParseUser.getCurrentUser().getUsername());
                                            temp_order_in_process.put("name", objects.get(index).get("name").toString());
                                            temp_order_in_process.put("total", objects.get(index).get("total"));

                                            String first_message = "Order for- " + objects.get(index).get("cust_name").toString();
                                            listOfOrders.add(first_message);

                                            String[] indi_dishes = objects.get(0).get("name").toString().split(",");
                                            for (int i = 0; i < indi_dishes.length; i++) {
                                                listOfOrders.add(indi_dishes[i]);
                                            }
                                            final String cust_name = objects.get(index).get("cust_name").toString();
                                            final int order_id = (int) objects.get(index).get("order_id");

                                            temp_order_in_process.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {

                                                        //deleting the parse row
                                                        objects.get(0).deleteInBackground(new DeleteCallback() {
                                                            @Override
                                                            public void done(ParseException e) {
                                                                if (e == null) {
                                                                    Toast.makeText(delivaeryManOrderPending.this, "Row added to the process and removed from the pending", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        adapterofListOfOrders.notifyDataSetChanged();
                                                        doneButton.setVisibility(View.VISIBLE);
                                                        refreshButton.setVisibility(View.VISIBLE);
                                                        //update the cust_notifications here
                                                        ParseQuery<ParseObject> parseQuery2 = new ParseQuery<ParseObject>(notify_cust_class_name);
                                                        parseQuery2.whereEqualTo("order_id", order_id);
                                                        parseQuery2.whereEqualTo("cust_name", cust_name);
                                                        parseQuery2.findInBackground(new FindCallback<ParseObject>() {
                                                            @Override
                                                            public void done(List<ParseObject> objects, ParseException e) {
                                                                if (e == null) {
                                                                    if (objects.size() == 1) {
                                                                        String message = objects.get(0).get("message").toString();
                                                                        String[] product_name = message.split("_", 2);
                                                                        String out_for_delivery_message = "Order out for delivery by- " + ParseUser.getCurrentUser().getUsername()
                                                                                + "_" + product_name[1];
                                                                        objects.get(0).put("message", out_for_delivery_message);
                                                                        objects.get(0).saveInBackground(new SaveCallback() {
                                                                            @Override
                                                                            public void done(ParseException e) {
                                                                                if (e == null) {
                                                                                    Toast.makeText(delivaeryManOrderPending.this, "Notifications Updated", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });

                                                                    }
                                                                }
                                                            }
                                                        });

                                                    }
                                                }
                                            });

                                        }

                                    } else {
                                        Toast.makeText(delivaeryManOrderPending.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
        else
        {
            Toast.makeText(this, "First complete the current order!!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivaery_man_order_pending);
        doneButton=findViewById(R.id.dbdone);
        doneButton.setVisibility(View.INVISIBLE);
        refreshButton=findViewById(R.id.dbRefreshButton);
        refreshButton.setVisibility(View.INVISIBLE);
        dbOrderPrompt=findViewById(R.id.dbOrderPrompt);
        listOfPendingOrders=findViewById(R.id.dblistofordersPending);
        listOfPendingOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    Intent intent=new Intent(getApplicationContext(),DeliveryBoyAddressView.class);
                    intent.putExtra("Addr",curr_addr);
                    startActivity(intent);

                }
            }
        });
        adapterofListOfOrders=new ArrayAdapter<String>(this,R.layout.rows,listOfOrders);
        listOfPendingOrders.setAdapter(adapterofListOfOrders);
        String temp_rest_name= ParseUser.getCurrentUser().get("RestaurantName").toString();
        temp_rest_name = temp_rest_name.replaceAll("[^a-zA-Z0-9]", "");

        order_done_class_name= temp_rest_name+"ODone";
        notify_cust_class_name=temp_rest_name+"NCustomer";
        order_out_for_delivery=temp_rest_name+"OOutForDelivery";

        final ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>(order_out_for_delivery);
        parseQuery.whereEqualTo("delivery_by",ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> object, ParseException e) {
                if(e==null)
                {
                    dbOrderPrompt.setText("");
                    Toast.makeText(delivaeryManOrderPending.this, ""+object.size(), Toast.LENGTH_SHORT).show();
                    if (object.size() == 0)
                    {
                        ParseQuery<ParseObject> parseQuery1=new ParseQuery<ParseObject>(order_done_class_name);
                        parseQuery1.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(final List<ParseObject> objects, ParseException e) {
                                if (e == null)
                                {

                                    if (objects.size() == 0)
                                    {
                                        dbOrderPrompt.setText("No orders for now!!");
                                    }
                                    else
                                    {
                                        Toast.makeText(delivaeryManOrderPending.this, objects.get(0).get("name").toString(), Toast.LENGTH_SHORT).show();
                                        int index = 0;
                                        //saving the last row of the table order pending into the order processing and deleting it from the order pending
                                        final ParseObject temp_order_in_process = new ParseObject(order_out_for_delivery);
                                        temp_order_in_process.put("order_id", objects.get(index).get("order_id"));
                                        temp_order_in_process.put("cust_name", objects.get(index).get("cust_name").toString());
                                        temp_order_in_process.put("cust_addr", objects.get(index).get("cust_addr").toString());
                                        curr_addr=objects.get(index).get("cust_addr").toString();
                                        temp_order_in_process.put("delivery_by", ParseUser.getCurrentUser().getUsername());
                                        temp_order_in_process.put("name", objects.get(index).get("name").toString());
                                        temp_order_in_process.put("total", objects.get(index).get("total"));

                                        String first_message = "Order for- " + objects.get(index).get("cust_name").toString();
                                        listOfOrders.add(first_message);

                                        String[] indi_dishes = objects.get(0).get("name").toString().split(",");
                                        for (int i = 0; i < indi_dishes.length; i++) {
                                            listOfOrders.add(indi_dishes[i]);
                                        }
                                        final String cust_name=objects.get(index).get("cust_name").toString();
                                        final int order_id=(int)objects.get(index).get("order_id");

                                        temp_order_in_process.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {

                                                    //deleting the parse row
                                                    objects.get(0).deleteInBackground(new DeleteCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            if (e == null) {
                                                                Toast.makeText(delivaeryManOrderPending.this, "Row added to the process and removed from the pending", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                    adapterofListOfOrders.notifyDataSetChanged();
                                                    doneButton.setVisibility(View.VISIBLE);
                                                    refreshButton.setVisibility(View.VISIBLE);
                                                    //update the cust_notifications here
                                                    ParseQuery<ParseObject> parseQuery2=new ParseQuery<ParseObject>(notify_cust_class_name);
                                                    parseQuery2.whereEqualTo("order_id",order_id);
                                                    parseQuery2.whereEqualTo("cust_name",cust_name);
                                                    parseQuery2.findInBackground(new FindCallback<ParseObject>() {
                                                        @Override
                                                        public void done(List<ParseObject> objects, ParseException e) {
                                                            if(e==null)
                                                            {
                                                                if(objects.size()==1)
                                                                {
                                                                    String message=objects.get(0).get("message").toString();
                                                                    String[] product_name=message.split("_",2);
                                                                    String out_for_delivery_message="Order out for delivery by- "+ParseUser.getCurrentUser().getUsername()
                                                                            +"_"+product_name[1];
                                                                    objects.get(0).put("message",out_for_delivery_message);
                                                                    objects.get(0).saveInBackground(new SaveCallback() {
                                                                        @Override
                                                                        public void done(ParseException e) {
                                                                            if(e==null)
                                                                            {
                                                                                Toast.makeText(delivaeryManOrderPending.this, "Notifications Updated", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });

                                                                }
                                                            }
                                                        }
                                                    });

                                                }
                                            }
                                        });

                                    }

                                }
                                else
                                {
                                    Toast.makeText(delivaeryManOrderPending.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(delivaeryManOrderPending.this, "Here in u lalalal", Toast.LENGTH_SHORT).show();
                        if(object.size()==1)
                        {

                            String first_message = "Order for- " + object.get(0).get("cust_name");
                            listOfOrders.add(first_message);
                            String[] indi_dishes = object.get(0).getString("name").split(",");
                            curr_addr=object.get(0).get("cust_addr").toString();
                            for (int i = 0; i < indi_dishes.length; i++) {
                                listOfOrders.add(indi_dishes[i]);
                            }
                            doneButton.setVisibility(View.VISIBLE);
                            refreshButton.setVisibility(View.VISIBLE);


                            adapterofListOfOrders.notifyDataSetChanged();

                        }
                        else
                        {
                            Toast.makeText(delivaeryManOrderPending.this, "SomeThing is Amiss", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });

    }

}