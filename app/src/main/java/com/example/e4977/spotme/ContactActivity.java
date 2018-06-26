package com.example.e4977.spotme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ContactActivity
        extends AppCompatActivity
{

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);

    }

    public void intentToNewContactPage(View view)
    {
        Intent intent = new Intent(this, NewContactActivity.class);
        startActivity(intent);
    }

}
