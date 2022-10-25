package com.example.phone_shop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    Connection connection;

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
    public void DeleteBase(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Предупреждение");
        builder.setMessage("Вы уверены, что хотите полностью и безвозвратно очистить базу?");

        builder.setPositiveButton("Да", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which) {
                ClearData();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void ClearData()
    {
        try
        {
            BaseData baseData = new BaseData();
            connection = baseData.connectionClass();
            if(connection != null) {
                String query = "Delete From Phones";
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "При удаление данных возникла ошибка", Toast.LENGTH_LONG).show();
        }
    }
}