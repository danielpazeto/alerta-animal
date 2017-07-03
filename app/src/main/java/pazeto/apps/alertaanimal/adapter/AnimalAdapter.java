package pazeto.apps.alertaanimal.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pazeto.alertaanimal.DTO.Animal;

public class AnimalAdapter extends ArrayAdapter<Animal> {

    private final Context context;
    private final int textViewResourceId;
    List<Animal> animalList = new ArrayList<>();

    public AnimalAdapter(Context context, int textViewResourceId, List<Animal> objects) {
        super(context, textViewResourceId, objects);
        this.textViewResourceId = textViewResourceId;
        this.context = context;
        this.animalList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        AnimalHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(textViewResourceId, parent, false);

            holder = new AnimalHolder();
            holder.nameTv = (TextView)row.findViewById(android.R.id.text1);

            row.setTag(holder);
        }
        else
        {
            holder = (AnimalHolder)row.getTag();
        }

        Animal animal = animalList.get(position);
        holder.nameTv.setText(animal.getName());
        holder.id = animal.getId();

        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {   // This view starts when we click the spinner.
        return getView(position, convertView, parent);
    }

    public static class AnimalHolder
    {
        Long id;
        TextView nameTv;

        public Long getId() {
            return id;
        }

        public String getName() {
            if(nameTv !=null){
                return nameTv.getText().toString();
            }
            return "";
        }
    }

}
