package pazeto.apps.alertaanimal.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import pazeto.apps.alertaanimal.R;

/**
 * Created by pazeto on 6/24/17.
 */

public class Utils {

    public static void openFragment(Fragment fragment, FragmentActivity ctx){
        FragmentManager fragmentManager = ctx.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    public static boolean isConnected(Context mCtx) {
        ConnectivityManager cm = (ConnectivityManager) mCtx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
