package com.example.e4977.spotme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends Activity {
    private static final String TAG = SignupActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        inputFullName = findViewById(R.id.fullNameField);
        inputEmail = findViewById(R.id.emailAddressField);
        inputPassword = findViewById(R.id.passwordField);
        btnRegister = findViewById(R.id.registerButton);
        btnLinkToLogin = findViewById(R.id.backToLoginButton);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        db = new SQLiteHandler(this);

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (validate(name, email, password))
                {
                    registerUser(name, email, password);
                }
                else
                    {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        btnLinkToLogin.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }

        });

    }

    private void registerUser(String name, String email, String password)
    {
        pDialog.setMessage("Registering ...");
        showDialog();

        if(!db.emailExists(email))
        {
            db.addUser(name, email, password);
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            hideDialog();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Email already associated with a user.", Toast.LENGTH_LONG).show();
            hideDialog();
        }
    }

    public boolean validate(String name, String email, String password) {
        boolean valid;

        if (name.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.isEmpty())
        {
            valid = false;
        }
        else
        {
            if (name.length() > 5 || password.length() > 5)
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
