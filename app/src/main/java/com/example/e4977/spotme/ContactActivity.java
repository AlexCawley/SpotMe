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
    /*--------------------------------------------------------------------------------------------*
     *  Member variables                                                                          *
     *--------------------------------------------------------------------------------------------*/
    ArrayList<Contact> contacts;
    LinearLayout contactViewWrapper;
    SQLiteHandler db;

    /*--------------------------------------------------------------------------------------------*
     *  onCreate                                                                                  *
     *--------------------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);

        /*----------------------------------------------------------------------------------------*
         *  Initialize Views                                                                      *
         *----------------------------------------------------------------------------------------*/
        contactViewWrapper = findViewById(R.id.contactViewWrapper);

        // Initializes the SQLite database
        db = new SQLiteHandler(this);

        /*----------------------------------------------------------------------------------------*
         *  If the contacts array is null and the contact table exists in SQLite                  *
         *----------------------------------------------------------------------------------------*/
        if (contacts == null && SQLiteHandler.doesTableExist(db.getReadableDatabase(),
                                                             ContactDBContract.Contact.TABLE_NAME))
        {
            // Set the contacts array to all of the contacts held in the SQLite db
            contacts = db.getAllContacts();
        }

        /*----------------------------------------------------------------------------------------*
         *  If the contacts array is not null                                                     *
         *----------------------------------------------------------------------------------------*/
        if (contacts != null)
        {
            /*------------------------------------------------------------------------------------*
             *  Loop through all of the contacts                                                  *
             *------------------------------------------------------------------------------------*/
            for (int i = 0;
                 i < contacts.size();
                 i++)
            {
                // And display each one
                displayContact(contacts.get(i));
            }
        }
    }

    /*--------------------------------------------------------------------------------------------*
     *  DisplayContact                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Displays the contact object passed                                                        *
     *  Name:                                                                                     *
     *  Email:                                                                                    *
     *  Phone:                                                                                    *
     *--------------------------------------------------------------------------------------------*/
    public boolean displayContact(Contact contact)
    {
        /*----------------------------------------------------------------------------------------*
         *  Setup the layout params for the contact view                                          *
         *----------------------------------------------------------------------------------------*/
        LinearLayout contactWrapper = new LinearLayout(this);
        contactWrapper.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                         LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);

        /*----------------------------------------------------------------------------------------*
         *  Setup the name display                                                                *
         *----------------------------------------------------------------------------------------*/
        TextView name = new TextView(this);
        name.setText("Name: " + contact.getName());
        name.setLayoutParams(params);
        contactWrapper.addView(name);

        /*----------------------------------------------------------------------------------------*
         *  Setup the email display                                                               *
         *----------------------------------------------------------------------------------------*/
        TextView email = new TextView(this);
        email.setText("Email Address: " + contact.getEmail());
        email.setLayoutParams(params);
        contactWrapper.addView(email);

        /*----------------------------------------------------------------------------------------*
         *  Setup the phone display                                                               *
         *----------------------------------------------------------------------------------------*/
        TextView phoneNumber = new TextView(this);
        phoneNumber.setText("Phone Number: " + contact.getPhone());
        phoneNumber.setLayoutParams(params);
        contactWrapper.addView(phoneNumber);

        /*----------------------------------------------------------------------------------------*
         *  Add the created view to the contacts list                                             *
         *----------------------------------------------------------------------------------------*/
        contactViewWrapper.addView(contactWrapper);

        // Return true to indicate that the contacts were successfully added
        return true;
    }

    /*--------------------------------------------------------------------------------------------*
     *  RouteToNewContactPage                                                                     *
     *--------------------------------------------------------------------------------------------*
     *  Sends app to new contact page                                                             *
     *--------------------------------------------------------------------------------------------*/
    public void routeToNewContactPage()
    {
        Intent intent = new Intent(this, NewContactActivity.class);
        startActivity(intent);
    }
}
