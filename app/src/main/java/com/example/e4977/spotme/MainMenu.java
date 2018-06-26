package com.example.e4977.spotme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainMenu
        extends AppCompatActivity
    {
    private LinearLayout menuWrapper;

    @Override
    protected void onCreate (Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        menuWrapper = findViewById(R.id.menuWrapper);
        }

        public void intentToContactPage(View view)
        {
            Intent intent = new Intent(this, ContactActivity.class);
            startActivity(intent);
        }
    }
