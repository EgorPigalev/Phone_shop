package com.example.phone_shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConclusionList extends AppCompatActivity {

    private AdapterMask pAdapter;
    private List<Mask> listPhone = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conclusion_list);

        ListView ivProducts = findViewById(R.id.lvData);
        pAdapter = new AdapterMask(ConclusionList.this, listPhone);
        ivProducts.setAdapter(pAdapter);

        new GetPhones().execute();
    }

    private class GetPhones extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {

                URL url = new URL("http://localhost:53670/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();

            } catch (Exception exception) {
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
                            productJson.getString("price")
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