package thinkhattke.gaurav.vjti;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.pusher.pushnotifications.PushNotifications;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import thinkhattke.gaurav.vjti.Util.TinyDB;


public class MainActivity extends AppCompatActivity {


    //UI Components
    private RelativeLayout managePass, Report;
    private LinearLayout local, bus;


    //Global data
    TinyDB db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Setting Views
        managePass = findViewById(R.id.manage);
        Report = findViewById(R.id.report);
        local = findViewById(R.id.local);
        bus = findViewById(R.id.bus);


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
                startActivity(new Intent(MainActivity.this , Pass.class));
            }
        });


        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , Train.class));
            }
        });


        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


        //Setting Pusher
        PushNotifications.start(getApplicationContext(), "814cd602-bfe8-42fe-ba72-1c601136f7ea");
        PushNotifications.subscribe("hello");


    }



}
