package com.example.phone_shop;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddingData extends AppCompatActivity {

    EditText textManufacturer, textModel, textColour, textPrice;
    TextView deletePicture;
    ImageView image;
    String varcharPicture;

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
                        TextView deletePicture = findViewById(R.id.tvdeletePicture);
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
        setContentView(R.layout.activity_adding_data);

        textManufacturer = findViewById(R.id.etManufacturer);
        textModel = findViewById(R.id.etModel);
        textColour = findViewById(R.id.etColour);
        textPrice = findViewById(R.id.etPrice);
        image = findViewById(R.id.ivPicture);
        deletePicture = findViewById(R.id.tvdeletePicture);
        varcharPicture = null;

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
    }

    /*
    public void AddData(View v)
    {
        if(textManufacturer.getText().length() == 0 || textModel.getText().length() == 0 || textColour.getText().length() == 0 || textPrice.getText().length() == 0){
            Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_LONG).show();
            return;
        }
        try
        {
            BaseData baseData = new BaseData();
            connection = baseData.connectionClass();
            if(connection != null) {
                String query = "";
                if(varcharPicture == null){
                    query = "Insert into Phones(manufacturer, model, colour, price) Values('" + textManufacturer.getText() + "', '" + textModel.getText() + "', '" + textColour.getText() + "', '" + textPrice.getText() + "')";
                }
                else{
                    query = "Insert into Phones(manufacturer, model, colour, price, image) Values('" + textManufacturer.getText() + "', '" + textModel.getText() + "', '" + textColour.getText() + "', '" + textPrice.getText() + "', '" + varcharPicture + "')";
                }
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
                Toast.makeText(this, "Запись успешно добавлена в базу", Toast.LENGTH_LONG).show();
                textManufacturer.setText("");
                textModel.setText("");
                textColour.setText("");
                textPrice.setText("");
                image.setImageResource(R.drawable.absence);
                deletePicture.setVisibility(View.INVISIBLE);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "При добавление данных в БД возникла ошибка", Toast.LENGTH_LONG).show();
        }
    }

     */
    public void AddData(View v)
    {
        postData(textManufacturer.getText().toString(), textModel.getText().toString(), textColour.getText().toString(), textPrice.getText().toString(), varcharPicture);
    }

    private void postData(String manufacturer, String model, String colour, String price, String picture) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5101/NGKNN/ПигалевЕД/api/Phones/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        DataModal modal = new DataModal(manufacturer, model, colour, Float.parseFloat(price), picture);

        Call<DataModal> call = retrofitAPI.createPost(modal);

        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {

                Toast.makeText(AddingData.this, "Запись успешно добавлена в базу", Toast.LENGTH_LONG).show();
                textManufacturer.setText("");
                textModel.setText("");
                textColour.setText("");
                textPrice.setText("");
                image.setImageResource(R.drawable.absence);
                deletePicture.setVisibility(View.INVISIBLE);

                DataModal responseFromAPI = response.body();

                // on below line we are getting our data from modal class and adding it to our string.
                //String responseString = "Response Code : " + response.code() + "\nName : " + responseFromAPI.getName() + "\n" + "Job : " + responseFromAPI.getJob();

            }

            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.

            }
        });
    }

    public void Back(View view)
    {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void UpdatePicture(View v)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        someActivityResultLauncher.launch(photoPickerIntent);
    }

    public void DeletePicture(View v)
    {
        ImageView picture = (ImageView) findViewById(R.id.ivPicture);
        picture.setImageBitmap(null);
        varcharPicture = null;
        TextView deletePicture = findViewById(R.id.tvdeletePicture);
        picture.setImageResource(R.drawable.absence);
        deletePicture.setVisibility(View.INVISIBLE);
    }
}