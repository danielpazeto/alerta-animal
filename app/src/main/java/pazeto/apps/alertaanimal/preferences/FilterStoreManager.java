package pazeto.apps.alertaanimal.preferences;

import android.content.Context;

import pazeto.alertaanimal.model.FilterObject;

/**
 * Created by pazeto on 5/17/17.
 */

public class FilterStoreManager extends SharedPreferencesManager {

    private final String KEY = "SHARED_PREFS_FILTER_KEY";

    private static FilterStoreManager instance;

    private FilterStoreManager(Context ctx) {
        super(ctx);
    }

    public FilterObject getFilter() {
        FilterObject filter = (FilterObject) get(KEY, FilterObject.class);
        if(filter == null){
            filter = new FilterObject();
        }
        return filter;
    }

    public void setFilter(FilterObject filter) {
        put(KEY, filter);
    }

    public void clearFilter(){
        put(KEY, null);
    }


    public static FilterStoreManager getInstance(Context ctx) {
        if(instance==null){
            instance = new FilterStoreManager(ctx);
        }
        return instance;
    }
}
