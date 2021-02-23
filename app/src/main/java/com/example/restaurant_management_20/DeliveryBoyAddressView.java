package com.example.restaurant_management_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Dictionary;
import java.util.Hashtable;

public class DeliveryBoyAddressView extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_address_view);
        Intent intent=getIntent();
        String addr=intent.getStringExtra("Addr");
        TextView addrTextView=findViewById(R.id.dbAddress);
        addrTextView.setText(addr);

    }
}