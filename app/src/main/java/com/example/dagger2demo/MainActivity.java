package com.example.dagger2demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerAppComponent.builder().appModule(new AppModule(this)).build().inject(this);

        //here we have not initialized object of shared preference, it's initialised by dagger2
        sharedPreferences.edit().putString("KEY", "HELLO WORLD").apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String text = sharedPreferences.getString("KEY", "");
        Toast.makeText(this, user.getfName(), Toast.LENGTH_LONG).show();
    }
}
