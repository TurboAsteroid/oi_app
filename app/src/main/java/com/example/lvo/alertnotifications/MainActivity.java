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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                    List<Notification> att= user.getNotification();
                    if(att.size()!=0) {
                        String[] names = new String[att.size()];
                        int a = 0;
                        for (Notification x : att) {
                            names[a] = x.getTitle();
                            a = a + 1;
                        }
                        ListView listView = findViewById(R.id.mylist);
                        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, names);
                        listView.setAdapter(adapter);
//                        listView.setOnItemClickListener(new OnItemClickListener() {
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                Toast.makeText(MainActivity.this, "itemClick: position = " + position + ", id = " + id, Toast.LENGTH_SHORT).show();
//                                adapter.remove(adapter.getItem(position));
//                                adapter.notifyDataSetChanged();
//                            }
//                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<userInfoModel> call, Throwable t) {
                //Произошла ошибка
                Log.d("ERROR", t.toString() );

            }
        });


        mTextMessage = (TextView) findViewById(R.id.message);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

        Button logTokenButton = findViewById(R.id.logTokenButton);
        logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get token
                String token = FirebaseInstanceId.getInstance().getToken();


                // Log and toast
                String msg = getString(R.string.msg_token_fmt, token);
                Log.d(TAG, msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

