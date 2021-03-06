package thinkhattke.gaurav.vjti;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thinkhattke.gaurav.vjti.API.Connection.APIClient;
import thinkhattke.gaurav.vjti.API.Connection.ApiInterface;
import thinkhattke.gaurav.vjti.API.Reponse.NewPassRes;
import thinkhattke.gaurav.vjti.API.Request.AddPassReq;
import thinkhattke.gaurav.vjti.Util.TinyDB;

public class Pass extends AppCompatActivity {


    //UI Components
    private TextView startDate, endDate, startTrain, endTrain, edit;
    private Spinner start, end;
    private ImageView image, back, done;
    private CircularProgressButton submit;


    //Global Data
    TinyDB db;
    private ApiInterface api;


    //Local Data
    String places[] = {"[Select Station]","Nallasopara", "Virar", "Vasai", "Bhayandar", "Dahisar", "Borivali", "Kandivali", "Dadar", "Bandra"};
    String Start, End, StartTrain="", EndTrain="", StartDate, EndDate, ImagePath;
    private static final int SELECT_PHOTO = 1;
    boolean imaged = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remainder);


        //Setting Views
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        image = findViewById(R.id.image);
        startTrain = findViewById(R.id.startTrain);
        endTrain = findViewById(R.id.endTrain);
        back = findViewById(R.id.back);
        done = findViewById(R.id.done);
        submit = findViewById(R.id.submit);
        edit = findViewById(R.id.edit);


        //Initialising TinyDB
        db = new TinyDB(Pass.this);


        //Setting up API
        api = APIClient.getClient().create(ApiInterface.class);


        //checking for previously saved data
        if (db.getString("pass").equals("yes")) {


            done.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
            startDate.setText(db.getString("passStartDate"));
            endDate.setText(db.getString("passEndDate"));
            startTrain.setText(db.getString("passStartTrain"));
            endTrain.setText(db.getString("passEndTrain"));

            setValue(db.getString("passStart"),start);
            setValue(db.getString("passEnd"),end);

            StartTrain = startTrain.getText().toString().trim();
            EndTrain = endTrain.getText().toString().trim();


            if (!db.getString("passImage").equals("no")) {

                image.setImageBitmap(db.getImage(db.getString("passImage")));

            }

            image.setEnabled(false);
            startDate.setEnabled(false);
            endDate.setEnabled(false);
            start.setEnabled(false);
            end.setEnabled(false);
            startTrain.setEnabled(false);
            endTrain.setEnabled(false);


        }


        //Setting Spinners
        ArrayAdapter<String> minorAdapter = new ArrayAdapter<String>(Pass.this, android.R.layout.simple_spinner_item, places);
        minorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        start.setAdapter(minorAdapter);
        start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Start = places[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        end.setAdapter(minorAdapter);
        end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                End = places[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //OnClicks
        OnClicks();


    }


    //Function to show T&C Dialog
    private void showDialog() {

        final Dialog dialog;
        dialog = new Dialog(Pass.this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_terms_and_condition);
        ImageView dismiss = dialog.findViewById(R.id.dismiss);
        final CheckBox agree = dialog.findViewById(R.id.agree);
        TextView go = dialog.findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (agree.isChecked()) {

                    db.putString("passStartDate", StartDate);
                    db.putString("passEndDate", EndDate);
                    db.putString("passStart", Start);
                    db.putString("passEnd", End);
                    db.putString("passStartTrain", StartTrain);
                    db.putString("passEndTrain", EndTrain);
                    db.putString("pass","yes");

                    if (imaged) {

                        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
                        db.putImageWithFullPath(ImagePath, bitmap);
                        db.putString("passImage",ImagePath);

                    } else {

                        db.putString("passImage","no");

                    }

                    image.setEnabled(false);
                    startDate.setEnabled(false);
                    endDate.setEnabled(false);
                    start.setEnabled(false);
                    end.setEnabled(false);
                    startTrain.setEnabled(false);
                    endTrain.setEnabled(false);

                    done.setVisibility(View.GONE);
                    edit.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.GONE);

                    postData();

                    dialog.dismiss();

                }

            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    //Function to handle OnClicks
    private void OnClicks() {

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                edit.setVisibility(View.GONE);
                done.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
                submit.setText("Edit");
                image.setEnabled(true);
                startDate.setEnabled(true);
                endDate.setEnabled(true);
                start.setEnabled(true);
                end.setEnabled(true);
                startTrain.setEnabled(true);
                endTrain.setEnabled(true);


            }
        });



        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(startDate);
            }
        });


        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(endDate);
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


        startTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog;
                dialog = new Dialog(Pass.this);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_trains);
                ImageView dismiss = dialog.findViewById(R.id.dismiss);
                final TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12;

                t1 = dialog.findViewById(R.id.t1);
                t2 = dialog.findViewById(R.id.t2);
                t3 = dialog.findViewById(R.id.t3);
                t4 = dialog.findViewById(R.id.t4);
                t5 = dialog.findViewById(R.id.t5);
                t6 = dialog.findViewById(R.id.t6);
                t7 = dialog.findViewById(R.id.t7);
                t8 = dialog.findViewById(R.id.t8);
                t9 = dialog.findViewById(R.id.t9);
                t10 = dialog.findViewById(R.id.t10);
                t11 = dialog.findViewById(R.id.t11);
                t12 = dialog.findViewById(R.id.t12);


                t1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setStartTrain(t1);
                        dialog.dismiss();

                    }
                });

                t2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setStartTrain(t2);
                        dialog.dismiss();

                    }
                });

                t3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setStartTrain(t3);
                        dialog.dismiss();

                    }
                });

                t4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setStartTrain(t4);
                        dialog.dismiss();

                    }
                });

                t5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setStartTrain(t5);
                        dialog.dismiss();

                    }
                });

                t6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setStartTrain(t6);
                        dialog.dismiss();

                    }
                });

                t7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setStartTrain(t7);
                        dialog.dismiss();

                    }
                });
                t8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setStartTrain(t8);
                        dialog.dismiss();

                    }
                });

                t9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setStartTrain(t9);
                        dialog.dismiss();

                    }
                });

                t10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setStartTrain(t10);
                        dialog.dismiss();

                    }
                });
                t11.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setStartTrain(t11);
                        dialog.dismiss();

                    }
                });

                t12.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setStartTrain(t12);
                        dialog.dismiss();

                    }
                });


                dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();


            }
        });


        endTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog;
                dialog = new Dialog(Pass.this);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_trains);
                ImageView dismiss = dialog.findViewById(R.id.dismiss);
                final TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12;

                t1 = dialog.findViewById(R.id.t1);
                t2 = dialog.findViewById(R.id.t2);
                t3 = dialog.findViewById(R.id.t3);
                t4 = dialog.findViewById(R.id.t4);
                t5 = dialog.findViewById(R.id.t5);
                t6 = dialog.findViewById(R.id.t6);
                t7 = dialog.findViewById(R.id.t7);
                t8 = dialog.findViewById(R.id.t8);
                t9 = dialog.findViewById(R.id.t9);
                t10 = dialog.findViewById(R.id.t10);
                t11 = dialog.findViewById(R.id.t11);
                t12 = dialog.findViewById(R.id.t12);


                t1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setEndTrain(t1);
                        dialog.dismiss();

                    }
                });

                t2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setEndTrain(t2);
                        dialog.dismiss();

                    }
                });

                t3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setEndTrain(t3);
                        dialog.dismiss();

                    }
                });

                t4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setEndTrain(t4);
                        dialog.dismiss();

                    }
                });

                t5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setEndTrain(t5);
                        dialog.dismiss();

                    }
                });

                t6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setEndTrain(t6);
                        dialog.dismiss();

                    }
                });

                t7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setEndTrain(t7);
                        dialog.dismiss();

                    }
                });
                t8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setEndTrain(t8);
                        dialog.dismiss();

                    }
                });

                t9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setEndTrain(t9);
                        dialog.dismiss();

                    }
                });

                t10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setEndTrain(t10);
                        dialog.dismiss();

                    }
                });
                t11.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setEndTrain(t11);
                        dialog.dismiss();

                    }
                });

                t12.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setEndTrain(t12);
                        dialog.dismiss();

                    }
                });


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


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.performClick();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (checkFields()) {

                    showDialog();

                }


            }
        });


    }


    //Setting start date using Custom DatePicker
    private void setDate(final TextView view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(Pass.this);
                final View pickDate = inflater.inflate(R.layout.layout_date_pick, null);
                new AlertDialog.Builder(Objects.requireNonNull(Pass.this))
                        .setView(pickDate)
                        .setIcon(android.R.drawable.ic_menu_agenda)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatePicker datePicker = pickDate.findViewById(R.id.pickdate);
                                String year = "" + datePicker.getYear();
                                String month = "" + (datePicker.getMonth() + 1);
                                String day = "" + datePicker.getDayOfMonth();
                                if (month.length() == 1)
                                    month = "0" + month;
                                if (day.length() == 1)
                                    day = "0" + day;
                                String date = day + "/" + month + "/" + year;
                                view.setText(date);
                                db.putString("DOB",date);
                            }
                        })
                        .create().show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImagePath = "";
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                ImagePath = cursor.getString(columnIndex);
                cursor.close();
                image.setImageURI(selectedImage);
                image.setTag("yes");
            }
        }
    }


    //Function to set Start Train
    private void setStartTrain(TextView tv) {


        StartTrain = tv.getText().toString().trim();
        String value[] = StartTrain.split("AM");
        startTrain.setText(value[0]+" AM \n"+value[1]);


    }


    //Function to set End Train
    private void setEndTrain(TextView tv) {

        EndTrain = tv.getText().toString().trim();
        endTrain.setText(EndTrain);

    }


    //Function to check Valid fields
    private boolean checkFields() {


        StartDate = startDate.getText().toString();
        EndDate = endDate.getText().toString();


        if (StartDate.equals("Click Here")) {

            print("Provide Start date to contiue");
            return false;

        } else if (EndDate.equals("Click Here")) {

            print("Provide End date to contiue");
            return false;

        } else if (Start.equals("[Select Station]")) {

            print("Provide Start Station to continue");
            return false;

        } else if (End.equals("[Select Station]")) {

            print("Provide Destination Station to continue");
            return false;

        } else if (StartTrain.isEmpty()) {

            print("Provide Start Train to continue");
            return false;

        } else if (EndTrain.isEmpty()) {

            print("Provide Return Train to continue");
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


    //Setting Spinner Value
    private void setValue(String s, Spinner spinner) {

        for(int i=0; i<places.length; i++) {

            if (s.equals(places[i].trim())) {

                spinner.setSelection(i);

            }

        }

    }


    private void postData() {

        AddPassReq addPassReq = new AddPassReq("abcde",StartDate, EndDate,Start, End, StartTrain, EndTrain);

        api.attemptPass(addPassReq).enqueue(new Callback<NewPassRes>() {
            @Override
            public void onResponse(Call<NewPassRes> call, Response<NewPassRes> response) {


                if (response.body().getStatus().equals("Done")) {

                    db.putString("passID", response.body().getId());

                }


            }

            @Override
            public void onFailure(Call<NewPassRes> call, Throwable t) {

            }
        });

    }





}
