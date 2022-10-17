package com.example.phone_shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

public class ConclusionList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conclusion_list);
    }

    public void Exit(View view)
    {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void TextBlockVisibility(View view)
    {
        Button btnSearch = findViewById(R.id.btnSearch);
        TableLayout tlSearch = findViewById(R.id.tlSearch);
        if(tlSearch.getVisibility() == View.VISIBLE)
        {
            btnSearch.setText("Фильтр ▼");
            tlSearch.setVisibility(View.GONE);
        }
        else
        {
            btnSearch.setText("Фильтр ▲");
            tlSearch.setVisibility(View.VISIBLE);
        }
    }

}