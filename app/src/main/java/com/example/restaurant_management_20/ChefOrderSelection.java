package com.example.restaurant_management_20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ChefOrderSelection extends AppCompatActivity {

    TextView chefOrderPrompt;
    ListView listOfPendingOrders;
    ArrayList<String> listOfOrders=new ArrayList<String>();
    ArrayAdapter<String> adapterofListOfOrders;
    String order_processing_class_name,order_done_class_name,order_pending_class_name,notify_cashier_class_name;
    Button doneButton,refreshButton;
    public void onClickOfDoneChef(View view)
    {
        if(listOfOrders.size()>0)
        {

            ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>(order_processing_class_name);
            parseQuery.whereEqualTo("process_by", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        if (objects.size() == 1) {
                            if (objects.get(0).get("order_type").toString().compareTo("offline") == 0) {
                                ParseQuery<ParseObject> parseQuery1 = new ParseQuery<ParseObject>(notify_cashier_class_name);
                                parseQuery1.whereEqualTo("order_id", objects.get(0).get("order_id"));
                                parseQuery1.whereEqualTo("table_no", objects.get(0).get("table_no"));
                                parseQuery1.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if (e == null) {
                                            if (objects.size() == 1) {
                                                Toast.makeText(ChefOrderSelection.this, "here in offline", Toast.LENGTH_SHORT).show();
                                                String message = objects.get(0).get("message").toString();
                                                String[] partmessage = message.split("_", 2);
                                                message = "Order Done by- " + ParseUser.getCurrentUser().getUsername() + "_" + partmessage[1];
                                                objects.get(0).put("message", message);
                                                objects.get(0).saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {
                                                            Toast.makeText(ChefOrderSelection.this, "Updated", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                            }
                                        } else {
                                            Toast.makeText(ChefOrderSelection.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                objects.get(0).deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Toast.makeText(ChefOrderSelection.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                                            listOfOrders.clear();
                                            chefOrderPrompt.setText("No orders for now!!");
                                            adapterofListOfOrders.notifyDataSetChanged();
                                        }
                                    }
                                });

                            } else {

                                ParseObject done_dish = new ParseObject(order_done_class_name);
                                done_dish.put("order_id", objects.get(0).get("order_id"));
                                done_dish.put("cust_name", objects.get(0).get("cust_name").toString());
                                done_dish.put("cust_addr", objects.get(0).get("cust_addr").toString());
                                done_dish.put("name", objects.get(0).get("name").toString());
                                done_dish.put("total", objects.get(0).get("total"));
                                done_dish.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {

                                            Toast.makeText(ChefOrderSelection.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                objects.get(0).deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Toast.makeText(ChefOrderSelection.this, "Successfully deleted and added to the table", Toast.LENGTH_SHORT).show();
                                            listOfOrders.clear();
                                            adapterofListOfOrders.notifyDataSetChanged();

                                        }
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(ChefOrderSelection.this, "Something Amiss here", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
        else
        {
            Toast.makeText(this, "There are no orders!!", Toast.LENGTH_SHORT).show();
        }
    }
    public void OnClickOfRefreshChef(View view)
    {
        if(listOfOrders.size()==0)
        {
            final ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>(order_processing_class_name);
            parseQuery.whereEqualTo("process_by", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> object, ParseException e) {
                    if (e == null) {
                        chefOrderPrompt.setText("");
                        if (object.size() == 0) {
                            ParseQuery<ParseObject> parseQuery1 = new ParseQuery<ParseObject>(order_pending_class_name);
                            parseQuery1.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(final List<ParseObject> objects, ParseException e) {
                                    if (e == null) {

                                        if (objects.size() == 0) {
                                            chefOrderPrompt.setText("No orders for now!!");
                                        } else {
                                            Toast.makeText(ChefOrderSelection.this, objects.get(0).get("name").toString(), Toast.LENGTH_SHORT).show();
                                            if (objects.get(0).get("order_type").toString().compareTo("online") == 0) {
                                                int index = 0;
                                                //saving the last row of the table order pending into the order processing and deleting it from the order pending
                                                ParseObject temp_order_in_process = new ParseObject(order_processing_class_name);
                                                temp_order_in_process.put("order_id", objects.get(index).get("order_id"));
                                                temp_order_in_process.put("cust_name", objects.get(index).get("cust_name").toString());
                                                temp_order_in_process.put("cust_addr", objects.get(index).get("cust_addr").toString());
                                                temp_order_in_process.put("order_type", objects.get(index).get("order_type").toString());
                                                temp_order_in_process.put("process_by", ParseUser.getCurrentUser().getUsername().toString());
                                                temp_order_in_process.put("name", objects.get(index).get("name").toString());
                                                temp_order_in_process.put("total", objects.get(index).get("total"));

                                                String first_message = "Order for- " + objects.get(index).get("cust_name").toString();
                                                listOfOrders.add(first_message);

                                                String[] indi_dishes = objects.get(index).get("name").toString().split(",");
                                                for (int i = 0; i < indi_dishes.length; i++) {
                                                    listOfOrders.add(indi_dishes[i]);
                                                }


                                                temp_order_in_process.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {

                                                            //deleting the parse row
                                                            objects.get(0).deleteInBackground(new DeleteCallback() {
                                                                @Override
                                                                public void done(ParseException e) {
                                                                    if (e == null) {
                                                                        Toast.makeText(ChefOrderSelection.this, "Row added to the process and removed from the pending", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                            adapterofListOfOrders.notifyDataSetChanged();
                                                            doneButton.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                });
                                            } else {
                                                int index = 0;
                                                //saving the last row of the table order pending into the order processing and deleting it from the order pending
                                                ParseObject temp_order_in_process = new ParseObject(order_processing_class_name);
                                                temp_order_in_process.put("order_id", objects.get(index).get("order_id"));
                                                temp_order_in_process.put("table_no", objects.get(index).get("table_no").toString());
                                                temp_order_in_process.put("order_type", objects.get(index).get("order_type").toString());
                                                temp_order_in_process.put("process_by", ParseUser.getCurrentUser().getUsername().toString());
                                                temp_order_in_process.put("name", objects.get(index).get("name").toString());
                                                temp_order_in_process.put("total", objects.get(index).get("total"));

                                                String first_message = "Order for- " + objects.get(index).get("table_no").toString();
                                                listOfOrders.add(first_message);

                                                String[] indi_dishes = objects.get(0).get("name").toString().split(",");
                                                for (int i = 0; i < indi_dishes.length; i++) {
                                                    listOfOrders.add(indi_dishes[i]);
                                                }
                                                Toast.makeText(ChefOrderSelection.this, "Here too 2323", Toast.LENGTH_SHORT).show();

                                                temp_order_in_process.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {
                                                            //deleting the parse row
                                                            objects.get(0).deleteInBackground(new DeleteCallback() {
                                                                @Override
                                                                public void done(ParseException e) {
                                                                    if (e == null) {
                                                                        Toast.makeText(ChefOrderSelection.this, "Row added to the process and removed from the pending", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                            adapterofListOfOrders.notifyDataSetChanged();
                                                            doneButton.setVisibility(View.VISIBLE);
                                                        } else {
                                                            Toast.makeText(ChefOrderSelection.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                            }

                                        }

                                    } else {
                                        Toast.makeText(ChefOrderSelection.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "First complete the current order", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_order_selection);
        doneButton=findViewById(R.id.chefCompleteOrder);
        doneButton.setVisibility(View.INVISIBLE);
        refreshButton=findViewById(R.id.chefRefresh);
        refreshButton.setVisibility(View.INVISIBLE);
        chefOrderPrompt=findViewById(R.id.chefOrderPrompt);
        listOfPendingOrders=findViewById(R.id.cheflistofordersPending);
        adapterofListOfOrders=new ArrayAdapter<String>(this,R.layout.rows,listOfOrders);
        listOfPendingOrders.setAdapter(adapterofListOfOrders);
        String temp_rest_name= ParseUser.getCurrentUser().get("RestaurantName").toString();
        temp_rest_name = temp_rest_name.replaceAll("[^a-zA-Z0-9]", "");

        order_done_class_name= temp_rest_name+"ODone";
        order_pending_class_name=temp_rest_name+"OPending";
        order_processing_class_name=temp_rest_name+"OProcessing";
        notify_cashier_class_name=temp_rest_name+"NCashier";

        final ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>(order_processing_class_name);
        parseQuery.whereEqualTo("process_by",ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> object, ParseException e) {
                if(e==null)
                {
                    chefOrderPrompt.setText("");
                    if (object.size() == 0)
                    {
                        ParseQuery<ParseObject> parseQuery1=new ParseQuery<ParseObject>(order_pending_class_name);
                        parseQuery1.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(final List<ParseObject> objects, ParseException e) {
                                if (e == null)
                                {

                                    if (objects.size() == 0)
                                    {
                                        chefOrderPrompt.setText("No orders for now!!");
                                    }
                                    else
                                    {
                                        Toast.makeText(ChefOrderSelection.this, objects.get(0).get("name").toString(), Toast.LENGTH_SHORT).show();
                                        if(objects.get(0).get("order_type").toString().compareTo("online")==0)
                                        {
                                            int index = 0;
                                            //saving the last row of the table order pending into the order processing and deleting it from the order pending
                                            ParseObject temp_order_in_process = new ParseObject(order_processing_class_name);
                                            temp_order_in_process.put("order_id", objects.get(index).get("order_id"));
                                            temp_order_in_process.put("cust_name", objects.get(index).get("cust_name").toString());
                                            temp_order_in_process.put("cust_addr", objects.get(index).get("cust_addr").toString());
                                            temp_order_in_process.put("order_type", objects.get(index).get("order_type").toString());
                                            temp_order_in_process.put("process_by", ParseUser.getCurrentUser().getUsername().toString());
                                            temp_order_in_process.put("name", objects.get(index).get("name").toString());
                                            temp_order_in_process.put("total", objects.get(index).get("total"));

                                            String first_message = "Order for- " + objects.get(index).get("cust_name").toString();
                                            listOfOrders.add(first_message);

                                            String[] indi_dishes = objects.get(0).get("name").toString().split(",");
                                            for (int i = 0; i < indi_dishes.length; i++) {
                                                listOfOrders.add(indi_dishes[i]);
                                            }


                                            temp_order_in_process.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {

                                                        //deleting the parse row
                                                        objects.get(0).deleteInBackground(new DeleteCallback() {
                                                            @Override
                                                            public void done(ParseException e) {
                                                                if (e == null) {
                                                                    Toast.makeText(ChefOrderSelection.this, "Row added to the process and removed from the pending", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        adapterofListOfOrders.notifyDataSetChanged();
                                                        doneButton.setVisibility(View.VISIBLE);
                                                        refreshButton.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            });
                                        }
                                        else
                                        {
                                            int index = 0;
                                            //saving the last row of the table order pending into the order processing and deleting it from the order pending
                                            ParseObject temp_order_in_process = new ParseObject(order_processing_class_name);
                                            temp_order_in_process.put("order_id", objects.get(index).get("order_id"));
                                            temp_order_in_process.put("table_no", objects.get(index).get("table_no").toString());
                                            temp_order_in_process.put("order_type", objects.get(index).get("order_type").toString());
                                            temp_order_in_process.put("process_by", ParseUser.getCurrentUser().getUsername().toString());
                                            temp_order_in_process.put("name", objects.get(index).get("name").toString());
                                            temp_order_in_process.put("total", objects.get(index).get("total"));

                                            String first_message = "Order for- " + objects.get(index).get("table_no").toString();
                                            listOfOrders.add(first_message);

                                            String[] indi_dishes = objects.get(0).get("name").toString().split(",");
                                            for (int i = 0; i < indi_dishes.length; i++) {
                                                listOfOrders.add(indi_dishes[i]);
                                            }
                                            Toast.makeText(ChefOrderSelection.this, "Here too 2323", Toast.LENGTH_SHORT).show();

                                            temp_order_in_process.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        //deleting the parse row
                                                        objects.get(0).deleteInBackground(new DeleteCallback() {
                                                            @Override
                                                            public void done(ParseException e) {
                                                                if (e == null) {
                                                                    Toast.makeText(ChefOrderSelection.this, "Row added to the process and removed from the pending", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        adapterofListOfOrders.notifyDataSetChanged();
                                                        doneButton.setVisibility(View.VISIBLE);
                                                        refreshButton.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(ChefOrderSelection.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        }

                                    }

                                }
                                else
                                {
                                    Toast.makeText(ChefOrderSelection.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(ChefOrderSelection.this, "Here in", Toast.LENGTH_SHORT).show();
                        if(object.size()==1)
                        {
                            if(object.get(0).get("order_type").toString().compareTo("online")==0)
                            {
                                String first_message = "Order for- " + object.get(0).get("cust_name");
                                listOfOrders.add(first_message);
                                String[] indi_dishes = object.get(0).getString("name").split(",");
                                for (int i = 0; i < indi_dishes.length; i++) {
                                    listOfOrders.add(indi_dishes[i]);
                                }
                                doneButton.setVisibility(View.VISIBLE);
                                refreshButton.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                String first_message = "Order for table no - " + object.get(0).get("table_no");
                                listOfOrders.add(first_message);
                                String[] indi_dishes = object.get(0).getString("name").split(",");
                                for (int i = 0; i < indi_dishes.length; i++) {
                                    listOfOrders.add(indi_dishes[i]);
                                }
                                doneButton.setVisibility(View.VISIBLE);
                                refreshButton.setVisibility(View.VISIBLE);
                            }
                            adapterofListOfOrders.notifyDataSetChanged();

                        }
                        else
                        {
                            Toast.makeText(ChefOrderSelection.this, "SomeThing is Amiss", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });

    }
}