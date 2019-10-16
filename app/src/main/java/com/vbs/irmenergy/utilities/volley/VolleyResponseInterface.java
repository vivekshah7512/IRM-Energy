package com.vbs.irmenergy.utilities.volley;

public interface VolleyResponseInterface {
    void vResponse(int reqCode, String result);

    void vErrorMsg(int reqCode, String error);
}
