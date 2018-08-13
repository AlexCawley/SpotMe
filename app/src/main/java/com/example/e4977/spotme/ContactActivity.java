package com.example.e4977.spotme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactActivity
        extends AppCompatActivity
{
    /*--------------------------------------------------------------------------------------------*
     *  Member variables                                                                          *
     *--------------------------------------------------------------------------------------------*/
    private ArrayList<Contact> contacts;
    private LinearLayout contactViewWrapper;
    private Button newContactButton;
    private ProgressDialog pDialog;
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
        setContentView(R.layout.activity_contact_view);

        /*----------------------------------------------------------------------------------------*
         *  Initialize Views                                                                      *
         *----------------------------------------------------------------------------------------*/
        contactViewWrapper = findViewById(R.id.contactViewWrapper);
        newContactButton = findViewById(R.id.newContactButton);

        /*----------------------------------------------------------------------------------------*
         *  Initialize Session                                                                    *
         *----------------------------------------------------------------------------------------*/
        sessionManager = new SessionManager(getApplicationContext());

        /*----------------------------------------------------------------------------------------*
         *  initialize progressDialog                                                             *
         *----------------------------------------------------------------------------------------*/
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // String used to cancel request if necessary
        String tag_string_req = "req_contacts";

        // Indicate the login process has started
        pDialog.setMessage("Getting contacts ...");
        showDialog();

        // Create the new string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_CONTACTS, new Response.Listener<String>()
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
                methodLogger.d("Contact query Response: " + response.toString());

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
                        // Parse through the JSON data
                        String uid = jObj.getString("uid");
                        JSONObject contact = jObj.getJSONObject("contact");
                        String name = contact.getString("name");
                        String email = contact.getString("email");
                        String created_at = contact.getString("created_at");
                        String updated_at = contact.getString("updated_at");
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
                    Toast.makeText(getApplicationContext(), "Error finding contacts: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                methodLogger.e("Error finding contacts: " + error.getMessage());
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
                params.put("user_id", sessionManager.getUserId());

                // Log method exit
                methodLogger.end();

                // Return the user data
                return params;
            }

        };

        // Send the request string to the request queue to be sent to the PHP API
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);

        /*----------------------------------------------------------------------------------------*
         *  If the contacts array is not null                                                     *
         *----------------------------------------------------------------------------------------*/
        if (contacts != null)
        {
            /*------------------------------------------------------------------------------------*
             *  Loop through all of the contacts                                                  *
             *------------------------------------------------------------------------------------*/
            for (int i = 0;
                 i < contacts.size();
                 i++)
            {
                // And display each one
                displayContact(contacts.get(i));
            }
        }

        /*----------------------------------------------------------------------------------------*
         *  Set new contact button on click listener                                              *
         *----------------------------------------------------------------------------------------*/
        newContactButton.setOnClickListener(new View.OnClickListener()
        {

            /*------------------------------------------------------------------------------------*
             *  OnClick                                                                           *
             *------------------------------------------------------------------------------------*/
            @Override
            public void onClick(View v)
            {
                // Send the app to the new contact page
                routeToNewContactPage();
            }
        });

        // Log method exit
        methodLogger.end();
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  DisplayContact                                                                            *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Displays the contact object passed                                                        *
     *  Name:                                                                                     *
     *  Email:                                                                                    *
     *  Phone:                                                                                    *
     *--------------------------------------------------------------------------------------------*/
    public boolean displayContact(Contact contact)
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        /*----------------------------------------------------------------------------------------*
         *  Setup the layout params for the contact view                                          *
         *----------------------------------------------------------------------------------------*/
        LinearLayout contactWrapper = new LinearLayout(this);
        contactWrapper.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                         LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);

        /*----------------------------------------------------------------------------------------*
         *  Setup the name display                                                                *
         *----------------------------------------------------------------------------------------*/
        TextView name = new TextView(this);
        name.setText("Name: " + contact.getName());
        name.setLayoutParams(params);
        contactWrapper.addView(name);

        /*----------------------------------------------------------------------------------------*
         *  Setup the email display                                                               *
         *----------------------------------------------------------------------------------------*/
        TextView email = new TextView(this);
        email.setText("Email Address: " + contact.getEmail());
        email.setLayoutParams(params);
        contactWrapper.addView(email);

        /*----------------------------------------------------------------------------------------*
         *  Setup the phone display                                                               *
         *----------------------------------------------------------------------------------------*/
        TextView phoneNumber = new TextView(this);
        phoneNumber.setText("Phone Number: " + contact.getPhone());
        phoneNumber.setLayoutParams(params);
        contactWrapper.addView(phoneNumber);

        /*----------------------------------------------------------------------------------------*
         *  Add the created view to the contacts list                                             *
         *----------------------------------------------------------------------------------------*/
        contactViewWrapper.addView(contactWrapper);

        // Log method exit
        methodLogger.end();

        // Return true to indicate that the contacts were successfully added
        return true;
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  RouteToNewContactPage                                                                     *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Sends app to new contact page                                                             *
     *--------------------------------------------------------------------------------------------*/
    public void routeToNewContactPage()
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        Intent intent = new Intent(this, NewContactActivity.class);
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
