package thinkhattke.gaurav.vjti;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Objects;

import thinkhattke.gaurav.vjti.Util.TinyDB;

public class Remainder extends AppCompatActivity {


    //UI Components
    private TextView startDate, endDate, startTrain, endTrain;
    private Spinner start, end;
    private ImageView image;


    //Global Data
    TinyDB db;


    //Local Data
    String places[] = {"[Select Station]","Nallasopara", "Virar", "Vasai", "Bhayandar", "Dahisar", "Borivali", "Kandivali", "Dadar", "Bandra"};
    String Start, End, StartTrain, EndTrain;
    private static final int SELECT_PHOTO = 1;


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


        //Intialising TinyDB
        db = new TinyDB(Remainder.this);


        //checking for previously saved data
        if (db.getString("pass").equals("yes")) {





        }


        //Setting Spinners
        ArrayAdapter<String> minorAdapter = new ArrayAdapter<String>(Remainder.this, android.R.layout.simple_spinner_item, places);
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


    //Function to handle OnClicks
    private void OnClicks() {


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
                dialog = new Dialog(Remainder.this);
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
                dialog = new Dialog(Remainder.this);
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


    }


    //Setting start date using Custom DatePicker
    private void setDate(final TextView view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(Remainder.this);
                final View pickDate = inflater.inflate(R.layout.layout_date_pick, null);
                new AlertDialog.Builder(Objects.requireNonNull(Remainder.this))
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


    //


}
