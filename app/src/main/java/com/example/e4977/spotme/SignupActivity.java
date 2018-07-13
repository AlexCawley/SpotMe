package com.example.e4977.spotme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends Activity
{
    /*--------------------------------------------------------------------------------------------*
     *  Constants                                                                                 *
     *--------------------------------------------------------------------------------------------*/
    private static final String TAG = SignupActivity.class.getSimpleName();

    /*--------------------------------------------------------------------------------------------*
     *  Member variables                                                                          *
     *--------------------------------------------------------------------------------------------*/
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SQLiteHandler db;

    /*--------------------------------------------------------------------------------------------*
     *  Member variables                                                                          *
     *--------------------------------------------------------------------------------------------*/
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /*----------------------------------------------------------------------------------------*
         *  Initialize Views                                                                      *
         *----------------------------------------------------------------------------------------*/
        inputFullName = findViewById(R.id.fullNameField);
        inputEmail = findViewById(R.id.emailAddressField);
        inputPassword = findViewById(R.id.passwordField);
        btnRegister = findViewById(R.id.registerButton);
        btnLinkToLogin = findViewById(R.id.backToLoginButton);

        /*----------------------------------------------------------------------------------------*
         *  initialize progressDialog                                                             *
         *----------------------------------------------------------------------------------------*/
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        /*----------------------------------------------------------------------------------------*
         *  Initialize SQLite DB                                                                  *
         *----------------------------------------------------------------------------------------*/
        db = new SQLiteHandler(this);

        /*----------------------------------------------------------------------------------------*
         *  Set register button on click listener                                                 *
         *----------------------------------------------------------------------------------------*/
        btnRegister.setOnClickListener(new View.OnClickListener()
        {

            /*------------------------------------------------------------------------------------*
             *  onClick                                                                           *
             *------------------------------------------------------------------------------------*/
            public void onClick(View view)
            {
                // Get the input from the user
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                /*--------------------------------------------------------------------------------*
                 *  If the name email and password are valid                                      *
                 *--------------------------------------------------------------------------------*/
                if (validate(name, email, password))
                {
                    // Register that user
                    registerUser(name, email, password);
                }

                /*--------------------------------------------------------------------------------*
                 *  Else if the name email and password are not valid                             *
                 *--------------------------------------------------------------------------------*/
                else
                {
                    // Notify the user
                    Toast.makeText(getApplicationContext(),
                                   "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        /*----------------------------------------------------------------------------------------*
         *  Set login button on click listener                                                    *
         *----------------------------------------------------------------------------------------*/
        btnLinkToLogin.setOnClickListener(new View.OnClickListener()
        {

            /*------------------------------------------------------------------------------------*
             *  onClick                                                                           *
             *------------------------------------------------------------------------------------*/
            public void onClick(View view)
            {
                // Take the app to the login page
                routeToLoginPage();
            }

        });

    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  Register User                                                                             *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Creates a new uer with the passed info and adds them to the db                            *
     *--------------------------------------------------------------------------------------------*/
    private void registerUser(String name, String email, String password)
    {
        // Show the loading indicator
        pDialog.setMessage("Registering ...");
        showDialog();

        /*----------------------------------------------------------------------------------------*
         *  If the email passed is not already associated with an account                         *
         *----------------------------------------------------------------------------------------*/
        if (!db.emailExists(email))
        {
            // Remove the processing indicator
            hideDialog();

            // Add that user to the database
            db.addUser(name, email, password);

            // Send the app to the login page
            routeToLoginPage();
        }

        /*----------------------------------------------------------------------------------------*
         *  If the email passed is already associated with an account                             *
         *----------------------------------------------------------------------------------------*/
        else
        {
            // Notify the user
            Toast.makeText(getApplicationContext(), "Email already associated with a user.", Toast.LENGTH_LONG).show();

            // Remove the processing indicator
            hideDialog();
        }
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  Validate                                                                                  *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Validates that the name email and password are in the correct format                      *
     *--------------------------------------------------------------------------------------------*/
    public boolean validate(String name, String email, String password)
    {
        // Initialize a boolean representing the validity of the parameters passed
        boolean valid;

        /*----------------------------------------------------------------------------------------*
         *  If the name is empty, the email does not look like an email, or the pass is empty     *
         *----------------------------------------------------------------------------------------*/
        if (name.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.isEmpty())
        {
            // Set valid to  false
            valid = false;
        }

        /*----------------------------------------------------------------------------------------*
         *  Else if the name and pass is not empty, and the email looks like an email             *
         *----------------------------------------------------------------------------------------*/
        else
        {
            // Set valid to true
            valid = true;
        }

        // Return the validity of the parameters
        return valid;
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  RouteToLoginPage                                                                          *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Sends app to login page                                                                   *
     *--------------------------------------------------------------------------------------------*/
    public void routeToLoginPage()
    {
        Intent i = new Intent(getApplicationContext(),
                              LoginActivity.class);
        startActivity(i);
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
