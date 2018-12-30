package thinkhattke.gaurav.vjti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Train extends AppCompatActivity {


    //UI Components
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);


        //Setting views
        back = findViewById(R.id.back);


        //Handling OnClicks
        OnClicks();


    }


    //Functions to handle OnClicks
    private void OnClicks() {


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


}
