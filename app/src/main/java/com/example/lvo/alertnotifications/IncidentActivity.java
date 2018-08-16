
package com.example.lvo.alertnotifications;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class IncidentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incedent);

        Intent myIntent = getIntent(); // gets the previously created intent
        String notification_id = myIntent.getStringExtra("notification_id");

        App.getApi().getincedentbynotification(notification_id).enqueue(new Callback<incidentModel>() {
            @Override
            public void onResponse(Call<incidentModel> call, Response<incidentModel> response) {
                //Данные успешно пришли, но надо проверить response.body() на null
                Log.d("REQUEST", response.body().toString() );
                incidentModel incident = response.body();

                TextView title = findViewById(R.id.incidenttitle);
                TextView description = findViewById(R.id.incidentdescription);
                TextView datetime = findViewById(R.id.incidenttime);
                TextView solution = findViewById(R.id.incidentsolution);

                Log.d("!!!!!!!!!!!!", incident.getTitle() );
                title.setText(incident.getTitle());
                description.setText(incident.getDescription());
                datetime.setText(incident.getDatetime());
                solution.setText(incident.getSolution());
            }
            @Override
            public void onFailure(Call<incidentModel> call, Throwable t) {
                //Произошла ошибка
                Log.d("ERROR", t.toString() );

            }
        });
    }
}




