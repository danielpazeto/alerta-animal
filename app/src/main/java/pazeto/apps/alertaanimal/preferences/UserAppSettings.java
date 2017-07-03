package pazeto.apps.alertaanimal.preferences;

import android.content.Context;

import com.google.gson.Gson;

import java.util.List;

import pazeto.alertaanimal.DTO.Animal;
import pazeto.alertaanimal.DTO.User;
import pazeto.apps.alertaanimal.R;

/**
 * Created by pazeto on 5/22/17.
 */

public class UserAppSettings extends SharedPreferencesManager{


    public UserAppSettings(Context ctx) {
        super(ctx);
    }


    public void setUser(User user) {
        super.put(ctx.getString(R.string.shared_pref_user), user);
    }

    public User getUser() {
        return (User) super.get(ctx.getString(R.string.shared_pref_user), User.class);
    }


    public void resetAppSettings() {
        super.remove(ctx.getString(R.string.shared_pref_user));
    }

    public List<Animal> getAvailableAnimals(){
        return (List<Animal>) getList(ctx.getString(R.string.shared_pref_animal_list), Animal.class);
    }

    public void setAvailableAnimals(List<Animal> animals){
        putList(ctx.getString(R.string.shared_pref_animal_list), animals);
    }
}
