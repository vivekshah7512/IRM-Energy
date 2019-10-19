package com.vbs.irmenergy.utilities.volley;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vbs.irmenergy.utilities.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class VolleyCacheRequestClass {

    private static VolleyCacheRequestClass volleyCacheRequestClass;
    private VolleyResponseInterface volleyResponseInterface;

    private VolleyCacheRequestClass() {

    }

    public static VolleyCacheRequestClass getInstance() {
        if (volleyCacheRequestClass == null) {
            volleyCacheRequestClass = new VolleyCacheRequestClass();
        }
        return volleyCacheRequestClass;
    }

    public void volleyJsonAPI(final Context mContext, final int req, String mUrl, Map<String, String> params) {
        try {
            volleyResponseInterface = (VolleyResponseInterface) mContext;
            Log.e(Constant.TAG, "URL: " + mUrl + "\nRequest: " + params.toString());
            JsonObjectRequest cacheRequest = new JsonObjectRequest(mUrl, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e(Constant.TAG, "Response: " + response.toString());
                                volleyResponseInterface.vResponse(req, response.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(Constant.TAG, "Error: " + error.getMessage());
                    volleyResponseInterface.vErrorMsg(req, error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            cacheRequest.setRetryPolicy(new DefaultRetryPolicy(Constant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue queue = Volley.newRequestQueue(mContext);
            queue.add(cacheRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void volleyJsonAPI1(final Context mContext, final int req, String mUrl, Map<String, Object> params) {
        try {
            volleyResponseInterface = (VolleyResponseInterface) mContext;
            Log.d(Constant.TAG, "Request: " + params.toString());
            JsonObjectRequest cacheRequest = new JsonObjectRequest(mUrl, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d(Constant.TAG, "Response: " + response.toString());
                                volleyResponseInterface.vResponse(req, response.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(Constant.TAG, "Error: " + error.getMessage());
                    volleyResponseInterface.vErrorMsg(req, error.getMessage());

                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            JSONObject obj = new JSONObject(res);
                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            cacheRequest.setRetryPolicy(new DefaultRetryPolicy(Constant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue queue = Volley.newRequestQueue(mContext);
            cacheRequest.setShouldCache(false);
            queue.add(cacheRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
