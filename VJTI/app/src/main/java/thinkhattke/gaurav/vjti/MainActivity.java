package thinkhattke.gaurav.vjti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {


    //UI Components
    private RelativeLayout managePass, Report;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Setting Views
        managePass = findViewById(R.id.manage);
        Report = findViewById(R.id.report);


        //Handling Onclicks

        Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , Report.class));
            }
        });


    }


}
