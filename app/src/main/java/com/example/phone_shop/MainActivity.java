package com.example.phone_shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Exit(View view)
    {
        this.finishAffinity();
    }

    public void NextAdd(View view)
    {
        startActivity(new Intent(this, AddingData.class));
    }

    public void NextConclusionList(View view)
    {
        startActivity(new Intent(this, ConclusionList.class));
    }
}