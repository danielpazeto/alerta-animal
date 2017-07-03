package pazeto.apps.alertaanimal.connection;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pazeto.alertaanimal.DTO.Animal;

/**
 * Created by pazeto on 6/10/17.
 */

public class AnimalServerRequest extends ServerRequests {
    private static AnimalServerRequest mInstance;
    private final String TAG = AnimalServerRequest.class.getName();

    public AnimalServerRequest(Context context) {
        super(context);
    }

    public static synchronized AnimalServerRequest getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AnimalServerRequest(context);
        }
        return mInstance;
    }

    public void listAnimals(final Response.Listener<List<Animal>> listAnimalCallback){

        String animalListUrl = "/animal/list";

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET,
                HOST + animalListUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Animal> animalList = new ArrayList<>();
                        try {
                            Log.d("JsonArray",response.toString());
                            for(int i=0;i<response.length();i++){
                                JSONObject jresponse = response.getJSONObject(i);
                                Animal animal = new Gson().fromJson(jresponse.toString(), Animal.class);
                                animalList.add(animal);
                            }
                            listAnimalCallback.onResponse(animalList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error:", error);
            }
        });
        addToRequestQueue(stringRequest);
    }

}
