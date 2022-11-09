package com.example.phone_shop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingPB = findViewById(R.id.pbLoading);
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
                callDeleteBaseDataMethod();
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

    private void callDeleteBaseDataMethod() {

        loadingPB.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call call = retrofitAPI.deleteBasaData();
        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {
                loadingPB.setVisibility(View.INVISIBLE);
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "При удание данных возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(MainActivity.this, "Удаление прошло успешно", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {
                Toast.makeText(MainActivity.this, "При удаление записи возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                loadingPB.setVisibility(View.INVISIBLE);
            }
        });
    }
}