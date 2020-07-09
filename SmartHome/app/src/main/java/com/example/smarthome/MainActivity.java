package com.example.smarthome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button bulb_on;
    private Button bulb_off;
    private Button fan_on;
    private Button fan_off;
    ProgressBar loading;
    TextView status;
    FirebaseDatabase database;
    DatabaseReference docref_bulb;
    DatabaseReference docref_fan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bulb_on = findViewById(R.id.bulb_on);
        bulb_off = findViewById(R.id.bulb_off);
        fan_on = findViewById(R.id.fan_on);
        fan_off = findViewById(R.id.fan_off);
        loading = findViewById(R.id.loading);
        status = findViewById(R.id.status);

        loading.setVisibility(View.VISIBLE);
        status.setVisibility(View.VISIBLE);
        database = FirebaseDatabase.getInstance();
        docref_bulb = database.getReference("bulb_status");
        docref_fan = database.getReference("fan_status");

        docref_bulb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String bulb = snapshot.getValue().toString();
                if(bulb.equals("1")){
                    bulb_off.setVisibility(View.VISIBLE);
                }else{
                    bulb_on.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        docref_fan.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fan = snapshot.getValue().toString();
                if(fan.equals("1")){
                    fan_off.setVisibility(View.VISIBLE);
                }else{
                    fan_on.setVisibility(View.VISIBLE);
                }
                loading.setVisibility(View.INVISIBLE);
                status.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bulb_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docref_bulb.setValue(0);
                bulb_off.setVisibility(View.INVISIBLE);
                bulb_on.setVisibility(View.VISIBLE);
            }
        });
        bulb_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docref_bulb.setValue(1);
                bulb_on.setVisibility(View.INVISIBLE);
                bulb_off.setVisibility(View.VISIBLE);
            }
        });

        fan_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docref_fan.setValue(0);
                fan_off.setVisibility(View.INVISIBLE);
                fan_on.setVisibility(View.VISIBLE);
            }
        });
        fan_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docref_fan.setValue(1);
                fan_on.setVisibility(View.INVISIBLE);
                fan_off.setVisibility(View.VISIBLE);
            }
        });
    }
}