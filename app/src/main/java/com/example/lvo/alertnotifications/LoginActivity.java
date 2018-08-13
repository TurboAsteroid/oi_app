
package com.example.lvo.alertnotifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import java.io.IOException;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    userInfoModel user;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }
    public void onSentClick (View view) {
        TextView textViewLogin = findViewById(R.id.loginField);
        TextView textViewPass = findViewById(R.id.passwordField);
        App.getApi().registerUser(FirebaseInstanceId.getInstance().getToken(), textViewLogin.getText().toString(), textViewPass.getText().toString()).enqueue(new Callback<userInfoModel>() {
            @Override
            public void onResponse(Call<userInfoModel> call, Response<userInfoModel> response) {
                //Данные успешно пришли, но надо проверить response.body() на null
                Log.d("REQUEST", response.body().toString() );
                user = response.body();



                if (Integer.parseInt(user.getStatus()) == 0) {
                    TextView textView = findViewById(R.id.loginerror);
                    textView.setVisibility(View.VISIBLE);
                } else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<userInfoModel> call, Throwable t) {
                //Произошла ошибка
                Log.d("ERROR", t.toString() );

            }
        });
    }
}

