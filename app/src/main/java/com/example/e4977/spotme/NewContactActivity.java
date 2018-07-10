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

    Button addContactButton;
    EditText nameField;
    EditText emailAddressField;
    EditText phoneNumberField;
    SQLiteHandler db;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact_view);

        db = new SQLiteHandler(this);

        nameField = findViewById(R.id.nameField);
        emailAddressField = findViewById(R.id.emailAddressField);
        phoneNumberField = findViewById(R.id.phoneNumberField);

        addContactButton = findViewById(R.id.addContactButton);
        addContactButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                String name = nameField.getText().toString();
                String emailAddress = emailAddressField.getText().toString();
                String phoneNumber = phoneNumberField.getText().toString();
                if(validate(name, emailAddress, phoneNumber))
                {
                    db.addContact(name, emailAddress, phoneNumber);
                    Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Contact details not full, please try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validate(String name, String email, String phone)
    {
        boolean valid;

        if (name.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || phone.isEmpty())
        {
            valid = false;
        }
        else
        {
            if (phone.length() != 7 || phone.length() != 10)
            {
                valid = true;
            }
            else
            {
                valid = false;
            }
        }

        return valid;
    }

}
