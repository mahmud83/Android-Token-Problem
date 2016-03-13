package com.example.android_token_problem;

import android.net.Uri;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class GoogleTranslateRequest extends Request {

    private static final String GOOGLE_TRANSLATE_API_KEY = "PASTE_API_KEY_HERE";
    private static String token;

    public GoogleTranslateRequest(String token) {
        super(Method.GET, "https://www.googleapis.com/language/translate/v2?&source=en&target=es&q=TEST&key="
                + Uri.encode(GOOGLE_TRANSLATE_API_KEY), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response", error.toString());
            }
        });
        this.token = token;
    }

    @Override
    public Map<String, String> getHeaders() {
        // Send the Google OAuth access token in the Authorization header
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Authorization", "Bearer " + token);
        return params;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(Object response) {
        Log.d("Response", response.toString());
    }
}
