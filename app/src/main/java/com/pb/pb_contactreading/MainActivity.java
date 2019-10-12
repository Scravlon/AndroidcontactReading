package com.pb.pb_contactreading;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    Button but_con;
    Button but_vcf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        but_con = findViewById(R.id.but_con);
        but_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AllContactActivity.class);
                startActivity(intent);
            }
        });
        but_vcf = findViewById(R.id.but_vcf);
        but_vcf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AllVCFActivity.class);
                startActivity(intent);
            }
        });


    }
}
