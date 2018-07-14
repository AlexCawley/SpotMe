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
    private SessionManager sessionManager;

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
                // Log the json that was returned
                Log.d(TAG, "Register Response: " + response.toString());

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
                        String uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Add the user to the db
                        db.addUser(name, email, uid, created_at);

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

            }
        }, new Response.ErrorListener()
        {

            /*------------------------------------------------------------------------------------*
             *  onErrorResponse                                                                   *
             *------------------------------------------------------------------------------------*/
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Notify the user
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                               error.getMessage(), Toast.LENGTH_LONG).show();

                // Hide the processing view
                hideDialog();
            }
        })
        {

            /*------------------------------------------------------------------------------------*
             *  getParams                                                                         *
             *------------------------------------------------------------------------------------*/
            @Override
            protected Map<String, String> getParams()
            {
                // add a new params object and add the new user data
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);

                // Return the params created
                return params;
            }

        };

        // Add the request to the queue to be sent to the php api
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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
