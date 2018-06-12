package com.example.roger.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "AppTeste";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();

        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onDestory");
    }
}
