package com.example.lvo.alertnotifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import java.io.IOException;
import java.util.List;
import android.widget.ArrayAdapter;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

public class MainActivity extends AppCompatActivity {
    userInfoModel user;
    ArrayAdapter<String> adapter;

    private static final String TAG = "MainActivity";
    private TextView mTextMessage;

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
//                    return true;
//                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
//                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
//            }
//            return false;
//        }
//    };

    public void updateData() {
        App.getApi().getUserByToken(FirebaseInstanceId.getInstance().getToken()).enqueue(new Callback<userInfoModel>() {
            @Override
            public void onResponse(Call<userInfoModel> call, Response<userInfoModel> response) {
                //Данные успешно пришли, но надо проверить response.body() на null
                Log.d("REQUEST", response.body().toString() );
                user = response.body();

                if (Integer.parseInt(user.getStatus()) == 0) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    TextView textView = findViewById(R.id.greeting);
                    textView.setText(String.format(getString(R.string.hello), user.getName()));

                    List<NotificationModel> att= user.getNotification();

                    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                    mRecyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
                    mRecyclerView.setLayoutManager(mLayoutManager);

                    RecyclerView.Adapter mAdapter = new MyAdapter(att);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
            @Override
            public void onFailure(Call<userInfoModel> call, Throwable t) {
                //Произошла ошибка
                Log.d("ERROR", t.toString() );
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Delegate.theMainActivity = this;
        setContentView(R.layout.activity_main);

//        updateData();



//        mTextMessage = (TextView) findViewById(R.id.message);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Create channel to show notifications.
//            String channelId  = getString(R.string.default_notification_channel_id);
//            String channelName = getString(R.string.default_notification_channel_name);
//            NotificationManager notificationManager =
//                    getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
//                    channelName, NotificationManager.IMPORTANCE_LOW));
//        }
//
//        if (getIntent().getExtras() != null) {
//            for (String key : getIntent().getExtras().keySet()) {
//                Object value = getIntent().getExtras().get(key);
//                Log.d(TAG, "Key: " + key + " Value: " + value);
//            }
//        }

//        Button logTokenButton = findViewById(R.id.logTokenButton);
//        logTokenButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Get token
//                String token = FirebaseInstanceId.getInstance().getToken();
//
//
//                // Log and toast
//                String msg = getString(R.string.msg_token_fmt, token);
//                Log.d(TAG, msg);
//                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public void onNotificationReceive(){
        updateData();
    }
}

