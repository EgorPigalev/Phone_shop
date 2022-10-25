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
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SingleEntryChange extends AppCompatActivity {

    Connection connection;
    Integer index;
    EditText textManufacturer, textModel, textColour, textPrice;
    ImageView image;
    TextView deletePicture;

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
                        String varcharPicture = BitMapToString(bitmap);
                        addPicture(varcharPicture);
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

    public void addPicture(String varcharPicture)
    {
        try
        {
            BaseData baseData = new BaseData();
            connection = baseData.connectionClass();
            if(connection != null) {
                String query;
                if(varcharPicture == ""){
                    query = "Update Phones Set image = null where id_phone = " + index;
                }
                else{
                    query = "Update Phones Set image = '" + varcharPicture + "' where id_phone = " + index;
                }
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "При добавление картинки возникла ошибка!", Toast.LENGTH_LONG).show();
        }
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
        textManufacturer = findViewById(R.id.etManufacturer);
        textModel = findViewById(R.id.etModel);
        textColour = findViewById(R.id.etColour);
        textPrice = findViewById(R.id.etPrice);
        deletePicture = findViewById(R.id.tvDeletePicture);
        image = findViewById(R.id.ivPicture);
        try
        {
            BaseData baseData = new BaseData();
            connection = baseData.connectionClass();
            if(connection != null) {
                String query = "Select * From Phones where id_phone = " + index;
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next())
                {
                    textManufacturer.setText(resultSet.getString(2).replaceAll("\\s+",""));
                    textModel.setText(resultSet.getString(3).replaceAll("\\s+",""));
                    textColour.setText(resultSet.getString(4).replaceAll("\\s+",""));
                    textPrice.setText(resultSet.getString(5));
                    if(resultSet.getString(6) == null)
                    {
                        image.setImageResource(R.drawable.absence);
                        deletePicture.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        Bitmap bitmap = StringToBitMap(resultSet.getString(6));
                        image.setImageBitmap(bitmap);
                        deletePicture.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "При выводе данных произошла ошибка", Toast.LENGTH_LONG).show();
        }
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

    public void deleteLine(View v)
    {
        try
        {
            BaseData baseData = new BaseData();
            connection = baseData.connectionClass();
            if(connection != null) {
                String query = "Delete From Phones where id_phone = " + index;
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "При удаление данных возникла ошибка", Toast.LENGTH_LONG).show();
        }
        Back(v);
    }

    public void updateLine(View v)
    {
        textManufacturer = findViewById(R.id.etManufacturer);
        textModel = findViewById(R.id.etModel);
        textColour = findViewById(R.id.etColour);
        textPrice = findViewById(R.id.etPrice);
        deletePicture = findViewById(R.id.tvDeletePicture);
        image = findViewById(R.id.ivPicture);
        if(textManufacturer.getText().length() == 0 || textModel.getText().length() == 0 || textColour.getText().length() == 0 || textPrice.getText().length() == 0)
        {
            Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_LONG).show();
            return;
        }
        try
        {
            BaseData baseData = new BaseData();
            connection = baseData.connectionClass();
            if(connection != null) {
                String query = "Update Phones Set manufacturer = '" + textManufacturer.getText() + "', model = '" + textModel.getText() + "', colour = '" + textColour.getText() + "', price = '" + textPrice.getText() + "' where id_phone = " + index;
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "При изменение данных в БД возникла ошибка", Toast.LENGTH_LONG).show();
        }
        Back(v);
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
        addPicture("");
        TextView deletePicture = findViewById(R.id.tvDeletePicture);
        picture.setImageResource(R.drawable.absence);
        deletePicture.setVisibility(View.INVISIBLE);
    }
}