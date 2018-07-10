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
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.emailField);
        inputPassword = findViewById(R.id.passwordField);
        btnLogin = findViewById(R.id.loginButton);
        btnLinkToRegister = findViewById(R.id.signupButton);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        db = new SQLiteHandler(getApplicationContext());

        btnLogin.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View view)
            {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (validate(email, password))
                {
                    checkLogin(email, password);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Username and password do not match, please try again", Toast.LENGTH_LONG)
                            .show();
                }

            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(),
                        SignupActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void checkLogin(final String email, final String password) {

        pDialog.setMessage("Logging in ...");
        showDialog();

        User currentUser = db.authenticateUser(new User(null, null, email, password));

        if (currentUser != null)
        {
            hideDialog();
            Intent intent = new Intent(LoginActivity.this, MainMenu.class);
            startActivity(intent);
            finish();
        }
        else
        {
            hideDialog();
            Toast.makeText(getApplicationContext(), "Username and password don't match, please try again.", Toast.LENGTH_LONG).show();
        }
    }

    public boolean validate(String email, String password) {
        boolean valid;

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.isEmpty())
        {
            valid = false;
        }
        else
        {
            if (password.length() > 5)
            {
                valid = true;
            }
            else
            {
                valid = false;
            }
        }

        return valid;
    }

    private void showDialog()
    {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog()
    {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
