package com.example.e4977.spotme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainMenu
        extends AppCompatActivity
{
    /*--------------------------------------------------------------------------------------------*
     *  Member variables                                                                          *
     *--------------------------------------------------------------------------------------------*/
    private Button btnLogout;
    private Button btnContact;
    private SQLiteHandler db;

    /*--------------------------------------------------------------------------------------------*
     *  onCreate                                                                                  *
     *--------------------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        /*----------------------------------------------------------------------------------------*
         *  Initialize Views                                                                      *
         *----------------------------------------------------------------------------------------*/
        btnLogout = findViewById(R.id.logoutButton);
        btnContact = findViewById(R.id.contactButton);

        // Initializes the SQLite database
        db = new SQLiteHandler(this);

        /*----------------------------------------------------------------------------------------*
         *  Set Logout Button on click listener                                                   *
         *----------------------------------------------------------------------------------------*/
        btnLogout.setOnClickListener(new View.OnClickListener()
        {

            /*------------------------------------------------------------------------------------*
             *  OnClick                                                                           *
             *------------------------------------------------------------------------------------*/
            @Override
            public void onClick(View v)
            {
                // Logout the user
                logoutUser();
            }
        });

        /*----------------------------------------------------------------------------------------*
         *  Set Contact Button on click listener                                                   *
         *----------------------------------------------------------------------------------------*/
        btnContact.setOnClickListener(new View.OnClickListener()
        {

            /*------------------------------------------------------------------------------------*
             *  OnClick                                                                           *
             *------------------------------------------------------------------------------------*/
            @Override
            public void onClick(View v)
            {
                // Send the app to the contact page
                routeToContactPage();
            }
        });
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
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  LogoutUser                                                                                *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Sends user back to login screen                                                           *
     *--------------------------------------------------------------------------------------------*/
    private void logoutUser()
    {
        Intent intent = new Intent(MainMenu.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
