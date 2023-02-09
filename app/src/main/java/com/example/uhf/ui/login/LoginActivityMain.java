package com.example.uhf.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhf.R;
import com.example.uhf.activity.BaseTabFragmentActivity;
import com.example.uhf.activity.UHFMainActivity;
import com.example.uhf.ui.login.LoginViewModel;
import com.example.uhf.ui.login.LoginViewModelFactory;

public class LoginActivityMain extends Activity {
private Button login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        initPageViews();



    }

    private void initPageViews() {
        login = (Button) findViewById(R.id.login);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivityMain.this, "Prijava!", Toast.LENGTH_SHORT).show();



                Intent myIntent = new Intent(getApplicationContext(), UHFMainActivity.class);
                startActivity(myIntent);


            }
        });
    }

}