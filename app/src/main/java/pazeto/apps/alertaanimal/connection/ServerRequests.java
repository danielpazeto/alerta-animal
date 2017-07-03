package pazeto.apps.alertaanimal.connection;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pazeto.alertaanimal.DTO.Animal;
import pazeto.alertaanimal.model.FilterObject;
import pazeto.apps.alertaanimal.BuildConfig;
import pazeto.apps.alertaanimal.util.Utils;

/**
 * Created by pazeto on 6/6/17.
 */

public class ServerRequests {

    final static String HOST = BuildConfig.HOST;
    private static final String TAG = ServerRequests.class.getName();

    private RequestQueue mRequestQueue;
    private static Context mCtx;

    public ServerRequests(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        addToRequestQueue(req, 0);
    }


    public <T> void addToRequestQueue(Request<T> req, int retriesCaseFailed) {

        if(Utils.isConnected(mCtx)) {
            req.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    retriesCaseFailed,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            getRequestQueue().add(req);
        }else{
//            Snackbar snackbar = Snackbar
//                    .make(coordinatorLayout, "Had a snack at Snackbar", Snackbar.LENGTH_LONG)
//                    .setAction("Undo", mOnClickListener);
//            snackbar.setActionTextColor(Color.RED);
//            View snackbarView = snackbar.getView();
//            snackbarView.setBackgroundColor(Color.DKGRAY);
//            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
//            textView.setTextColor(Color.YELLOW);
//            snackbar.show();
        }
    }

    protected Response.ErrorListener getDefaultErrorHandler() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: ", error.fillInStackTrace());
                Toast.makeText(mCtx, "Server error.. ",Toast.LENGTH_LONG).show();
            }
        };
    }
}