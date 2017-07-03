package pazeto.apps.alertaanimal.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import pazeto.alertaanimal.DTO.Animal;
import pazeto.apps.alertaanimal.adapter.AnimalAdapter;
import pazeto.apps.alertaanimal.preferences.FilterStoreManager;
import pazeto.apps.alertaanimal.R;
import pazeto.apps.alertaanimal.preferences.UserAppSettings;

public class FilterDialogActivity extends Activity {

    public static final int NEW_FILTER_REQUEST_CODE = 1992;

    SeekBar rangeBar;
    private Button applyFilterBtn, clearFilterBtn;
    private Spinner animalSpinner;

    pazeto.alertaanimal.model.FilterObject currentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        currentFilter = FilterStoreManager.getInstance(FilterDialogActivity.this).getFilter();

        applyFilterBtn = (Button) findViewById(R.id.btn_apply_filter);
        applyFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterStoreManager filterManager = FilterStoreManager.getInstance(FilterDialogActivity.this);
                filterManager.setFilter(currentFilter);
                setResult(RESULT_OK);
                finish();
            }
        });

        clearFilterBtn = (Button) findViewById(R.id.btn_clear_filter);
        clearFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterStoreManager.getInstance(FilterDialogActivity.this).clearFilter();
            }
        });
//        rangeBar = (SeekBar) findViewById(R.id.range_bar);
//        rangeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                currentFilter.setRange(seekBar.getProgress());
//            }
//        });

        animalSpinner = (Spinner) findViewById(R.id.animal_chooser_dropdown);

        List<Animal> animals = new UserAppSettings(this).getAvailableAnimals();
        AnimalAdapter adapter = new AnimalAdapter(FilterDialogActivity.this,
                android.R.layout.simple_dropdown_item_1line, animals);
        animalSpinner.setAdapter(adapter);

        animalSpinner.setOnItemSelectedListener(onAnimalItemClickListener);


    }

    AdapterView.OnItemSelectedListener onAnimalItemClickListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            AnimalAdapter.AnimalHolder selected = (AnimalAdapter.AnimalHolder) view.getTag();

            Animal animal = new Animal();
            animal.setId(selected.getId());
            animal.setName(selected.getName());

            currentFilter.setAnimal(animal);
            Toast.makeText(FilterDialogActivity.this,
                    "Clicked " + position + " name: " + animal.getName(),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}
