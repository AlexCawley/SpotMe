package com.example.e4977.spotme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class NewContactActivity
        extends AppCompatActivity
{

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact_view);

    }

    public void processNewContactInformation(View view)
    {
        EditText firstNameField = findViewById(R.id.firstNameField);
        String firstName = firstNameField.getText().toString();

        EditText lastNameField = findViewById(R.id.lastNameField);
        String lastName = lastNameField.getText().toString();

        EditText emailAddressField = findViewById(R.id.emailAddressField);
        String emailAddress = emailAddressField.getText().toString();

        EditText phoneNumberField = findViewById(R.id.phoneNumberField);
        String phoneNumber = phoneNumberField.getText().toString();

        if(firstName.isEmpty())
        {
            //DO NOTHING
        }
        else
        {
            Contact contact = new Contact(firstName, lastName, emailAddress, phoneNumber);
            Intent intent = new Intent(this, ContactActivity.class);
            intent.putExtra("EXTRA_CONTACT", contact);
        }
    }

}
