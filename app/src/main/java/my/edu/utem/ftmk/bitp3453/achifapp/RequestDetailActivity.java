package com.bitp3453.bitis1g1.projectrequest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;


public class RequestDetailActivity extends AppCompatActivity {

    private TextView txtname, txtphonenumber, txtquantity, txtdescription, txtrequestid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.);

        String name = getIntent().getStringExtra("name");
        String quantity = getIntent().getStringExtra("quantity");
        String description = getIntent().getStringExtra("description");
        String phonenumber = getIntent().getStringExtra("phonenumber");
        String requestid = getIntent().getStringExtra("requestid");

        txtname = findViewById(R.id.);
        txtphonenumber = findViewById(R.id.);
        txtquantity = findViewById(R.id.);
        txtdescription = findViewById(R.id.);
        txtrequestid = findViewById(R.id.);

        txtname.setText(name);
        txtphonenumber.setText(phonenumber);
        txtdescription.setText(description);
        txtrequestid.setText(requestid);
        txtquantity.setText(quantity);
    }
}
