package com.example.phone_shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConclusionList extends AppCompatActivity {

    private AdapterMask pAdapter;
    private List<Mask> listPhone = new ArrayList<>();

    ListView listView;

    EditText textSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conclusion_list);


        ListView ivProducts = findViewById(R.id.lvData);
        pAdapter = new AdapterMask(ConclusionList.this, listPhone);
        ivProducts.setAdapter(pAdapter);

        listView = findViewById(R.id.lvData);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Go((int)id);
            }
        });

        textSearch = findViewById(R.id.etSearch);

        textSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                textSearch.setHint("");
            else
                textSearch.setHint(R.string.enter_value);
        });

        new GetPhones().execute();
    }

    private class GetPhones extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {

                URL url = new URL("https://ngknn.ru:5101/NGKNN/ПигалевЕД/api/Phones");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null)
                {
                    result.append(line);
                }
                return result.toString();
            }
            catch (Exception exception)
            {
                return null;
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try
            {
                JSONArray tempArray = new JSONArray(s);
                for (int i = 0;i<tempArray.length();i++)
                {
                    JSONObject productJson = tempArray.getJSONObject(i);
                    Mask tempProduct = new Mask(
                            productJson.getInt("id_phone"),
                            productJson.getString("manufacturer"),
                            productJson.getString("model"),
                            productJson.getString("colour"),
                            (float) productJson.getDouble("price"),
                            productJson.getString("image")
                    );
                    listPhone.add(tempProduct);
                    pAdapter.notifyDataSetInvalidated();
                }
            }
            catch (Exception ignored)
            {

            }
        }
    }

    public void Go(int id)
    {
        Intent intent = new Intent(this, SingleEntryChange.class);
        Bundle b = new Bundle();
        b.putInt("key", id);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    public void Back(View view)
    {
        startActivity(new Intent(this, MainActivity.class));
    }


    public void TextBlockVisibility(View view)
    {
        Button btnSearch = findViewById(R.id.btnSearch);
        TableLayout tlSearch = findViewById(R.id.tlSearch);
        if(tlSearch.getVisibility() == View.VISIBLE)
        {
            btnSearch.setText(R.string.filter_closed);
            tlSearch.setVisibility(View.GONE);
        }
        else
        {
            btnSearch.setText(R.string.filter_open);
            tlSearch.setVisibility(View.VISIBLE);
        }
    }
}