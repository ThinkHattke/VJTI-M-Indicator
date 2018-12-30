package thinkhattke.gaurav.vjti;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thinkhattke.gaurav.vjti.API.Connection.APIClient;
import thinkhattke.gaurav.vjti.API.Connection.ApiInterface;
import thinkhattke.gaurav.vjti.API.Reponse.NewReportRes;
import thinkhattke.gaurav.vjti.API.Request.AddReportReq;
import thinkhattke.gaurav.vjti.Util.GPSTracker;
import thinkhattke.gaurav.vjti.Util.TinyDB;

public class Report extends AppCompatActivity {


    //UI Components
    private TextView how;
    private EditText name, number;
    private Spinner problem;
    private AutoCompleteTextView location;
    private ImageView image, back;
    private CircularProgressButton submit;


    //Local Data
    String Name, Number, Problem, Location, ImageURL, Lat, Lon;
    String problems[] = {"[Select Problem]", "Door Blocking", "Bullying", "Theft", "Voilence", "Others"};
    String places[] = {"Nallasopara", "Virar", "Vasai", "Bhayandar", "Dahisar", "Borivali", "Kandivali", "Dadar", "Bandra"};
    private static final int SELECT_PHOTO = 1;


    //Global data
    TinyDB db;
    private GPSTracker gps;
    private ApiInterface api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        //Setting Views
        how = findViewById(R.id.how);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        problem = findViewById(R.id.problem);
        location = findViewById(R.id.location);
        image = findViewById(R.id.image);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.back);


        //OnClicks
        OnClicks();


        //Setting Spinners
        ArrayAdapter<String> minorAdapter = new ArrayAdapter<String>(Report.this, android.R.layout.simple_spinner_item, problems);
        minorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        problem.setAdapter(minorAdapter);
        problem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Problem = problems[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Setting AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, places);
        location.setAdapter(adapter);


        //Extracting GPS data
        gps = new GPSTracker(Report.this);
        getCoordinates();


        //Setting up API
        api = APIClient.getClient().create(ApiInterface.class);


        //Setting up TinyDB
        db = new TinyDB(Report.this);


        //Updating the FireBase Device Token
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {

                String newToken = instanceIdResult.getToken();
                db = new TinyDB(Report.this);
                db.putString("token",newToken);


            }
        });

    }


    //Function to handle on clicks
    private void OnClicks() {


        how.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog;
                dialog = new Dialog(Report.this);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_report_working);

                ImageView dismiss = dialog.findViewById(R.id.dismiss);

                dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();


            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (checkFields()) {

                    submit.startAnimation();

                    if (image.getTag().equals("no")) {

                        AddReportReq addReportReq = new AddReportReq(Name, Number, Problem,"", Location, Lat, Lon, db.getString("token") );
                        submit(addReportReq);

                    } else {

                        FirebaseStorage storage =  FirebaseStorage.getInstance();
                        StorageReference storageReference = storage.getReferenceFromUrl("\n" +
                                "gs://test-indicator.appspot.com").child("ic_launcher.png");
                        image.setDrawingCacheEnabled(true);
                        image.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                        image.layout(0, 0, image.getMeasuredWidth(), image.getMeasuredHeight());
                        image.buildDrawingCache();
                        Bitmap bitmap = Bitmap.createBitmap(image.getDrawingCache());

                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        byte[] data = outputStream.toByteArray();

                        UploadTask uploadTask = storageReference.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                ImageURL = taskSnapshot.getStorage().getDownloadUrl().toString();

                                AddReportReq addReportReq = new AddReportReq(Name, Number, Problem,"", Location, Lat, Lon, db.getString("token"));

                                submit(addReportReq);

                            }
                        });


                    }


                }


            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);


            }
        });


    }


    //Function to check if all the fields are valid
    private Boolean checkFields() {


        Name = name.getText().toString().trim();
        Number = number.getText().toString().trim();
        Location = location.getText().toString().trim();

        if (Name.isEmpty()) {

            print("Enter your name to continue");
            return  false;

        } else if (Number.isEmpty()) {

            print("Enter your number to continue");
            return false;

        } else if (Problem.equals("[Select Problem]")) {

            print("Select a Problem / Crime to continue");
            return false;

        } else if (Location.isEmpty()) {

            print("Enter the location to continue");
            return false;

        } else {

            return true;

        }


    }


    //Print function which prints a toast for Debugging purpose
    public void print(String s) {
        Toast toast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);
        toast.show();
    }


    //Function to submit the report
    private void submit(AddReportReq addReportReq) {

        api.attemptReport(addReportReq).enqueue(new Callback<NewReportRes>() {
            @Override
            public void onResponse(Call<NewReportRes> call, Response<NewReportRes> response) {


                if (response.body().getStatus().equals("done")) {


                    print("Successfully done");

                }

                submit.revertAnimation();

                finish();

            }

            @Override
            public void onFailure(Call<NewReportRes> call, Throwable t) {

                submit.revertAnimation();

                print("something went wrong");

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String imagePath = "";
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = cursor.getString(columnIndex);
                cursor.close();
                image.setImageURI(selectedImage);
                image.setTag("yes");
            }
        }
    }


    //Function to fetch GPS location
    private void getCoordinates() {

        if (gps.canGetLocation()) {

            Double dlatitude = gps.getLatitude();
            Double dlongitude = gps.getLongitude();
            Lat = String.valueOf(dlatitude);
            Lon = String.valueOf(dlongitude);

        }

    }


}
