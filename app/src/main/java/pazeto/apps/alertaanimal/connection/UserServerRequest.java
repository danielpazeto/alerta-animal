package pazeto.apps.alertaanimal.connection;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pazeto.alertaanimal.DTO.Alert;
import pazeto.alertaanimal.DTO.User;
import pazeto.alertaanimal.model.FilterObject;

/**
 * Created by pazeto on 6/10/17.
 */

public class UserServerRequest extends ServerRequests {
    private static UserServerRequest mInstance;
    private final String TAG = UserServerRequest.class.getName();

    public UserServerRequest(Context context) {
        super(context);
    }

    public static synchronized UserServerRequest getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UserServerRequest(context);
        }
        return mInstance;
    }

    public void syncUserData(final Response.Listener<User> syncUserCallback,
                             Response.ErrorListener onRequestErrorCallback, User user)
            throws JSONException {

        String userJson= new Gson().toJson(user);

        final JSONObject jsonObject = new JSONObject(userJson);

        String userUrl = "/user/login";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HOST + userUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "User: " + response);
                        User user = new Gson().fromJson(response, User.class);
                        syncUserCallback.onResponse(user);
                    }
                }, onRequestErrorCallback){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user", jsonObject.toString());
                return params;
            }
        };
        addToRequestQueue(stringRequest);
    }




}
