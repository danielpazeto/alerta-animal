package pazeto.apps.alertaanimal.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import pazeto.alertaanimal.DTO.Alert;
import pazeto.alertaanimal.DTO.Animal;
import pazeto.alertaanimal.DTO.User;
import pazeto.apps.alertaanimal.R;
import pazeto.apps.alertaanimal.adapter.AnimalAdapter;
import pazeto.apps.alertaanimal.connection.AlertServerRequest;
import pazeto.apps.alertaanimal.connection.AnimalServerRequest;
import pazeto.apps.alertaanimal.preferences.UserAppSettings;

public class AlertActivity extends AppCompatActivity {


    public static final int NEW_ALERT_REQUEST_CODE = 852156;
    ImageView mapPreview;
    private Spinner animalSpinner;

    private Button sendAlertBtn;
    private TextView tvComment;
    private LatLng position;
    private CheckBox checkboxAnonymous;

    private Animal selectedAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mapPreview = (ImageView) findViewById(R.id.mapPreview);

        if(getIntent().hasExtra("byteArray")) {
            ImageView previewThumbnail = new ImageView(this);
            Bitmap b = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"),0,getIntent()
                            .getByteArrayExtra("byteArray").length);
            previewThumbnail.setImageBitmap(b);
            mapPreview.setImageBitmap(b);
        }
        if(getIntent().hasExtra("lat") && getIntent().hasExtra("lng")){
            position = new LatLng(getIntent().getDoubleExtra("lat", 0),
                    getIntent().getDoubleExtra("lng", 0));
        }

        animalSpinner = (Spinner) findViewById(R.id.animal_chooser_dropdown);
        sendAlertBtn = (Button) findViewById(R.id.add_filter_btn);
        tvComment = (TextView) findViewById(R.id.alert_comment);
        checkboxAnonymous = (CheckBox) findViewById(R.id.checkbox_alert_anonymous);

        sendAlertBtn.setOnClickListener(sendAlertClickListener);

        AnimalAdapter adapter = new AnimalAdapter(AlertActivity.this,
                android.R.layout.simple_dropdown_item_1line, new UserAppSettings(this).getAvailableAnimals());
        animalSpinner.setAdapter(adapter);
        animalSpinner.setOnItemSelectedListener(onAnimalItemClickListener);
    }





    private View.OnClickListener sendAlertClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            User user = new UserAppSettings(AlertActivity.this).getUser();
            Alert alert = new Alert(selectedAnimal, user, position.latitude,
                    position.longitude, tvComment.getText().toString(), checkboxAnonymous.isChecked());

            AlertServerRequest.getInstance(AlertActivity.this).saveAlert(alert, saveAlertCallback);

        }
    };

    private Response.Listener<Alert> saveAlertCallback = new Response.Listener<Alert>(){

        @Override
        public void onResponse(Alert alert) {
            Toast.makeText(AlertActivity.this, "Alerta enviado", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }
    };

    private AdapterView.OnItemSelectedListener onAnimalItemClickListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            AnimalAdapter.AnimalHolder selected = (AnimalAdapter.AnimalHolder) view.getTag();

            Animal animal = new Animal();
            animal.setId(selected.getId());
            animal.setName(selected.getName());
            selectedAnimal = animal;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

}
