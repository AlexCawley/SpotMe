package com.example.e4977.spotme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class NewContactActivity
        extends AppCompatActivity
{
    /*--------------------------------------------------------------------------------------------*
     *  Member variables                                                                          *
     *--------------------------------------------------------------------------------------------*/
    private Button addContactButton;
    private EditText nameField;
    private EditText emailAddressField;
    private EditText phoneNumberField;
    private ProgressDialog pDialog;
    private Context mContext;
    private SessionManager sessionManager;

    /*--------------------------------------------------------------------------------------------*
     *  onCreate                                                                                  *
     *--------------------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact_view);

        /*----------------------------------------------------------------------------------------*
         *  Initialize Views and context                                                          *
         *----------------------------------------------------------------------------------------*/
        nameField = findViewById(R.id.nameField);
        emailAddressField = findViewById(R.id.emailAddressField);
        phoneNumberField = findViewById(R.id.phoneNumberField);
        addContactButton = findViewById(R.id.addContactButton);
        mContext = this;

        /*----------------------------------------------------------------------------------------*
         *  Initialize Session                                                                    *
         *----------------------------------------------------------------------------------------*/
        sessionManager = new SessionManager(getApplicationContext());

        /*----------------------------------------------------------------------------------------*
         *  Set addContact Button on click listener                                               *
         *----------------------------------------------------------------------------------------*/
        addContactButton.setOnClickListener(new View.OnClickListener()
        {

            /*------------------------------------------------------------------------------------*
             *  OnClick                                                                           *
             *------------------------------------------------------------------------------------*/
            @Override
            public void onClick(View v)
            {
                // Get the name email and phone input by the user
                final String name = nameField.getText().toString();
                final String emailAddress = emailAddressField.getText().toString();
                String phoneNumber = phoneNumberField.getText().toString();

                /*--------------------------------------------------------------------------------*
                 *  if the name phone and email are valid                                         *
                 *--------------------------------------------------------------------------------*/
                if (validate(name, emailAddress, phoneNumber))
                {
                    /*----------------------------------------------------------------------------*
                     *  initialize progressDialog                                                 *
                     *----------------------------------------------------------------------------*/
                    pDialog = new ProgressDialog(mContext);
                    pDialog.setCancelable(false);

                    // String used to cancel request if necessary
                    String tag_string_req = "req_new_contact";

                    // Indicate the login process has started
                    pDialog.setMessage("Adding contact ...");
                    showDialog();

                    // Create the new string request
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_NEW_CONTACT, new Response.Listener<String>()
                    {

                        /*------------------------------------------------------------------------*
                         *  onResponse                                                            *
                         *------------------------------------------------------------------------*/
                        @Override
                        public void onResponse(String response)
                        {
                            // Log method entry
                            MethodLogger methodLogger = new MethodLogger();

                            // Log the JSON that was returned
                            methodLogger.d("New Contact Response: " + response.toString());

                            // Remove the processing view
                            hideDialog();

                            // Try to retrieve JSON and parse through it
                            try
                            {
                                JSONObject jObj = new JSONObject(response);

                                // Check for errors in the JSON passed back
                                boolean error = jObj.getBoolean("error");

                                /*----------------------------------------------------------------*
                                 *  If there was no error found                                   *
                                 *----------------------------------------------------------------*/
                                if (!error)
                                {
                                    // Indicate the success to the user
                                    Toast.makeText(getApplicationContext(), "Contact successfully created.", Toast.LENGTH_LONG).show();

                                    // Take the user back to the contact page
                                    routeToContactPage();
                                }

                                /*----------------------------------------------------------------*
                                 *  Else if there was an error found                              *
                                 *----------------------------------------------------------------*/
                                else
                                {
                                    // Notify the user
                                    String errorMsg = jObj.getString("message");
                                    Toast.makeText(getApplicationContext(),
                                                   errorMsg, Toast.LENGTH_LONG).show();
                                }
                            }

                            /*--------------------------------------------------------------------*
                             * Catch any error in retrieving parsing etc JSON                     *
                             *--------------------------------------------------------------------*/
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Error storing contact: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            // Log method exit
                            methodLogger.end();

                        }
                    }, new Response.ErrorListener()
                    {

                        /*------------------------------------------------------------------------*
                         *  onErrorResponse                                                       *
                         *------------------------------------------------------------------------*/
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            // Log method entry
                            MethodLogger methodLogger = new MethodLogger();

                            // Notify the user
                            methodLogger.e("Error storing contact: " + error.getMessage());
                            Toast.makeText(getApplicationContext(),
                                           error.getMessage(), Toast.LENGTH_LONG).show();
                            hideDialog();

                            // Log method exit
                            methodLogger.end();
                        }

                    })
                    {

                        /*------------------------------------------------------------------------*
                         *  getParams                                                             *
                         *------------------------------------------------------------------------*/
                        @Override
                        protected Map<String, String> getParams()
                        {
                            // Log method entry
                            MethodLogger methodLogger = new MethodLogger();

                            // create a new params object and add the contact data
                            Map<String, String> params = new HashMap<String, String>();

                            // Add the params to the volley request
                            params.put("user_id", sessionManager.getUserId());

                            methodLogger.d(String.format("===========USERID=========== %s", sessionManager.getUserId()));

                            params.put("name", name);
                            params.put("email", emailAddress);

                            // Log method exit
                            methodLogger.end();

                            // Return the user data
                            return params;
                        }

                    };

                    // Send the request string to the request queue to be sent to the PHP API
                    AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);

                    // Send the app to the contact activity
                    routeToContactPage();
                }

                /*--------------------------------------------------------------------------------*
                 *  Else if the name phone and email are not valid                                *
                 *--------------------------------------------------------------------------------*/
                else
                {
                    // Notify the user
                    Toast.makeText(getApplicationContext(), "Contact details not full, please try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Log method exit
        methodLogger.end();
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  Validate                                                                                  *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Checks for the email and password passed to make sure they are valid inputs               *
     *--------------------------------------------------------------------------------------------*/
    private boolean validate(String name, String email, String phone)
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        // Initialize a boolean to represent the validity of the name email and phone passed
        boolean valid;

        /*----------------------------------------------------------------------------------------*
         *  If the name is empty, the email looks like and email, or the phone is empty           *
         *----------------------------------------------------------------------------------------*/
        if (name.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || phone.isEmpty())
        {
            // Set valid to false
            valid = false;
        }

        /*----------------------------------------------------------------------------------------*
         *  Else if the name and phone are not empty and the email looks like an email            *
         *----------------------------------------------------------------------------------------*/
        else
        {
            // Set valid to true if phone is 7 or 10 long and false if not
            valid = phone.length() != 7 || phone.length() != 10;
        }

        // Log method exit
        methodLogger.end();

        // Return validity of the name email and phone
        return valid;
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  RouteToContactPage                                                                        *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Sends app to contact page                                                                 *
     *--------------------------------------------------------------------------------------------*/
    public void routeToContactPage()
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
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
