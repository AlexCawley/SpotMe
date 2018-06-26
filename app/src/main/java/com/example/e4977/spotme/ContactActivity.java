package com.example.e4977.spotme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactActivity
        extends AppCompatActivity
{

    ArrayList<Contact> contacts;
    ScrollView contactViewWrapper;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);

        contactViewWrapper = findViewById(R.id.contactViewWrapper);
        if(contacts == null)
        {
            contacts = new ArrayList<Contact>();
        }
        Contact contact = getIntent().getParcelableExtra("EXTRA_CONTACT");
        if(contact != null)
        {
            contacts.add(contact);
        }
        for(int i = 0; i < contacts.size(); i++)
        {
            displayContact(contacts.get(i));
        }
    }

    public boolean displayContact(Contact contact)
    {
        LinearLayout contactWrapper = new LinearLayout(this);
        contactWrapper.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);

        LinearLayout nameWrapper = new LinearLayout(this);
        nameWrapper.setOrientation(LinearLayout.HORIZONTAL);
        TextView firstName = new TextView(this);
        firstName.setText("First Name: " + contact.getFirstName());
        TextView lastName = new TextView(this);
        lastName.setText("Last Name: " + contact.getLastName());
        firstName.setLayoutParams(params);
        lastName.setLayoutParams(params);
        nameWrapper.addView(firstName);
        nameWrapper.addView(lastName);
        contactWrapper.addView(nameWrapper);

        TextView email = new TextView(this);
        email.setText("Email Address: " + contact.getEmail());
        TextView phoneNumber = new TextView(this);
        phoneNumber.setText("Phone Number: " + contact.getPhone());
        email.setLayoutParams(params);
        phoneNumber.setLayoutParams(params);
        contactWrapper.addView(email);
        contactWrapper.addView(phoneNumber);
        contactViewWrapper.addView(contactWrapper);
        return true;
    }

    public void intentToNewContactPage(View view)
    {
        Intent intent = new Intent(this, NewContactActivity.class);
        startActivity(intent);
    }

}
