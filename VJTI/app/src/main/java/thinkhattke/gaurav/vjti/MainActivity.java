package thinkhattke.gaurav.vjti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import thinkhattke.gaurav.vjti.Util.TinyDB;


public class MainActivity extends AppCompatActivity {


    //UI Components
    private RelativeLayout managePass, Report;


    //Global data
    TinyDB db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Setting Views
        managePass = findViewById(R.id.manage);
        Report = findViewById(R.id.report);


        //Handling OnClicks
        Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , Report.class));
            }
        });


        managePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , Remainder.class));
            }
        });


        //Updating the FireBase Device Token
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {

                String newToken = instanceIdResult.getToken();
                db = new TinyDB(MainActivity.this);
                db.putString("token",newToken);


            }
        });


    }


}
