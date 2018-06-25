package com.example.e4977.spotme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainMenu
        extends AppCompatActivity
    {

    private final String[] MENU_OPTIONS = {"People", "Spots", "Calendar", "Options"};

    private LinearLayout menuWrapper;

    @Override
    protected void onCreate (Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        menuWrapper = findViewById(R.id.menuWrapper);

        for(int i = 0; i < MENU_OPTIONS.length; i++)
        {
            Button newOption = new Button(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                             LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            newOption.setLayoutParams(params);
            newOption.setText(MENU_OPTIONS[i]);
            menuWrapper.addView(newOption);
        }

        }
    }
