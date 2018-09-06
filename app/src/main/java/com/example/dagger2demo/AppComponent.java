package com.example.dagger2demo;

import android.content.SharedPreferences;

import dagger.Component;
import dagger.Provides;

@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(MainActivity activity);
}
