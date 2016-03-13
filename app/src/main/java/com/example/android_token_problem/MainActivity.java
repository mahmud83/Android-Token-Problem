package com.example.android_token_problem;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    private static final int REQUEST_CODE_SIGN_IN_REQUIRED = 2000;

    private String accountEmail;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickUserAccount();
    }

    private void pickUserAccount() {
        // Show a dialog asking which Google account to use, if the user has more
        // than one Google account linked to the device.
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
                if (data != null) {
                    // With the account email name acquired, go get the auth token.
                    accountEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    getToken();
                }
            } else if (requestCode == REQUEST_CODE_SIGN_IN_REQUIRED) {
                // User has signed in. Request token again.
                getToken();
            }
        }
    }

    private void getToken() {
        // Request token and then request the translation
        new GetGoogleUsernameTask(this, accountEmail).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void handleException(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e instanceof GooglePlayServicesAvailabilityException) {
                    Log.e("MainActivity", "Google Play Services APK is old or unavailable");
                } else if (e instanceof UserRecoverableAuthException) {
                    // Ask user to grant the app access to the account using Google Play services.
                    startActivityForResult(((UserRecoverableAuthException)e).getIntent(), REQUEST_CODE_SIGN_IN_REQUIRED);
                } else if (e instanceof GoogleAuthException) {
                    Log.e("MainActivity", "Caught GoogleAuthException");
                } else if (e instanceof IOException) {
                    Log.e("MainActivity", "Caught IOException, is network connected?");
                }
            }
        });
    }

    public void translate() {
        // Send the request to the Google Translate API
        GoogleTranslateRequest request = new GoogleTranslateRequest(token);
        Volley.newRequestQueue(this).add(request);
    }

}
