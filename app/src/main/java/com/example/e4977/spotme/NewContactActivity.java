package com.example.e4977.spotme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewContactActivity
        extends AppCompatActivity
{
    /*--------------------------------------------------------------------------------------------*
     *  Member variables                                                                          *
     *--------------------------------------------------------------------------------------------*/
    Button addContactButton;
    EditText nameField;
    EditText emailAddressField;
    EditText phoneNumberField;
    SQLiteHandler db;

    /*--------------------------------------------------------------------------------------------*
     *  onCreate                                                                                  *
     *--------------------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact_view);

        // Initializes the SQLite database
        db = new SQLiteHandler(this);

        /*----------------------------------------------------------------------------------------*
         *  Initialize Views                                                                      *
         *----------------------------------------------------------------------------------------*/
        nameField = findViewById(R.id.nameField);
        emailAddressField = findViewById(R.id.emailAddressField);
        phoneNumberField = findViewById(R.id.phoneNumberField);
        addContactButton = findViewById(R.id.addContactButton);

        /*----------------------------------------------------------------------------------------*
         *  Set addContact Button on click listener                                               *
         *----------------------------------------------------------------------------------------*/
        addContactButton.setOnClickListener(new View.OnClickListener()
        {

            /*------------------------------------------------------------------------------------*
             *  OnClick                                                                           *
             *------------------------------------------------------------------------------------*/
            @Override
            public void onClick(View v)
            {
                // Get the name email and phone input by the user
                String name = nameField.getText().toString();
                String emailAddress = emailAddressField.getText().toString();
                String phoneNumber = phoneNumberField.getText().toString();

                /*--------------------------------------------------------------------------------*
                 *  if the name phone and email are valid                                         *
                 *--------------------------------------------------------------------------------*/
                if (validate(name, emailAddress, phoneNumber))
                {
                    // Add the new contact to the database
                    db.addContact(name, emailAddress, phoneNumber);

                    // Send the app to the contact activity
                    routeToContactPage();
                }

                /*--------------------------------------------------------------------------------*
                 *  Else if the name phone and email are not valid                                *
                 *--------------------------------------------------------------------------------*/
                else
                {
                    // Notify the user
                    Toast.makeText(getApplicationContext(), "Contact details not full, please try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Log method exit
        methodLogger.end();
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  Validate                                                                                  *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Checks for the email and password passed to make sure they are valid inputs               *
     *--------------------------------------------------------------------------------------------*/
    private boolean validate(String name, String email, String phone)
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        // Initialize a boolean to represent the validity of the name email and phone passed
        boolean valid;

        /*----------------------------------------------------------------------------------------*
         *  If the name is empty, the email looks like and email, or the phone is empty           *
         *----------------------------------------------------------------------------------------*/
        if (name.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || phone.isEmpty())
        {
            // Set valid to false
            valid = false;
        }

        /*----------------------------------------------------------------------------------------*
         *  Else if the name and phone are not empty and the email looks like an email            *
         *----------------------------------------------------------------------------------------*/
        else
        {
            // Set valid to true if phone is 7 or 10 long and false if not
            valid = phone.length() != 7 || phone.length() != 10;
        }

        // Log method exit
        methodLogger.end();

        // Return validity of the name email and phone
        return valid;
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  RouteToContactPage                                                                        *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Sends app to contact page                                                                 *
     *--------------------------------------------------------------------------------------------*/
    public void routeToContactPage()
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
        startActivity(intent);

        // Log method exit
        methodLogger.end();
    }
}
