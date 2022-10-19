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

    //private AdapterMask pAdapter;
    //private List<Mask> listPhone = new ArrayList<>();
    Connection connection;
    List<Mask> data;
    ListView listView;
    AdapterMask pAdapter;
    EditText textSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conclusion_list);

        /*
        ListView ivProducts = findViewById(R.id.lvData);
        pAdapter = new AdapterMask(ConclusionList.this, listPhone);
        ivProducts.setAdapter(pAdapter);

         */

        textSearch = findViewById(R.id.etSearch);

        textSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                textSearch.setHint("");
            else
                textSearch.setHint(R.string.enter_value);
        });

        RequestExecution("Select * From Phones");
        //new GetPhones().execute();
    }

    /*
    private class GetPhones extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {

                URL url = new URL("http://localhost:53670/Phones");
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
     */
    public void enterMobile() {
        pAdapter.notifyDataSetInvalidated();
        listView.setAdapter(pAdapter);
    }
    public void RequestExecution(String query) {
        data = new ArrayList<Mask>();
        listView = findViewById(R.id.lvData);
        pAdapter = new AdapterMask(ConclusionList.this, data);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Go((int)id);
            }
        });
        try {
            BaseData baseData = new BaseData();
            connection = baseData.connectionClass();
            if (connection != null)
            {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next())
                {
                    Mask tempMask = new Mask
                            (resultSet.getInt("id_phone"),
                                    resultSet.getString("manufacturer"),
                                    resultSet.getString("model"),
                                    resultSet.getString("colour"),
                                    resultSet.getFloat("price"),
                                    resultSet.getString("image")
                            );
                    data.add(tempMask);
                    pAdapter.notifyDataSetInvalidated();
                }
                connection.close();
            }
            else
            {

            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        enterMobile();
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