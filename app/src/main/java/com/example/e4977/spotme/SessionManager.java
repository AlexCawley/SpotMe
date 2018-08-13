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
    private static final String KEY_USER_ID = "userId";

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

    public String getUserId()
    {
        return pref.getString(KEY_USER_ID, "");
    }

    public void setUser(User user)
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        // Set the userID
        editor.putString(KEY_USER_ID, user.getId());
        editor.commit();

        // Set the user object
        this.user = user;

        // Log method exit
        methodLogger.end();
    }

    public boolean isLoggedIn()
    {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }


    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  LogoutUser                                                                                *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Resets the user fields and id in the preferences                                          *
     *--------------------------------------------------------------------------------------------*/
    public void logoutUser()
    {
        // Remove the user ID from the preferences
        editor.remove(KEY_USER_ID);

        // Set login to false
        this.setLogin(false);

        // Make the user null
        user = null;
    }

}
