package com.example.e4977.spotme;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity
{
    /*--------------------------------------------------------------------------------------------*
     *  Constants                                                                                 *
     *--------------------------------------------------------------------------------------------*/
    private static final String TAG = LoginActivity.class.getSimpleName();

    /*--------------------------------------------------------------------------------------------*
     *  Member variables                                                                          *
     *--------------------------------------------------------------------------------------------*/
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SQLiteHandler db;

    /*--------------------------------------------------------------------------------------------*
     *  OnCreate                                                                                  *
     *--------------------------------------------------------------------------------------------*/
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*----------------------------------------------------------------------------------------*
         *  Initialize Views                                                                      *
         *----------------------------------------------------------------------------------------*/
        inputEmail = findViewById(R.id.emailField);
        inputPassword = findViewById(R.id.passwordField);
        btnLogin = findViewById(R.id.loginButton);
        btnLinkToRegister = findViewById(R.id.signupButton);

        /*----------------------------------------------------------------------------------------*
         *  initialize progressDialog                                                             *
         *----------------------------------------------------------------------------------------*/
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        /*----------------------------------------------------------------------------------------*
         *  Initialize SQLite DB                                                                  *
         *----------------------------------------------------------------------------------------*/
        db = new SQLiteHandler(getApplicationContext());

        /*----------------------------------------------------------------------------------------*
         *  Set login button on click listener                                                    *
         *----------------------------------------------------------------------------------------*/
        btnLogin.setOnClickListener(new View.OnClickListener()
        {

            /*------------------------------------------------------------------------------------*
             *  onClick                                                                           *
             *------------------------------------------------------------------------------------*/
            public void onClick(View view)
            {
                // Get the email and passwords that were typed in
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                /*--------------------------------------------------------------------------------*
                 *  If the email and password passed are in valid format                          *
                 *--------------------------------------------------------------------------------*/
                if (validate(email, password))
                {
                    // Check the email an password for a match
                    checkLogin(email, password);
                }

                /*--------------------------------------------------------------------------------*
                 *  Else if the email and password passed are not in valid format                 *
                 *--------------------------------------------------------------------------------*/
                else
                {
                    // Tell the user to try again
                    Toast.makeText(getApplicationContext(),
                                   "Username and password do not match, please try again", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        /*----------------------------------------------------------------------------------------*
         *  Set register button on click listener                                                 *
         *----------------------------------------------------------------------------------------*/
        btnLinkToRegister.setOnClickListener(new View.OnClickListener()
        {

            /*------------------------------------------------------------------------------------*
             *  onClick                                                                           *
             *------------------------------------------------------------------------------------*/
            public void onClick(View view)
            {
                // Take the app to the register page
                routeToSignupPage();
            }
        });
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  CheckLogin                                                                                *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Checks for the email and password passed in the database                                  *
     *--------------------------------------------------------------------------------------------*/
    private void checkLogin(final String email, final String password)
    {
        // Indicate the login process has started
        pDialog.setMessage("Logging in ...");
        showDialog();

        // Checks the database for a user with the same email and password
        User currentUser = db.authenticateUser(new User(null, null, email, password));

        /*----------------------------------------------------------------------------------------*
         *  If a user with the same credentials is found                                          *
         *----------------------------------------------------------------------------------------*/
        if (currentUser != null)
        {
            // Remove loading indicator
            hideDialog();

            // Move the app to the main menu
            routeToMainMenu();
        }

        /*----------------------------------------------------------------------------------------*
         *  Else if a user with the same credentials was not found                                *
         *----------------------------------------------------------------------------------------*/
        else
        {
            // Remove loading indicator
            hideDialog();

            // Alert the user to try again
            Toast.makeText(getApplicationContext(), "Username and password don't match, please try again.", Toast.LENGTH_LONG).show();
        }
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  Validate                                                                                  *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Checks for the email and password passed to make sure they are valid inputs               *
     *--------------------------------------------------------------------------------------------*/
    public boolean validate(String email, String password)
    {
        // Initialize a boolean indicating whether the email and password passed are valid
        boolean valid;

        /*----------------------------------------------------------------------------------------*
         *  If the email does not look like an email or the password is empty                     *
         *----------------------------------------------------------------------------------------*/
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.isEmpty())
        {
            // Set valid to false
            valid = false;
        }

        /*----------------------------------------------------------------------------------------*
         *  Else if the email is an email and the password is not empty                           *
         *----------------------------------------------------------------------------------------*/
        else
        {
            // Set valid to true if the length of the password is > and false otherwise
            valid = password.length() > 5;
        }

        // Return the result
        return valid;
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  RouteToNewMainMenu                                                                        *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Sends app to main menu                                                                    *
     *--------------------------------------------------------------------------------------------*/
    public void routeToMainMenu()
    {
        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(intent);
        finish();
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  RouteToNewSignupPage                                                                      *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Sends app to sign up page                                                                 *
     *--------------------------------------------------------------------------------------------*/
    public void routeToSignupPage()
    {
        Intent intent = new Intent(getApplicationContext(),
                                   SignupActivity.class);
        startActivity(intent);
        finish();
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  ShowDialog                                                                                *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Shows the progress dialog                                                                 *
     *--------------------------------------------------------------------------------------------*/
    private void showDialog()
    {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  HideDialog                                                                                *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Hides the progress dialog                                                                 *
     *--------------------------------------------------------------------------------------------*/
    private void hideDialog()
    {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
