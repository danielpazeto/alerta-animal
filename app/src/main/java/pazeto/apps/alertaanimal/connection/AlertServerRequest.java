package pazeto.apps.alertaanimal.connection;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pazeto.alertaanimal.DTO.Alert;
import pazeto.alertaanimal.DTO.Animal;
import pazeto.alertaanimal.model.FilterObject;

import static com.android.volley.Request.*;

/**
 * Created by pazeto on 6/10/17.
 */

public class AlertServerRequest extends ServerRequests {
    private static AlertServerRequest mInstance;
    private final String TAG = AlertServerRequest.class.getName();

    public AlertServerRequest(Context context) {
        super(context);
    }

    public static synchronized AlertServerRequest getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AlertServerRequest(context);
        }
        return mInstance;
    }

    public void listAlertByFilter(final Response.Listener<List<Alert>> filterAlertCallback, FilterObject filter){

        String jsonFilter= new Gson().toJson(filter);

        String animalListUrl = "/alert/list?filter="+ jsonFilter;

        JsonArrayRequest stringRequest = new JsonArrayRequest(Method.GET,
                HOST + animalListUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Alert> alertList = new ArrayList<>();
                        try {
                            Log.d("JsonArray",response.toString());
                            for(int i=0; i<response.length(); i++){
                                JSONObject jresponse = response.getJSONObject(i);
                                Alert alert = new Gson().fromJson(jresponse.toString(), Alert.class);
                                alertList.add(alert);
                            }
                            filterAlertCallback.onResponse(alertList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, getDefaultErrorHandler()
        );
        addToRequestQueue(stringRequest);
    }


    public void saveAlert(final Alert alert, final Response.Listener<Alert> saveAlertCallback) {

        String animalListUrl = "/alert/save";

        final StringRequest stringRequest = new StringRequest(Method.PUT,
                HOST + animalListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Alert alert = new Gson().fromJson(response, Alert.class);
                        saveAlertCallback.onResponse(alert);
                    }
                }, getDefaultErrorHandler()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String alertJson = new Gson().toJson(alert);

                Map<String, String> params = new HashMap<>();
                params.put("alert", alertJson);
                return params;
            }
        };
        addToRequestQueue(stringRequest);
    }
}
