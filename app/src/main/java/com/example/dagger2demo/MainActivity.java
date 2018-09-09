package com.example.dagger2demo;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeUtils;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    User user;

    private Disposable disposable;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerAppComponent.builder().appModule(new AppModule(this)).build().inject(this);

        //here we have not initialized object of shared preference, it's initialised by dagger2
        sharedPreferences.edit().putString("KEY", "HELLO WORLD").apply();


        Observable<String> stringObservable = Observable.just("Parmesh", "Mhore", "Rohan", "Laxmi", "Raj", "Punna");
        DisposableObserver<String> nameObservers = getNameObservers();

        compositeDisposable.add(nameObservers);
        stringObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.contains("a");
                    }
                })
                .subscribe(nameObservers);

        DisposableObserver<String> allCapsNameObserver = getAllCapsNameObserver();
        compositeDisposable.add(allCapsNameObserver);

        stringObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<String, String>() {

                    @Override
                    public String apply(String s) throws Exception {
                        return s.toUpperCase();
                    }
                })
                .subscribe(allCapsNameObserver);

        Integer array[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        //Observable.range(1, 20)
        Observable.fromArray(array)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer % 2 == 0;
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d("RX-ANDROID", "onNext:"+value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private DisposableObserver<String> getAllCapsNameObserver() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String value) {
                Log.d("RX-ANDROID", "onNext:"+value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private DisposableObserver<String> getNameObservers() {
        return new DisposableObserver<String>() {

            @Override
            public void onNext(String value) {
                Log.d("RX-ANDROID", "onNext:"+value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("RX-ANDROID", "onError");
            }

            @Override
            public void onComplete() {
                Log.d("RX-ANDROID", "onComplete");
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        String text = sharedPreferences.getString("KEY", "");
        Toast.makeText(this, user.getfName(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
