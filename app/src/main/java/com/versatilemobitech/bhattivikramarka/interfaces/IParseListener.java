
package com.versatilemobitech.bhattivikramarka.interfaces;

import com.android.volley.VolleyError;

public interface IParseListener {

    void ErrorResponse(VolleyError error, int requestCode);

    void SuccessResponse(String response, int requestCode);
}
