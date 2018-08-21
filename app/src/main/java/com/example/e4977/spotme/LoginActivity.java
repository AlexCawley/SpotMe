package com.example.e4977.spotme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity
{
    /*--------------------------------------------------------------------------------------------*
     *  Member variables                                                                          *
     *--------------------------------------------------------------------------------------------*/
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager sessionManager;

    /*--------------------------------------------------------------------------------------------*
     *  OnCreate                                                                                  *
     *--------------------------------------------------------------------------------------------*/
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*----------------------------------------------------------------------------------------*
         *  Initialize Views                                                                      *
         *----------------------------------------------------------------------------------------*/
        inputEmail = findViewById(R.id.emailField);
        inputPassword = findViewById(R.id.passwordField);
        btnLogin = findViewById(R.id.loginButton);
        btnLinkToRegister = findViewById(R.id.signupButton);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.titlebar);

        /*----------------------------------------------------------------------------------------*
         *  Setup the toolbar                                                                     *
         *----------------------------------------------------------------------------------------*/
        setSupportActionBar(toolbar);

        /*----------------------------------------------------------------------------------------*
         *  initialize progressDialog                                                             *
         *----------------------------------------------------------------------------------------*/
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        /*----------------------------------------------------------------------------------------*
         *  Initialize Session                                                                    *
         *----------------------------------------------------------------------------------------*/
        sessionManager = new SessionManager(getApplicationContext());

        /*----------------------------------------------------------------------------------------*
         *  If the user is already logged in                                                      *
         *----------------------------------------------------------------------------------------*/
        if (sessionManager.isLoggedIn())
        {
            // Take the app to the main menu
            routeToMainMenu();
        }

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

        // Log method exit
        methodLogger.end();
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
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        // String used to cancel request if necessary
        String tag_string_req = "req_login";

        // Indicate the login process has started
        pDialog.setMessage("Logging in ...");
        showDialog();

        // Create the new string request
        StringRequest stringRequest = new StringRequest(Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>()
        {

            /*------------------------------------------------------------------------------------*
             *  onResponse                                                                        *
             *------------------------------------------------------------------------------------*/
            @Override
            public void onResponse(String response)
            {
                // Log method entry
                MethodLogger methodLogger = new MethodLogger();

                // Log the JSON that was returned
                methodLogger.d("Login Response: " + response.toString());

                // Remove the processing view
                hideDialog();

                // Try to retrieve JSON and parse through it
                try
                {
                    JSONObject jObj = new JSONObject(response);

                    // Check for errors in the JSON passed back
                    boolean error = jObj.getBoolean("error");

                    /*----------------------------------------------------------------------------*
                     *  If there was no error found                                               *
                     *----------------------------------------------------------------------------*/
                    if (!error)
                    {
                        // Set login to true
                        sessionManager.setLogin(true);

                        // Parse through the JSON data
                        String user_id = jObj.getString("user_id");
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");

                        // Add the new user data the session manager
                        sessionManager.setUser(new User(user_id, name, email, created_at));

                        methodLogger.d(String.format("User ID: %s", sessionManager.getUserId()));

                        // Take the app to the main menu
                        routeToMainMenu();
                    }

                    /*----------------------------------------------------------------------------*
                     *  Else if there was an error found                                          *
                     *----------------------------------------------------------------------------*/
                    else
                    {
                        // Notify the user
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                       errorMsg, Toast.LENGTH_LONG).show();
                    }
                }

                /*--------------------------------------------------------------------------------*
                 * Catch any error in retrieving parsing etc JSON                                 *
                 *--------------------------------------------------------------------------------*/
                catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Login Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                // Log method exit
                methodLogger.end();

            }
        }, new Response.ErrorListener()
        {

            /*------------------------------------------------------------------------------------*
             *  onErrorResponse                                                                   *
             *------------------------------------------------------------------------------------*/
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Log method entry
                MethodLogger methodLogger = new MethodLogger();

                // Notify the user
                methodLogger.e("Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                               error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();

                // Log method exit
                methodLogger.end();
            }

        })
        {

            /*------------------------------------------------------------------------------------*
             *  getParams                                                                         *
             *------------------------------------------------------------------------------------*/
            @Override
            protected Map<String, String> getParams()
            {
                // Log method entry
                MethodLogger methodLogger = new MethodLogger();

                // create a new params object and add the user data
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                // Log method exit
                methodLogger.end();

                // Return the user data
                return params;
            }

        };

        // Send the request string to the request queue to be sent to the PHP API
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
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
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

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

        // Log method exit
        methodLogger.end();

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
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        /*----------------------------------------------------------------------------------------*
         *  If the user is not logged in                                                          *
         *----------------------------------------------------------------------------------------*/
        if (!sessionManager.isLoggedIn())
        {
            // Log them in
            sessionManager.setLogin(true);
        }

        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(intent);

        // Log method exit
        methodLogger.end();
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
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        Intent intent = new Intent(getApplicationContext(),
                                   SignupActivity.class);
        startActivity(intent);

        // Log method exit
        methodLogger.end();
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
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        if (!pDialog.isShowing())
        {
            pDialog.show();
        }

        // Log method exit
        methodLogger.end();
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
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        if (pDialog.isShowing())
        {
            pDialog.dismiss();
        }

        // Log method exit
        methodLogger.end();
    }
}
