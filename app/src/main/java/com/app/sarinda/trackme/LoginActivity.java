package com.app.sarinda.trackme;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private Button mLogin;
    private TextView mEmail;
    private TextView mPwd;
    private String email;
    private String pwd;
    private String user="user1@gmail.com";
    private String password="user123";
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLogin=(Button)findViewById(R.id.btn_login);
        mEmail=findViewById(R.id.email);
        mPwd=findViewById(R.id.pwd);
        layout=findViewById(R.id.login);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=mEmail.getText().toString();
                pwd=mPwd.getText().toString();
                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)){
                    if(email.equals(user) && pwd.equals(password))
                    {
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    }
                    else{
                        snackBar("Email or Password is Incorrect!!");
                    }
                }
                else {
                    snackBar("Please Fill the All fields!!");
                }

            }
        });
    }
    public void snackBar(String msg){
        Snackbar snackbar= Snackbar.make(layout,msg,Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
