package com.example.phone_shop;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SingleEntryChange extends AppCompatActivity {

    int index;
    EditText textManufacturer, textModel, textColour, textPrice;
    ImageView image;
    TextView deletePicture;
    String varcharPicture;
    ProgressBar loadingPB;

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Bitmap bitmap = null;
                    ImageView imageView = (ImageView) findViewById(R.id.ivPicture);
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Uri selectedImage = result.getData().getData();
                        try
                        {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        imageView.setImageBitmap(null);
                        imageView.setImageBitmap(bitmap);
                        TextView deletePicture = findViewById(R.id.tvDeletePicture);
                        deletePicture.setVisibility(View.VISIBLE);
                        varcharPicture = BitMapToString(bitmap);
                    }
                }
            });

    public String BitMapToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_entry_change);

        textManufacturer = findViewById(R.id.etManufacturer);
        textModel = findViewById(R.id.etModel);
        textColour = findViewById(R.id.etColour);
        textPrice = findViewById(R.id.etPrice);
        image = findViewById(R.id.ivPicture);
        deletePicture = findViewById(R.id.tvDeletePicture);
        loadingPB = findViewById(R.id.pbLoading);

        textManufacturer.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                textManufacturer.setHint("");
            else
                textManufacturer.setHint(R.string.firm);
        });

        textModel.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                textModel.setHint("");
            else
                textModel.setHint(R.string.model);
        });

        textColour.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                textColour.setHint("");
            else
                textColour.setHint(R.string.colour);
        });

        textPrice.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                textPrice.setHint("");
            else
                textPrice.setHint(R.string.price);
        });
        Bundle arguments = getIntent().getExtras();
        index = arguments.getInt("key");
        GetData();
    }

    public void Back(View view)
    {
        startActivity(new Intent(this, ConclusionList.class));
    }


    public void GetData()
    {
        loadingPB.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5101/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<DataModal> call = retrofitAPI.getDATA(index);
        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {
                loadingPB.setVisibility(View.INVISIBLE);
                if(!response.isSuccessful())
                {
                    Toast.makeText(SingleEntryChange.this, "При выводе данных возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                textManufacturer.setText(response.body().getManufacturer());
                textModel.setText(response.body().getModel());
                textColour.setText(response.body().getColour());
                textPrice.setText(response.body().getPrice().toString());
                if(response.body().getImage() == null)
                {
                    image.setImageResource(R.drawable.absence);
                    deletePicture.setVisibility(View.INVISIBLE);
                }
                else
                {
                    Bitmap bitmap = StringToBitMap(response.body().getImage());
                    image.setImageBitmap(bitmap);
                    deletePicture.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {
                Toast.makeText(SingleEntryChange.this, "При выводе данных возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                loadingPB.setVisibility(View.INVISIBLE);
            }
        });
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void updateLine(View v)
    {
        if(textManufacturer.getText().length() == 0 || textModel.getText().length() == 0 || textColour.getText().length() == 0 || textPrice.getText().length() == 0){
            Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_LONG).show();
            return;
        }
        callPUTDataMethod(textManufacturer.getText().toString(), textModel.getText().toString(), textColour.getText().toString(), textPrice.getText().toString(), varcharPicture);
    }

    public void deleteLine(View v)
    {
        callDeleteDataMethod();
        SystemClock.sleep(200);
        Back(v);
    }

    private void callDeleteDataMethod() {

        loadingPB.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5101/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call call = retrofitAPI.deleteData(index);
        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {
                loadingPB.setVisibility(View.INVISIBLE);
                if(!response.isSuccessful())
                {
                    Toast.makeText(SingleEntryChange.this, "При удание записи возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(SingleEntryChange.this, "Удаление прошло успешно", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {
                Toast.makeText(SingleEntryChange.this, "При удаление записи возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                loadingPB.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void callPUTDataMethod(String manufacturer, String model, String colour, String price, String picture) {

        loadingPB.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5101/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        DataModal modal = new DataModal(manufacturer, model, colour, Float.parseFloat(price), picture);
        Call<DataModal> call = retrofitAPI.updateData(index, modal);
        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {
                loadingPB.setVisibility(View.INVISIBLE);
                if(!response.isSuccessful())
                {
                    Toast.makeText(SingleEntryChange.this, "При изменение данных возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(SingleEntryChange.this, "Данные изменены", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {
                Toast.makeText(SingleEntryChange.this, "При изменение записи возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                loadingPB.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void updatePicture(View v)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        someActivityResultLauncher.launch(photoPickerIntent);
    }

    public void deletePicture(View v)
    {
        ImageView picture = (ImageView) findViewById(R.id.ivPicture);
        picture.setImageBitmap(null);
        varcharPicture = null;
        TextView deletePicture = findViewById(R.id.tvDeletePicture);
        picture.setImageResource(R.drawable.absence);
        deletePicture.setVisibility(View.INVISIBLE);
    }
}