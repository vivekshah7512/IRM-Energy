package com.vbs.irmenergy.utilities.volley;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vbs.irmenergy.utilities.Constant;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nehal on 22/11/18.
 */
public class VolleyAPIClass {
    private VolleyResponseInterface volleyResponseInterface;

    public void volleyAPICall(Context mContext, Fragment fragment, final int req,
                              String mUrl, Map<String, Object> params) {
        try {
            if (fragment == null) {
                volleyResponseInterface = (VolleyResponseInterface) mContext;
            } else {
                volleyResponseInterface = (VolleyResponseInterface) fragment;
            }

            JSONObject jsonObject;
            if (params != null) {
                jsonObject = new JSONObject(params);
                Log.e("URL : ", mUrl);
                Log.e("Request : ", jsonObject.toString());
            } else {
                jsonObject = null;
            }

            JsonObjectRequest cacheRequest = new JsonObjectRequest(mUrl, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e("Response : ", response.toString());
                                volleyResponseInterface.vResponse(req, response.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(Constant.TAG, "Error: " + error.getMessage());
                    volleyResponseInterface.vErrorMsg(req, error.toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Accept", "application/json");
                    return headers;
                }
            };


            cacheRequest.setRetryPolicy(new DefaultRetryPolicy(
                    Constant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue queue = Volley.newRequestQueue(mContext);
            queue.add(cacheRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void volleyGetJsonAPI(final Context mContext, Fragment fragment, final int req,
                                 String mUrl) {
        try {
            if (fragment == null) {
                volleyResponseInterface = (VolleyResponseInterface) mContext;
            } else {
                volleyResponseInterface = (VolleyResponseInterface) fragment;
            }
            Log.d(Constant.TAG, "Request URL: " + mUrl);
            JsonObjectRequest cacheRequest = new JsonObjectRequest(mUrl, null,
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
                    VolleyLog.d(Constant.TAG, "Error: " + error.getMessage());
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
}
