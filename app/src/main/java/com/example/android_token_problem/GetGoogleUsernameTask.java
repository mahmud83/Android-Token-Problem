package com.example.android_token_problem;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthUtil;

/**
 * Retrieves a token for the Google Translate API and calls back to the main
 * activity to request a translation.
 */
public class GetGoogleUsernameTask extends AsyncTask<Void, Void, String> {

    private MainActivity activity;
    private String email;

    public GetGoogleUsernameTask(MainActivity activity, String name) {
        this.activity = activity;
        this.email = name;
    }

    @Override
    protected String doInBackground(Void... params) {
        return fetchToken();
    }

    /**
     * Gets an authentication token from Google and handles any
     * GoogleAuthException that may occur.
     */
    private String fetchToken() {
        try {
            return GoogleAuthUtil.getToken(activity, email, "oauth2:https://www.googleapis.com/auth/translate");
        } catch (Exception e) {
            activity.handleException(e);
        }
        return null;
    }

    protected void onPostExecute(String result) {
        if (result != null) {
            // Save the token in the main activity.
            activity.setToken(result);

            // Run the Google Translate API translation.
            activity.translate();
        }
    }
}
