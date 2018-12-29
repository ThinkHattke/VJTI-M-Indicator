package thinkhattke.gaurav.vjti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Login extends AppCompatActivity {


    //UI Components
    private Button login;
    private LinearLayout register;
    private String Email="", Password="";
    private EditText email, password;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Setting Views
        login = findViewById(R.id.loginButton);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);


        //Login Onclicks
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this , MainActivity.class));

            }
        });


    }


}
