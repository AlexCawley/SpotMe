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
    private Button btnLogout;

    private SQLiteHandler db;


    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btnLogout = findViewById(R.id.logoutButton);

        db = new SQLiteHandler(this);

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
        Intent intent = new Intent(MainMenu.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
