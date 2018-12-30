package thinkhattke.gaurav.vjti;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;
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
    private TextView title;


    //Local Data
    String Name, Number, Problem, Location, ImageURL, Lat, Lon;
    String problems[] = {"[Select Problem]", "Door Blocking", "Bullying", "Theft", "Voilence", "Others"};
    String places[] = {"Nallasopara", "Virar", "Vasai", "Bhayandar", "Dahisar", "Borivali", "Kandivali", "Dadar", "Bandra"};
    private static final int SELECT_PHOTO = 1;
    String ImageIconURL = "https://static.toiimg.com/thumb/imgsize-100561,msid-63153327,width-400,resizemode-4/63153327.jpg";
    Bitmap meraBitmap;


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
        title = findViewById(R.id.title);


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


        //Downloading Icon
        Picasso.get().load(ImageIconURL).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                meraBitmap = bitmap;
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

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


        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
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

                    sendNotification();

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


    //Function to send custom Notification
    public void sendNotification() {

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.drawable.icon_local_train) // notification icon
                .setContentTitle("There has been a Door blockage nearby (Virar)") // title for notification
                .setContentText("Is this information true?")// message for notification
                .setSound(defaultSoundUri)// set alarm sound for notification
                .addAction(R.drawable.icon_correct_check,"Yes",pi)
                .addAction(R.drawable.email_icon,"No", pi)
                .setLargeIcon(meraBitmap)
                .addAction(R.drawable.icon_stop,"Not sure", pi)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setAutoCancel(true); // clear notification after click
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());

    }

}
