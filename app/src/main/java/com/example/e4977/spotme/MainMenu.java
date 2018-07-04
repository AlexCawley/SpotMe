package com.example.e4977.spotme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.HashMap;

public class MainMenu
        extends AppCompatActivity
{
    private LinearLayout menuWrapper;
    private Button btnLogout;

    private SQLiteHandler db;
    private SessionManager session;


    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        menuWrapper = findViewById(R.id.menuWrapper);
        btnLogout = findViewById(R.id.logoutButton);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn())
        {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    public void intentToContactPage(View view)
    {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }

    private void logoutUser()
    {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainMenu.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
