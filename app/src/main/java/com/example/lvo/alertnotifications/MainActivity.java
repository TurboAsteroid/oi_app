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

import android.view.Menu;
import android.view.MenuInflater;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

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

    public void updateNotificationStatus(String notification_id, String status) {
        App.getApi().checkNotification(FirebaseInstanceId.getInstance().getToken(), notification_id, status).enqueue(new Callback<simpleResponceModel>() {
            @Override
            public void onResponse(Call<simpleResponceModel> call, Response<simpleResponceModel> response) {
                //Данные успешно пришли, но надо проверить response.body() на null
                Log.d("11111111111111", response.body().getStatus() );
            }
            @Override
            public void onFailure(Call<simpleResponceModel> call, Throwable t) {
                Log.d("11111111111111rrr", t.toString() );

            }
        });
    }
    public void updateData() {
        App.getApi().getUserByToken(FirebaseInstanceId.getInstance().getToken()).enqueue(new Callback<userInfoModel>() {
            @Override
            public void onResponse(Call<userInfoModel> call, Response<userInfoModel> response) {

                //Данные успешно пришли, но надо проверить response.body() на null
                Log.d("11111111111111", response.body().toString() );
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
                Log.d("11111111111111", t.toString() );
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.navigation_exit:
                logout();
                return true;
//            case R.id.navigation_download:
//                loadfile();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    public void logout(){
        App.getApi().logoutUser(FirebaseInstanceId.getInstance().getToken()).enqueue(new Callback<simpleResponceModel>() {
            @Override
            public void onResponse(Call<simpleResponceModel> call, Response<simpleResponceModel> response) {
                //Данные успешно пришли, но надо проверить response.body() на null
                updateData();
            }
            @Override
            public void onFailure(Call<simpleResponceModel> call, Throwable t) {
                Log.d("ERROR", t.toString() );
            }
        });
    }

//    public void loadfile(){
//        final String incident_id = "1";
//        final String fname = "1.docx";
//
//        App.getApi().downloadFile(incident_id, fname).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                boolean writtenToDisk = writeResponseBodyToDisk(response.body(), fname);
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                //Произошла ошибка
//                Log.d("11111111111111", t.toString() );
//            }
//        });
//    }

    public void onNotificationReceive(String notification_id){
        updateData();
        updateNotificationStatus(notification_id, "received");
    }

}