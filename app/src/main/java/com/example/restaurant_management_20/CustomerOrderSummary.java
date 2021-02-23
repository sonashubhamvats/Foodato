package com.example.restaurant_management_20;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;

public class CustomerOrderSummary extends AppCompatActivity {

    ListView summaryListOfOrder;
    TextView totalPrice;
    EditText addressEditText;
    public static String address;
    int totalPriceInt=0;
    ArrayAdapter<String> arrayAdapter;
    public void onClickOfOrderMore(View view)
    {
        finish();
    }

    public void OnClickOfProceed(View view)
    {
        if(addressEditText.getText().toString().compareTo("")==0)
        {

            Toast.makeText(this, "Enter your address to proceed!!!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            address=addressEditText.getText().toString();
            Intent intent=new Intent(getApplicationContext(),CustomerFinalBill.class);
            intent.putExtra("tp",totalPriceInt);
            startActivity(intent);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_summary);
        summaryListOfOrder=findViewById(R.id.custListofTempOrder);

        totalPrice=findViewById(R.id.custtotalPrice);
        addressEditText=findViewById(R.id.custorderAddress);

        arrayAdapter=new ArrayAdapter<String>(this,R.layout.rows,CustomerMenuDishSelection.selectedItems);
        summaryListOfOrder.setAdapter(arrayAdapter);

        for(int i=0;i<CustomerMenuDishSelection.priceList.size();i++)
        {
            totalPriceInt+=CustomerMenuDishSelection.priceList.get(i);
        }


        String temp_total_price_string=""+totalPriceInt;
        totalPrice.setText(temp_total_price_string);
        SetOnItemLongClickListener(summaryListOfOrder);
    }
    public void SetOnItemLongClickListener(ListView list)
    {
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(CustomerOrderSummary.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are u sure!!??")
                        .setMessage("This item will be deleted from ur list menu!! Proceed")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CustomerMenuDishSelection.selectedItems.remove(position);
                                totalPriceInt-=CustomerMenuDishSelection.priceList.get(position);
                                String temp_total_price_string=""+totalPriceInt;
                                totalPrice.setText(temp_total_price_string);
                                CustomerMenuDishSelection.priceList.remove(position);
                                arrayAdapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("No",null)
                        .show();

                return true;
            }
        });
    }

}