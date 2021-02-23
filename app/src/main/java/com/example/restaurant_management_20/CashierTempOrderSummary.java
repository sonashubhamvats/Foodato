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

public class CashierTempOrderSummary extends AppCompatActivity {

    ListView summaryListOfOrder;
    TextView totalPrice;
    EditText TableNoEditText;
    public static String tableNo;
    int totalPriceInt=0;
    ArrayAdapter<String> arrayAdapter;
    public void onClickOfOrderMoreCashier(View view)
    {
        finish();
    }

    public void OnClickOfProceedCashier(View view)
    {
        if(TableNoEditText.getText().toString().compareTo("")==0)
        {

            Toast.makeText(this, "Enter the TableNo to proceed!!!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            tableNo=TableNoEditText.getText().toString();
            Intent intent=new Intent(getApplicationContext(),CashierFinalBill.class);
            intent.putExtra("tp",totalPriceInt);
            startActivity(intent);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_temp_order_summary);
        summaryListOfOrder=findViewById(R.id.cashierListofTempOrder);

        totalPrice=findViewById(R.id.cashiertotalPrice);
        TableNoEditText=findViewById(R.id.cashierorderAddress);

        arrayAdapter=new ArrayAdapter<String>(this,R.layout.rows,CashierChooseDishFromMenu.selectedItems);
        summaryListOfOrder.setAdapter(arrayAdapter);

        for(int i=0;i<CashierChooseDishFromMenu.priceList.size();i++)
        {
            totalPriceInt+=CashierChooseDishFromMenu.priceList.get(i);
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
                new AlertDialog.Builder(CashierTempOrderSummary.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are u sure!!??")
                        .setMessage("This item will be deleted from ur list menu!! Proceed")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CashierChooseDishFromMenu.selectedItems.remove(position);
                                totalPriceInt-=CashierChooseDishFromMenu.priceList.get(position);
                                String temp_total_price_string=""+totalPriceInt;
                                totalPrice.setText(temp_total_price_string);
                                CashierChooseDishFromMenu.priceList.remove(position);
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