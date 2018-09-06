package com.example.dagger2demo;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Context mContext;

    AppModule(Context context) {
        mContext = context;
    }

    @Provides
    public SharedPreferences provideSharedPref() {
        return mContext.getSharedPreferences("shared_pref", Context.MODE_PRIVATE);
    }

    @Provides
    public User getUser() {
        return new User("Parmesh", "Mahore");
    }
}
