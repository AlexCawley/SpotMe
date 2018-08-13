package com.example.e4977.spotme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends Activity
{
    /*--------------------------------------------------------------------------------------------*
     *  Member variables                                                                          *
     *--------------------------------------------------------------------------------------------*/
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager sessionManager;

    /*--------------------------------------------------------------------------------------------*
     *  Member variables                                                                          *
     *--------------------------------------------------------------------------------------------*/
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

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
         *  Initialize Session manager                                                            *
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

        // Log method exit
        methodLogger.end();
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  Register User                                                                             *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Creates a new uer with the passed info and adds them to the db                            *
     *--------------------------------------------------------------------------------------------*/
    private void registerUser(final String name, final String email, final String password)
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        // String used to cancel request if needed
        String tag_string_req = "req_register";

        // Show the loading message
        pDialog.setMessage("Registering ...");
        showDialog();

        // Create the new string request
        StringRequest strReq = new StringRequest(Request.Method.POST,
                                                 AppConfig.URL_REGISTER, new Response.Listener<String>()
        {

            /*------------------------------------------------------------------------------------*
             *  onResponse                                                                        *
             *------------------------------------------------------------------------------------*/
            @Override
            public void onResponse(String response)
            {
                // Log method entry
                MethodLogger methodLogger = new MethodLogger();

                // Log the json that was returned
                methodLogger.d("Register Response: " + response.toString());

                // Hide the loading view
                hideDialog();

                // Try to get the json response
                try
                {
                    JSONObject jObj = new JSONObject(response);

                    // Check for an error in the response
                    boolean error = jObj.getBoolean("error");

                    /*----------------------------------------------------------------------------*
                     *  If no error was found                                                     *
                     *----------------------------------------------------------------------------*/
                    if (!error)
                    {
                        // Parse through the JSON
                        String user_id = jObj.getString("user_id");
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        sessionManager.setUser(new User(user_id, name, email, created_at));

                        // Indicate the success to the user
                        Toast.makeText(getApplicationContext(), "User successfully registered.", Toast.LENGTH_LONG).show();

                        // Take the user back to the login screen
                        routeToLoginPage();
                    }

                    /*----------------------------------------------------------------------------*
                     *  Else if an error was found                                                *
                     *----------------------------------------------------------------------------*/
                    else
                    {
                        // Notify the user
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                       errorMsg, Toast.LENGTH_LONG).show();
                    }
                }

                /*----------------------------------------------------------------------------*
                 * Catch any error in retrieving parsing etc JSON                             *
                 *----------------------------------------------------------------------------*/
                catch (JSONException e)
                {
                    e.printStackTrace();
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
                methodLogger.e("Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                               error.getMessage(), Toast.LENGTH_LONG).show();

                // Hide the processing view
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

                // add a new params object and add the new user data
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);

                // Log method exit
                methodLogger.end();

                // Return the params created
                return params;
            }

        };

        // Add the request to the queue to be sent to the php api
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        // Log method exit
        methodLogger.end();
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
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

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

        // Log method exit
        methodLogger.end();

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
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        Intent intent = new Intent(getApplicationContext(),
                                   LoginActivity.class);
        startActivity(intent);

        // Log method exit
        methodLogger.end();
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

        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
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
