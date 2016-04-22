package com.liye.richedittextdemo.demo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.liye.richedittextdemo.R;

public class MainActivity extends AppCompatActivity {
    private Button mButtonEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButtonEdit = (Button) findViewById(R.id.button_edit);
        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditActivity();
            }
        });
    }

    private void startEditActivity() {
        startActivity(new Intent(this, EditDemoActivity.class));
    }
}
