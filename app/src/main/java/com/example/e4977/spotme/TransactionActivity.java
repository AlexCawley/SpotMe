package com.example.e4977.spotme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TransactionActivity extends AppCompatActivity
{
    /*--------------------------------------------------------------------------------------------*
     *  Class Variables                                                                           *
     *--------------------------------------------------------------------------------------------*/
    private SessionManager sessionManager;

    /*--------------------------------------------------------------------------------------------*
     *  onCreate                                                                                  *
     *--------------------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        /*----------------------------------------------------------------------------------------*
         *  Initialize Views                                                                      *
         *----------------------------------------------------------------------------------------*/
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.titlebar);

        /*----------------------------------------------------------------------------------------*
         *  Setup the toolbar                                                                     *
         *----------------------------------------------------------------------------------------*/
        setSupportActionBar(toolbar);

        // Enable the back button to take the user back to the home menu
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        /*----------------------------------------------------------------------------------------*
         *  Initialize Session                                                                    *
         *----------------------------------------------------------------------------------------*/
        sessionManager = new SessionManager(getApplicationContext());

        // Log method exit
        methodLogger.end();
    }

}
