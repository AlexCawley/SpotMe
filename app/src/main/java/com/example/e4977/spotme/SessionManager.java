package com.example.e4977.spotme;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.util.HashMap;

public class SessionManager
{
    /*--------------------------------------------------------------------------------------------*
     *  Constants                                                                                 *
     *--------------------------------------------------------------------------------------------*/
    private static final String PREF_NAME = "SpotMeLogin";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    /*--------------------------------------------------------------------------------------------*
     *  Member variables                                                                          *
     *--------------------------------------------------------------------------------------------*/
    private SharedPreferences pref;
    private Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;
    private static User user;

    /*--------------------------------------------------------------------------------------------*
     *  Constructor                                                                               *
     *--------------------------------------------------------------------------------------------*/
    public SessionManager(Context context)
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        /*----------------------------------------------------------------------------------------*
         *  If the user is logged in                                                              *
         *----------------------------------------------------------------------------------------*/
        if (isLoggedIn())
        {
            // Set the user field that can be accessed throughout the app
            SQLiteHandler db = new SQLiteHandler(context);
            HashMap<String, String> userMap = db.getUserDetails();
            user = new User(userMap.get("uid"), userMap.get("name"), userMap.get("email"), null);
        }

        // Log method exit
        methodLogger.end();
    }

    /*--------------------------------------------------------------------------------------------*
     *  Getters and setters                                                                       *
     *--------------------------------------------------------------------------------------------*/
    public void setLogin(boolean isLoggedIn)
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();

        methodLogger.d("User login session modified");

        // Log method exit
        methodLogger.end();
    }

    public static User getUser()
    {
        return user;
    }

    public boolean isLoggedIn()
    {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}
