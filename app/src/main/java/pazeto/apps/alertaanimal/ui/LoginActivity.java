package pazeto.apps.alertaanimal.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import pazeto.alertaanimal.DTO.User;
import pazeto.apps.alertaanimal.R;
import pazeto.apps.alertaanimal.connection.UserServerRequest;
import pazeto.apps.alertaanimal.preferences.UserAppSettings;


/**
 * A login screen that offers login via email/password.facebook
 */
public class LoginActivity extends FragmentActivity implements FacebookCallback<LoginResult> {

    private static final long SPLASH_TIME_OUT = 1;
//    private UserLoginTask mAuthTask = null;

    // UI references.
/*    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;*/
    private LoginButton loginButton;
    CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private AccessTokenTracker accessTokenTracker;
    private UserAppSettings userAppSettings;
    private String TAG = LoginActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        userAppSettings = new UserAppSettings(this);

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
//                updateWithToken(newAccessToken);
            }
        };
        accessTokenTracker.startTracking();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                if(currentProfile != null) {
//                    userAppSettings.setUser(currentProfile.getName());
                }else{
                    userAppSettings.resetAppSettings();
                }
            }
        };
        profileTracker.startTracking();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        loginButton.registerCallback(callbackManager, this);
        loginButton.setOnClickListener(facebookLoginClickListner);

//        final ProgressDialog progressDialog = new ProgressDialog(this,
//                R.style.Theme_AppCompat_Light_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Creating Account...");
//        progressDialog.show();
    }

    View.OnClickListener facebookLoginClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "asdasdasdas");
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        if(userAppSettings.getUser() != null && AccessToken.getCurrentAccessToken() != null){
            Log.i(TAG, "ON RESUME USER: "+ userAppSettings.getUser());
            updateWithToken(AccessToken.getCurrentAccessToken());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void updateWithToken(AccessToken currentAccessToken) {

        if(currentAccessToken != null){
            GraphRequest request = GraphRequest.newMeRequest(
                    currentAccessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v(TAG, response.toString());

                            try {
                                Log.d(TAG, "object : " + object);

                                User user = new User();
                                user.setFacebookId(object.getLong("id"));
                                user.setEmail(object.getString("email"));
                                user.setName(object.getString("first_name"));
                                user.setLastName(object.getString("middle_name") + " "+object.getString("last_name"));

                                serverLogin(user);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,first_name,middle_name,last_name,email,gender,birthday,picture");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }

    @Override
    public void onSuccess(LoginResult o) {
        Log.i(TAG, "OnSUCESS");
        updateWithToken(o.getAccessToken());
    }

    @Override
    public void onCancel() {
        Log.e(TAG, "On cancel facebook login");
    }

    @Override
    public void onError(FacebookException error) {
        Log.e(TAG, "Error on facebok login: " + error.toString());
    }



    private void serverLogin(User user) throws JSONException {
        Log.i(TAG, "User to save/login: " + user.getId()+ " - "+user.getFacebookId() + " - "+user.getEmail());
        UserServerRequest.getInstance(this).syncUserData(syncUserCallback, syncUserErrorCallback, user);
    }

    private Response.Listener syncUserCallback = new Response.Listener<User>(){

        @Override
        public void onResponse(User user) {
            Log.i(TAG, "Logged succesfully!");
            if(user != null) {
                userAppSettings.setUser(user);
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }else{
                userAppSettings.resetAppSettings();
                finish();
                Log.d(TAG, " ERRRADDOOOO");
            }
        }
    };

    protected Response.ErrorListener syncUserErrorCallback = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "Error: ", error.fillInStackTrace());
            Toast.makeText(LoginActivity.this, "Erro no login!", Toast.LENGTH_LONG).show();
            userAppSettings.resetAppSettings();
            finish();
        }
    };

}

