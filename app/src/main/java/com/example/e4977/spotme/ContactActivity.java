package com.example.e4977.spotme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactActivity
        extends AppCompatActivity
{
    ArrayList<Contact> contacts;
    LinearLayout contactViewWrapper;
    SQLiteHandler db;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);

        db = new SQLiteHandler(this);

        contactViewWrapper = findViewById(R.id.contactViewWrapper);
        if(contacts == null && SQLiteHandler.doesTableExist(db.getReadableDatabase(),
                                                            ContactDBContract.Contact.TABLE_NAME))
        {
            contacts = db.getAllContacts();
        }
        if(contacts != null)
        {
            for (int i = 0;
                 i < contacts.size();
                 i++)
            {
                displayContact(contacts.get(i));
            }
        }
    }

    public boolean displayContact(Contact contact)
    {
        LinearLayout contactWrapper = new LinearLayout(this);
        contactWrapper.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);

        TextView name = new TextView(this);
        name.setText("Name: " + contact.getName());
        name.setLayoutParams(params);
        contactWrapper.addView(name);

        TextView email = new TextView(this);
        email.setText("Email Address: " + contact.getEmail());
        email.setLayoutParams(params);
        contactWrapper.addView(email);

        TextView phoneNumber = new TextView(this);
        phoneNumber.setText("Phone Number: " + contact.getPhone());
        phoneNumber.setLayoutParams(params);
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
