package com.example.e4977.spotme;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class User
{
    /*--------------------------------------------------------------------------------------------*
     *  Constants                                                                                 *
     *--------------------------------------------------------------------------------------------*/
    private static final String TAG = User.class.getSimpleName();

    /*--------------------------------------------------------------------------------------------*
     *  Class variables                                                                           *
     *--------------------------------------------------------------------------------------------*/
    public String id;
    public String userName;
    public String email;
    public String password;
    public String date;

    /*--------------------------------------------------------------------------------------------*
     *  Constructor                                                                               *
     *--------------------------------------------------------------------------------------------*/
    public User(String id, String userName, String email, String password)
    {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;

        try
        {
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = simpleDateFormat.format(today);
            this.date = dateString;
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error", e);
            return;
        }
    }
}
