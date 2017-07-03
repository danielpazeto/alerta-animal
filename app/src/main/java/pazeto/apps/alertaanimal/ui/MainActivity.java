package pazeto.apps.alertaanimal.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

import java.util.List;

import pazeto.alertaanimal.DTO.Animal;
import pazeto.alertaanimal.DTO.User;
import pazeto.apps.alertaanimal.R;
import pazeto.apps.alertaanimal.connection.AnimalServerRequest;
import pazeto.apps.alertaanimal.preferences.UserAppSettings;
import pazeto.apps.alertaanimal.util.DownloadImage;
import pazeto.apps.alertaanimal.util.Utils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SYNC_ANIMALS_ACTION = "pazeto.apps.alertaanimal.SYNC_ANIMALS_ACTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User loggedUser = new UserAppSettings(this).getUser();

        IntentFilter filter = new IntentFilter();
        filter.addAction(SYNC_ANIMALS_ACTION);
        registerReceiver(mSyncAnimalsReceiver, filter);

        if(loggedUser == null){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
            return;
        }
        syncAnimals(MainActivity.this);

        FacebookSdk.sdkInitialize(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);

        TextView tvProfileName = (TextView) hView.findViewById(R.id.tv_profile_name);
        TextView tvProfileEmail = (TextView) hView.findViewById(R.id.tv_profile_email);
        ImageView profilePicture = (ImageView)hView.findViewById(R.id.picture_profile);

        Profile profile = Profile.getCurrentProfile();
        if(profile !=null) {
            String imageUrl = profile.getProfilePictureUri(200, 200).toString();
            //Download and set the image picture
            new DownloadImage(profilePicture).execute(imageUrl);
        }


        tvProfileEmail.setText(loggedUser.getEmail());
        tvProfileName.setText(loggedUser.getName());

        Utils.openFragment(new MapsFragment(), this);
    }

    private static void syncAnimals(Context ctx) {
        Intent i = new Intent(SYNC_ANIMALS_ACTION);
        ctx.sendBroadcast(i);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        Class fragmentClass;

        switch(menuItem.getItemId()) {
            case R.id.nav_maps:
                fragmentClass = MapsFragment.class;
                break;
            case R.id.nav_log_out:
                LoginManager.getInstance().logOut();
                new UserAppSettings(MainActivity.this).resetAppSettings();
                finish();
                return true;
            default:
                fragmentClass = MapsFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        Utils.openFragment(fragment, this);

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Response.Listener<List<Animal>> listAnimalCallback = new Response.Listener<List<Animal>>(){

        @Override
        public void onResponse(List<Animal> animalList) {
            new UserAppSettings(MainActivity.this).setAvailableAnimals(animalList);
        }
    };

    private BroadcastReceiver mSyncAnimalsReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //call animal list api
            AnimalServerRequest.getInstance(context).listAnimals(listAnimalCallback);
        }
    };

    @Override
    protected void onDestroy() {
        if (mSyncAnimalsReceiver != null) {
            unregisterReceiver(mSyncAnimalsReceiver);
            mSyncAnimalsReceiver = null;
        }
        super.onDestroy();
    }
}
